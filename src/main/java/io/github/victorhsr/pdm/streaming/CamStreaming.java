/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.streaming;

import io.github.victorhsr.pdm.server.Cam;
import io.github.victorhsr.pdm.server.CamRegister;
import io.github.victorhsr.pdm.server.ConnectionHandler;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class CamStreaming {

    private Cam camTarget;
    private DataInputStream dis;
    private DataOutputStream dos;

    /**
     * Prepares the stream, obtaining data on the transmission target
     *
     * @param in The InputStream that contains the target data
     * @return True if the operation completed successfully, otherwise, returns
     * False
     */
    public boolean prepareStream(InputStream in) {

        try {

            DataInputStream tempDis = new DataInputStream(in);
            System.out.println("vou pegar o target");
            String targetCamCode = tempDis.readUTF();
            System.out.println("targetCamCode = " + targetCamCode);
            camTarget = CamRegister.findRegisteredCam(targetCamCode);
            dos = new DataOutputStream(camTarget.getCamSocket().getOutputStream());
            dis = new DataInputStream(camTarget.getCamSocket().getInputStream());

            if (camTarget == null) {
                return false;
            }

            return true;

        } catch (IOException ex) {
            return false;
        }

    }

    /**
     * Send data to the camTarget
     *
     * @param data The data to be sent
     * @throws IOException
     */
    public void sendData(byte[] data) throws IOException {

        dos.writeInt(data.length);
        dos.write(data);
    }

    public boolean isTargetConnected() throws IOException {

        if(dis.available() > 0){
            int status = dis.readInt();

            return status == ConnectionHandler.CONTINUE_STREAM_CODE;
        }
        
        return true;
    }

}
