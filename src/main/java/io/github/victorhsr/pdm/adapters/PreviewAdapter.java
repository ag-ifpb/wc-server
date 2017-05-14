/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.adapters;

import java.util.Base64;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class PreviewAdapter extends XmlAdapter<String, byte[]> {

    @Override
    public byte[] unmarshal(String v) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeBytes = decoder.decode(v);

        return decodeBytes;
    }

    @Override
    public String marshal(byte[] v) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(v);

        return encodedString;

    }

}
