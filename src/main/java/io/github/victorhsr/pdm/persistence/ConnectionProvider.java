/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.persistence;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public class ConnectionProvider {

    private static ConnectionProvider connectionProvider;

    private BasicDataSource connectionPool;

    private ConnectionProvider() {
    }

    public static synchronized ConnectionProvider getInstance() {

        if (connectionProvider == null) {
            connectionProvider = new ConnectionProvider();
        }

        return connectionProvider;
    }

    public Connection getConnection() throws SQLException {

        if (connectionPool == null || connectionPool.isClosed()) {

            connectionPool = new BasicDataSource();
            connectionPool.setDriverClassName("org.postgresql.Driver");

            try {

                URI dbUri = new URI(System.getenv("DATABASE_URL"));
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];

                StringBuilder dbUrl = new StringBuilder();
                dbUrl.append("jdbc:postgresql://").
                        append(dbUri.getHost()).
                        append(':').
                        append(dbUri.getPort()).
                        append(dbUri.getPath());

                connectionPool.setUsername(username);
                connectionPool.setPassword(password);
                connectionPool.setUrl(dbUrl.toString());

            } catch (Exception ex) {
                connectionPool.setUsername("postgres");
                connectionPool.setPassword("123456");
                connectionPool.setUrl("jdbc:postgresql://localhost:5432/webcam");
            }

        }

        return connectionPool.getConnection();
    }

}
