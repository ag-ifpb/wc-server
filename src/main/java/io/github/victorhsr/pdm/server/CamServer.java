/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class CamServer implements Runnable {

    private ServerSocket serverSocket;
    private final int port = 3000;

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {

                Socket camClient = serverSocket.accept();
                camClient.setKeepAlive(true);
                CamRegister.addCamClient(camClient);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void stopServer() throws IOException {
        serverSocket.close();
    }

}
