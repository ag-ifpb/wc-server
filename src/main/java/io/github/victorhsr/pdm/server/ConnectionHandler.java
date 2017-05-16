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

    public static final int RECORD_CAM_COMMAND = 1;
    public static final int LIVE_STREAMING_COMMAND = 2;
    public static final int REQUEST_RECORD_COMMAND = 3;
    public static final int REQUEST_LIVE_STREAMING_COMMAND = 4;
    public static final int NOT_FOUND_CODE = 404;
    public static final int SUCCESS_CODE = 200;
    public static final int END_OF_STREAM_CODE = 201;
    public static final int CONTINUE_STREAM_CODE = 206;

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
                    System.out.println("command = " + command);
                    switch (command) {

                        case RECORD_CAM_COMMAND:
                            recordCam();
                            break;
                        case LIVE_STREAMING_COMMAND:
                            liveStreaming();
                            break;
                        case REQUEST_RECORD_COMMAND:
                            requestRecord();
                            break;
                        case REQUEST_LIVE_STREAMING_COMMAND:
                            requestLiveStreaming();
                            break;
                    }
                    System.out.println(".-.");
                }
                sleep(20);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Record the video data sends by the camClient
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void recordCam() throws IOException, InterruptedException {

        VideoDataStream vds = new VideoDataStream(true);

        while (true) {
            try {
                if (dis.available() > 0) {
                    vds.processFrame(dis);
                } else {
                    //Writing bytes only to detect the socket closing
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

    /**
     * Stream video data between two camClients and stores it simultaneously
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private void liveStreaming() throws IOException, InterruptedException {

        CamStreaming camStreaming = new CamStreaming();
        VideoDataStream vds = new VideoDataStream(true);
        byte[] frame;

        if (camStreaming.prepareStream(in)) {

            while (camStreaming.isTargetConnected()) {
                try {

                    if (dis.available() > 0) {
                        frame = vds.processFrame(dis);
                        camStreaming.sendData(frame);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
                sleep(20);
            }
            System.out.println("saiu do while...");
            dos.writeInt(END_OF_STREAM_CODE);

            RecordStorage recordStorage = new RecordStorage();

            recordStorage.saveVideo(vds.getBuffer(), camClient.getCamCode());

            vds.cleanBuffer();

        }

    }

    /**
     * Requests video recording to another camClient
     *
     * @throws IOException
     */
    private void requestRecord() throws IOException {

        String targetCamCode = dis.readUTF();

        Cam targetCam = CamRegister.findRegisteredCam(targetCamCode);

        try {
            DataOutputStream targetDos = new DataOutputStream(targetCam.getCamSocket().getOutputStream());
            targetDos.writeInt(RECORD_CAM_COMMAND);

            dos.writeInt(SUCCESS_CODE);
        } catch (Exception e) {
            dos.writeInt(NOT_FOUND_CODE);
        }
    }

    /**
     * Requests video live streaming to another camClient
     *
     * @throws IOException
     */
    private void requestLiveStreaming() throws IOException {

        String targetCamCode = dis.readUTF();

        Cam targetCam = CamRegister.findRegisteredCam(targetCamCode);

        try {
            DataOutputStream targetDos = new DataOutputStream(targetCam.getCamSocket().getOutputStream());

            targetDos.writeInt(LIVE_STREAMING_COMMAND);
            targetDos.writeUTF(camClient.getCamCode());

            dos.writeInt(SUCCESS_CODE);

        } catch (Exception e) {
            e.printStackTrace();
            dos.writeInt(NOT_FOUND_CODE);
        }
    }

}
