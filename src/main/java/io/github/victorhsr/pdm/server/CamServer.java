/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class CamServer implements Runnable {

    private ServerSocket serverSocket;
    private CamRegister camRegister;
    private int port = 1099;

    @Override
    public void run() {

        try {
            camRegister = new CamRegister();
            serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {

                Socket camClient = serverSocket.accept();

                camRegister.addCamClient(camClient);
            }

        } catch (IOException ex) {
            Logger.getLogger(CamServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void stopServer() throws IOException {
        serverSocket.close();
    }

}
