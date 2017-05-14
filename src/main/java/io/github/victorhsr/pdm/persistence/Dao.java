/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pdm.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 * @param <T> Entity type
 * @param <I> Id type
 */
public interface Dao<T, I> {

    boolean save(T objeto);

    boolean delete(T objeto);

    boolean update(T objeto);

    T findById(I id);

    List<T> findByAttributes(Map<String, String> map);

    List<T> getAll();

    T fillObject(ResultSet rs) throws SQLException;

}
