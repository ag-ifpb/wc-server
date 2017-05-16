/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.controllers;

import io.github.victorhsr.pdm.entities.Record;
import io.github.victorhsr.pdm.persistence.RecordDao;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@WebServlet(urlPatterns = "/show")
public class RecordController extends HttpServlet {

    private final RecordDao dao = new RecordDao();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) {

        try {

            String camCode = req.getParameter("camcode");
            String code = req.getParameter("code");

            Record record = new Record();
            record.setCode(Long.parseLong(code));
            record.setCamCode(camCode);

            byte[] video = dao.getVideo(record);

            String rangeHeader = req.getHeader("range");

            int offset = 0;
            int endset = video.length - 1;

            ServletOutputStream output = res.getOutputStream();

            if (rangeHeader != null) {

                rangeHeader = rangeHeader.replace("bytes=", "");

                String[] ranges;
                ranges = rangeHeader.split("-");

                offset = Integer.valueOf(ranges[0]);

                if (ranges.length > 1) {
                    endset = Integer.valueOf(ranges[1]);
                }

                String contentRange = new StringBuffer()
                        .append("bytes ")
                        .append(offset)
                        .append("-")
                        .append(endset)
                        .append("/")
                        .append(video.length)
                        .toString();

                res.setStatus(206);
                res.setHeader("Content-Range", contentRange);

            } else {
                res.setStatus(200);
            }

            res.setHeader("Content-Length", String.valueOf(endset - offset + 1));
            res.setHeader("Content-Type", "video/mp4");
            res.setHeader("Accept-Ranges", "bytes");

            output.write(video, offset, endset - offset + 1);
            output.flush();
            output.close();

        } catch (Exception ex) {
            System.out.printf("[RecordController] - %s\n", ex.getMessage());
        }

    }

}
