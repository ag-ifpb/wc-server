/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.resources;

import io.github.victorhsr.pdm.entities.Record;
import io.github.victorhsr.pdm.persistence.RecordDao;
import java.net.URI;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.PathParam;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Path("/records")
public class Records {

    private RecordDao dao;

    @PostConstruct
    private void init() {
        dao = new RecordDao();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Record> records = dao.getAll();

        records.forEach(record -> {
            String src = "http://192.168.2.106:8080/show?camcode=" + record.getCamCode() + "&code=" + record.getCode();
            src = src.replace(" ", "%20");
            record.setUri(URI.create(src));
        });

        GenericEntity<List<Record>> entityResponse = new GenericEntity<List<Record>>(records) {
        };

        return Response.ok().entity(entityResponse).build();
    }

    @DELETE
    @Path("/{camcode}/{code}")
    public Response deleteRecord(@PathParam("camcode") String camCode, @PathParam("code") String code) {

        Record record = new Record();
        record.setCamCode(camCode);
        record.setCode(Long.parseLong(code));

        if (dao.delete(record)) {
            return Response.ok().build();
        }

        return Response.status(Status.NOT_FOUND).build();
    }

}
