package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.util.BasicLogger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int updateTransferStatus(int transferId, int transferStatusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        try {
            return jdbcTemplate.update(sql, transferStatusId, transferId);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while updating transfer status", ex);
        }
    }

    @Override
    public Integer getStatusIdByDesc(String description) {
        Integer statusId = null;
        String sql = "SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?";
        try {
            statusId = jdbcTemplate.queryForObject(sql, Integer.class, description);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No id found for status with description: " + description, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving status description", ex);
        }
        return statusId;
    }

    @Override
    public String getStatusDescById(int statusId) {
        String description = "";
        String sql = "SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?";
        try {
            description = jdbcTemplate.queryForObject(sql, String.class, statusId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No description found for status with Id: " + statusId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving status description", ex);
        }
        return description;
    }
}

