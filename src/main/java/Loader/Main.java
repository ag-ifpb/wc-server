/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Loader;

import io.github.victorhsr.pdm.server.CamServer;
import java.io.File;
import java.io.IOException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class Main {

    public static void main(String[] args) throws Exception {

        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        //The port that we should run on can be set into an environment variable
        //Look for that variable and default to 8080 if it isn't there.
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = normalizePort();
        }

        tomcat.setPort(Integer.valueOf(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        startSocketServer();

        tomcat.start();
        tomcat.getServer().await();
    }

    public static void startSocketServer() throws IOException, InterruptedException {

        CamServer camServer = new CamServer();

        Thread serverThread = new Thread(camServer, "Server Thread");

        System.out.println("-=ligando o server socket=-");
        serverThread.start();
    }

    private static String normalizePort() {

        String portEnv = System.getenv("PORT");

        portEnv = portEnv == null || portEnv.isEmpty() ? "8080" : portEnv;

        return portEnv;
    }
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
//            System.out.println("command " + dis.readInt());
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
//    }
}
