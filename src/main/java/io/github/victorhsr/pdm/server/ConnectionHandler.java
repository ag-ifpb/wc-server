/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.server;

import io.github.victorhsr.pdm.data.RecordStorage;
import io.github.victorhsr.pdm.data.VideoDataStream;
import io.github.victorhsr.pdm.streaming.CamStreaming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class ConnectionHandler extends Thread {

    private final Cam camClient;
    private final InputStream in;
    private final OutputStream out;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private boolean holdConnection = false;

    public ConnectionHandler(Cam camClient) throws IOException {
        super("Thread of " + camClient.getCamCode());

        this.camClient = camClient;

        this.in = camClient.getCamSocket().getInputStream();
        this.out = camClient.getCamSocket().getOutputStream();
        this.dis = new DataInputStream(in);
        this.dos = new DataOutputStream(out);
    }

    @Override
    public void run() {

        try {

            dos.writeUTF(camClient.getCamCode());

            int command;

            while (camClient.isConnected()) {

                if (dis.available() > 0) {
                    command = dis.readInt();

                    switch (command) {

                        case 1:
                            recordCam();
                            break;
                        case 2:
                            liveStreaming();
                            break;
                        case 4:
                            requestRecord();
                            break;
                        case 5:
                            requestLiveStreaming();
                            break;
                    }
                }
                sleep(20);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void recordCam() throws IOException, InterruptedException {

        VideoDataStream vds = new VideoDataStream(true);

        while (true) {
            try {
                if (dis.available() > 0) {
                    vds.processFrame(dis);
                } else {
                    //escrita de bytes apenas para detectar o fechamento do socket
                    dos.write(Byte.MIN_VALUE);
                    dos.flush();
                }
            } catch (Exception ex) {
                CamRegister.removeCamClient(camClient);
                break;
            }
            sleep(20);
        }

        RecordStorage recordStorage = new RecordStorage();

        recordStorage.saveVideo(vds.getBuffer(), camClient.getCamCode());

        vds.cleanBuffer();
    }

    private void liveStreaming() throws IOException, InterruptedException {

        CamStreaming camStreaming = new CamStreaming();
        VideoDataStream vds = new VideoDataStream(true);
        byte[] frame;

        if (camStreaming.prepareStream(in)) {

            while (true) {
                try {
                    if (dis.available() > 0) {
                        frame = vds.processFrame(dis);
                        try {
                            camStreaming.sendData(frame);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        //escrita de bytes apenas para detectar o fechamento do socket
                        dos.write(Byte.MIN_VALUE);
                        dos.flush();
                    }
                } catch (Exception ex) {
                    CamRegister.removeCamClient(camClient);
                    break;
                }
                sleep(20);
            }

            RecordStorage recordStorage = new RecordStorage();

            recordStorage.saveVideo(vds.getBuffer(), camClient.getCamCode());

            vds.cleanBuffer();

        }

    }

    private void requestRecord() throws IOException {

        String targetCamCode = dis.readUTF();
        System.out.println("targetCamCode = " + targetCamCode);
        Cam targetCam = CamRegister.findRegisteredCam(targetCamCode);

        DataOutputStream dos = new DataOutputStream(targetCam.getCamSocket().getOutputStream());

        dos.writeInt(1);
    }

    private void requestLiveStreaming() throws IOException {

        String targetCamCode = dis.readUTF();
        System.out.println("targetCamCode = " + targetCamCode);
        Cam targetCam = CamRegister.findRegisteredCam(targetCamCode);

        DataOutputStream targetDos = new DataOutputStream(targetCam.getCamSocket().getOutputStream());

        targetDos.writeInt(2);
        targetDos.writeUTF(camClient.getCamCode());
    }

}
