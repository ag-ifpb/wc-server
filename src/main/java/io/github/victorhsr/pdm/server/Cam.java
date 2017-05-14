/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.server;

import java.net.Socket;
import java.util.Objects;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class Cam {

    private final Socket camSocket;
    private final String camCode;

    public Cam(Socket camSocket, String camCode) {
        this.camSocket = camSocket;
        this.camCode = camCode;
    }

    public Socket getCamSocket() {
        return camSocket;
    }

    public String getCamCode() {
        return camCode;
    }

    public boolean isConnected() {
        return !camSocket.isClosed();
    }

    @Override
    public int hashCode() {

        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.camCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cam other = (Cam) obj;

        return Objects.equals(this.camCode, other.camCode);
    }

}
