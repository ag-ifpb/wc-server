/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.persistence;

import io.github.victorhsr.pdm.entities.Record;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class RecordDao implements Dao<Record, String> {

    private Connection connection;

    private Connection getConnection() throws SQLException {

        connection = ConnectionProvider.getInstance().getConnection();

        return connection;
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }

    @Override
    public List<Record> getAll() {
        List<Record> records = new ArrayList<>();

        try {

            CallableStatement prepareCall = getConnection().prepareCall("SELECT camcode, camdate, duration, preview, code FROM record");
            ResultSet resultSet = prepareCall.executeQuery();

            while (resultSet.next()) {
                records.add(fillObject(resultSet));
            }

            resultSet.close();
            closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(RecordDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return records;

    }

    public byte[] getVideo(Record record) {
        byte[] result = null;

        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT video FROM record WHERE camcode = ? AND code = ?");
            preparedStatement.setString(1, record.getCamCode());
            preparedStatement.setLong(2, record.getCode());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getBytes("video");
            }

            closeConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @Override
    public Record fillObject(ResultSet rs) throws SQLException {

        Record record = new Record();

        record.setCamCode(rs.getString("camCode"));
        record.setDate(rs.getTimestamp("camDate"));
        record.setDuration(rs.getInt("duration"));
        record.setPreview(rs.getBytes("preview"));
        record.setCode(rs.getLong("code"));
//        record.setVideo(rs.getBytes("video"));

        return record;

    }

    @Override
    public boolean save(Record record) {
        int result = 0;
        try {

            int index = 0;
            PreparedStatement preparedStatement = getConnection().prepareStatement("INSERT INTO record (camcode, camdate, duration, preview, video, code) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(++index, record.getCamCode());
            preparedStatement.setTimestamp(++index, new Timestamp(record.getDate().getTime()));
            preparedStatement.setLong(++index, record.getDuration());
            preparedStatement.setBytes(++index, record.getPreview());
            preparedStatement.setBytes(++index, record.getVideo());
            preparedStatement.setLong(++index, record.getCode());

            result = preparedStatement.executeUpdate();

            closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(RecordDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result != 0;
    }

    @Override
    public boolean delete(Record record) {
        int result = 0;
        try {

            int index = 0;
            PreparedStatement preparedStatement = getConnection().prepareStatement("DELETE FROM record WHERE camcode = ? AND code = ?");
            preparedStatement.setString(++index, record.getCamCode());
            preparedStatement.setLong(++index, record.getCode());

            result = preparedStatement.executeUpdate();

            closeConnection();

        } catch (SQLException ex) {
            Logger.getLogger(RecordDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result != 0;
    }

    @Override
    public boolean update(Record objeto) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Record findById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Record> findByAttributes(Map<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
