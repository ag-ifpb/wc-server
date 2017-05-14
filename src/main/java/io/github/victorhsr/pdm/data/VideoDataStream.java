/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.data;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class VideoDataStream {

    private final Queue<byte[]> frameBuffer;
    private final boolean useBuffer;

    /**
     * Create a VideoDataStream object
     *
     * @param useBuffer
     * <ul>
     * <li>True if you want to maintain a buffer</li>
     * <li>False if you don't want to maintain a buffer</li>
     * </ul>
     */
    public VideoDataStream(boolean useBuffer) {
        this.useBuffer = useBuffer;

        frameBuffer = useBuffer ? new LinkedList<>() : null;
    }

    /**
     * Processes frames received by an InputStream and store it in a buffer
     *
     * @param in The stream that provides the data
     * @return The processed frame
     * @throws java.io.IOException
     */
    public byte[] processFrame(InputStream in) throws IOException {

        DataInputStream dis = new DataInputStream(in);

        int frameLength;

        byte[] frame;

        frameLength = dis.readInt();

        frame = new byte[frameLength];

        int len = 0;

        while (len < frameLength) {
            len += dis.read(frame, len, frameLength - len);
        }

        if (useBuffer) {
            frameBuffer.offer(frame);
        }

        return frame;

    }

    /**
     * Clean the buffer, if it exists
     */
    public void cleanBuffer() {
        if (frameBuffer != null) {
            frameBuffer.clear();
        }
    }

    /**
     *
     * @return
     * <ul>
     * <li>Queue<byte[]> - if exists a buffer</li>
     * <li>Null - if doesn't have a buffer</li>
     * </ul>
     */
    public Queue<byte[]> getBuffer() {
        return frameBuffer;
    }

}
