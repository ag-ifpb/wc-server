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

    /**
     * Find a previously registered Cam from its camCode
     *
     * @param camCode The camCode of the Cam
     * @return The Cam that you are looking for
     */
    public static Cam findRegisteredCam(String camCode) {

        List<Cam> collect = CAM_CLIENTS.stream().filter(camClient -> camClient.getCamCode().equals(camCode)).collect(Collectors.toList());

        return collect.isEmpty() ? null : collect.get(0);
    }

    /**
     * Generates a camCode and create a Cam and then, store it in memory
     *
     * @param camSocket The socket that requested a connection to the server
     * @throws IOException
     */
    public static void addCamClient(Socket camSocket) throws IOException {

        String camCode = generateCamCode();

        while (existsCamcode(camCode)) {

            camCode = generateCamCode();
        }

        Cam camClient = new Cam(camSocket, camCode);

        CAM_CLIENTS.add(camClient);

        new ConnectionHandler(camClient).start();
    }

    /**
     * Remove a Cam from the memory
     *
     * @param camClient
     */
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

    /**
     * Verify if the camCode is already registered in memory
     *
     * @param camCode The camCode that will be verified
     * @return
     */
    private static boolean existsCamcode(String camCode) {

        return CAM_CLIENTS.stream().anyMatch(camClient -> camClient.getCamCode().equals(camCode));
    }

    /**
     * Generate a randomized camCode following the pattern 'CAM @@@@@@-##', were
     * '@' represents [A-Z] and '#' represents [0-9]
     *
     * @return The generated camCode
     */
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

    /**
     * Get specific digit of an integer number
     *
     * @param number The number that will be analyzed
     * @param position The position of the number that will be returned
     * @return
     */
    private static int getSpecificDigit(int number, int position) {

        return (int) ((number / Math.pow(10, position - 1)) % 10);
    }

}
