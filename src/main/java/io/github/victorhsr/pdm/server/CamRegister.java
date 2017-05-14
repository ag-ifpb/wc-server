/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.server;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class CamRegister {

    private static final CopyOnWriteArrayList<Cam> CAM_CLIENTS = new CopyOnWriteArrayList<>();

    public static Cam findRegisteredCam(String camCode) {

        List<Cam> collect = CAM_CLIENTS.stream().filter(camClient -> camClient.getCamCode().equals(camCode)).collect(Collectors.toList());

        return collect.isEmpty() ? null : collect.get(0);
    }

    public static void addCamClient(Socket camSocket) throws IOException {

        String camCode = generateCamCode();

        while (existsCamcode(camCode)) {

            camCode = generateCamCode();
        }

        Cam camClient = new Cam(camSocket, camCode);

        CAM_CLIENTS.add(camClient);

        new ConnectionHandler(camClient).start();
    }

    public static void removeCamClient(Cam camClient) {

        try {

            if (camClient.getCamSocket().isConnected()) {
                camClient.getCamSocket().close();
            }

        } catch (IOException ex) {
            Logger.getLogger(CamRegister.class.getName()).log(Level.SEVERE, null, ex);
        }

        CAM_CLIENTS.remove(camClient);
    }

    private static boolean existsCamcode(String camCode) {

        return CAM_CLIENTS.stream().anyMatch(camClient -> camClient.getCamCode().equals(camCode));
    }

    private static String generateCamCode() {

        String camCode;

        int random = Math.abs(new Random().nextInt());

        int hashtags = (random % 100);
        hashtags = hashtags < 10 ? hashtags * 10 : hashtags;

        StringBuilder at = new StringBuilder();

        for (int i = 0; i < 6; i++) {

            int charDigit = getSpecificDigit(random, i) + 65;

            at.append((char) charDigit);
        }

        camCode = String.format("CAM %s-%s", at.toString(), String.valueOf(hashtags));

        return camCode;
    }

    private static int getSpecificDigit(int number, int position) {

        return (int) ((number / Math.pow(10, position - 1)) % 10);
    }

}
