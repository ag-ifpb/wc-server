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

    private Cam clientTarget;

    public boolean prepareStream(InputStream in) {

        try {

            DataInputStream dis = new DataInputStream(in);

            String targetCamCode = dis.readUTF();

            clientTarget = CamRegister.findRegisteredCam(targetCamCode);

            if (clientTarget == null) {
                return false;
            }

            return true;

        } catch (IOException ex) {
            return false;
        }

    }

    public void sendData(byte[] data) throws IOException {

        OutputStream out = clientTarget.getCamSocket().getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        System.out.println("data leng "+data.length);
        dos.writeInt(data.length);
        dos.write(data);
    }

}
