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

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = normalizePort();
        }

        tomcat.setPort(Integer.valueOf(webPort));

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());       

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        startSocketServer();

        tomcat.start();
        tomcat.getServer().await();
    }

    private static void startSocketServer() throws IOException, InterruptedException {

        CamServer camServer = new CamServer();

        Thread serverThread = new Thread(camServer, "Server Thread");

        serverThread.start();
    }

    private static String normalizePort() {

        String portEnv = System.getenv("PORT");

        portEnv = portEnv == null || portEnv.isEmpty() ? "8080" : portEnv;

        return portEnv;
    }
    
}
