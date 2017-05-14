/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.entities;

import io.github.victorhsr.pdm.adapters.PreviewAdapter;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@XmlRootElement
public class Record {

    private String camCode;
    private Date date;
    private int duration;
    @XmlJavaTypeAdapter(PreviewAdapter.class)
    private byte[] preview;
    @XmlTransient
    private byte[] video;
    private URI uri;
    private long code;

    public Record() {
    }

    public Record(String camCode, Date date, int duration, byte[] preview, byte[] video, URI uri, long code) {
        this.camCode = camCode;
        this.date = date;
        this.duration = duration;
        this.preview = preview;
        this.video = video;
        this.uri = uri;
        this.code = code;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getCamCode() {
        return camCode;
    }

    public void setCamCode(String camCode) {
        this.camCode = camCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public byte[] getVideo() {
        return video;
    }

    public void setVideo(byte[] video) {
        this.video = video;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Record{" + "camCode=" + camCode + ", date=" + date + ", duration=" + duration + ", uri=" + uri + ", code=" + code + '}';
    }

}
