/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import io.github.victorhsr.pdm.server.CamServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        CamServer camServer = new CamServer();
        
        Thread serverThread = new Thread(camServer, "Server Thread");
        
        serverThread.start();
//        ServerSocket serverSocket = new ServerSocket(3000);
//
//        Socket cliSocket = serverSocket.accept();
//
//        InputStream in = cliSocket.getInputStream();
//
//        while (true) {
//            System.out.println(cliSocket.isClosed());
//            System.out.println(cliSocket.isInputShutdown());
//            System.out.println(cliSocket.isOutputShutdown());
//            Thread.sleep(500);
//        }
//
//        InputStream nao_usar = cliSocket.getInputStream();
//        DataInputStream dis = new DataInputStream(nao_usar);;
        //        System.out.println("command " + dis.readInt());
//        int bytesRead = dis.readInt();
//        System.out.println("bytesRead = " + bytesRead);
//        while (true) {
//            if(dis.read() > 0){
//                int length = dis.readInt();
//                System.out.println("length = " + length);
//                wh
//            }
//        }
//            if (dis.available() > 0) {
//                int totalQueDeveSerLido;
//
//                byte[] bytesDoFrame;
//
//                totalQueDeveSerLido = dis.readInt();
//                bytesDoFrame = new byte[totalQueDeveSerLido];
//
//                int lidoAteAgora = 0;
//                System.out.println("frame = " + totalQueDeveSerLido);
//                while (lidoAteAgora < totalQueDeveSerLido) {
//                    int aux = dis.read(bytesDoFrame, lidoAteAgora, totalQueDeveSerLido - lidoAteAgora);
//                    if (aux > 0) {
//                        lidoAteAgora += aux;
//                    }
//                }
//            }
//            frame = new byte[frameLength];
//
//            int len = 0;
//            System.out.println("1");
//        }
//        byte[] b = new byte[1024];
//
//        while (in.read() != -1) {
//            in.read(b);
//            System.out.println(new String(b));
//        }

    }

}
