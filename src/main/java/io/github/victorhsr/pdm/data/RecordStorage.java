/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.data;

import io.github.victorhsr.pdm.entities.Record;
import io.github.victorhsr.pdm.persistence.RecordDao;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Queue;
import javax.imageio.ImageIO;
import org.jcodec.api.awt.SequenceEncoder;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class RecordStorage {

    private RecordDao dao = new RecordDao();

    public void saveVideo(Queue<byte[]> frameBuffer, String camCode) throws IOException {

        String id = String.valueOf(System.currentTimeMillis());

        int durarion = 0;
        durarion = frameBuffer.size();
        System.out.println("total de frames = " + durarion);
        durarion /= 15;

        String src = "http://192.168.2.106:8080/webcam-server/show?camCode=" + camCode + id + ".mp4";
        src = src.replace(" ", "%20");

        Record record = new Record();
        record.setCamCode(camCode);
        record.setDate(new Timestamp(System.currentTimeMillis()));
        record.setUri(URI.create(src));
        record.setDuration(durarion);
        record.setCode(System.currentTimeMillis());

        URL resource = RecordStorage.class.getClassLoader().getResource("");
        String path = resource.getPath() + record.getCamCode() + record.getCode();
        path = path.replace("%20", " ");

        File file = new File(path);

        BufferedImage buffFrame;
        ByteArrayInputStream byteArrayInputStream;

        SequenceEncoder encoder = new SequenceEncoder(file);
        

        int previewPosition = frameBuffer.size() / 2;
        int previewCount = 0;

        while (!frameBuffer.isEmpty()) {

            ++previewCount;

            byte[] frame = frameBuffer.poll();

            if (previewCount == previewPosition) {
                record.setPreview(frame);
            }

            byteArrayInputStream = new ByteArrayInputStream(frame);
            buffFrame = ImageIO.read(byteArrayInputStream);
            encoder.encodeImage(buffFrame);
        }

        encoder.finish();

        FileInputStream fis = new FileInputStream(path);

        byte[] bytes = new byte[fis.available()];

        fis.read(bytes);
        fis.close();

        record.setVideo(bytes);

        dao.save(record);
        file.delete();
    }

}
