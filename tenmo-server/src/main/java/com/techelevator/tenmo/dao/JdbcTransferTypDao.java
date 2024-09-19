package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypDao implements TransferTypeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int getTransferTypeByDesc(String desc) {
        Integer statusId = null;
        String sql = "SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = ?";
        try {
            statusId = jdbcTemplate.queryForObject(sql, Integer.class, desc);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No id found for status with description: " + desc, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving status description", ex);
        }
        return statusId;
    }

    @Override
    public String getTypeDescById(int typeId) {
        String description = "";
        String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?";
        try {
            description = jdbcTemplate.queryForObject(sql, String.class, typeId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No description found for status with Id: " + typeId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving status description", ex);
        }
        return description;
    }
}
