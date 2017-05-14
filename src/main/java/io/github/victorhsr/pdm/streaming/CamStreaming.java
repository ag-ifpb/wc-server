/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.streaming;

import io.github.victorhsr.pdm.server.Cam;
import io.github.victorhsr.pdm.server.CamRegister;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class CamStreaming {

    private Cam camTarget;

    /**
     * Prepares the stream, obtaining data on the transmission target
     *
     * @param in The InputStream that contains the target data
     * @return True if the operation completed successfully, otherwise, returns
     * False
     */
    public boolean prepareStream(InputStream in) {

        try {

            DataInputStream dis = new DataInputStream(in);

            String targetCamCode = dis.readUTF();

            camTarget = CamRegister.findRegisteredCam(targetCamCode);

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

        OutputStream out = camTarget.getCamSocket().getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(data.length);
        dos.write(data);
    }

}
