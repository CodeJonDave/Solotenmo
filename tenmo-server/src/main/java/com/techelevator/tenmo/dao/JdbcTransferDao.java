package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.TransferResponseMapper;
import com.techelevator.tenmo.util.TransferSQLBase;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcTransferDao implements TransferDao {
    private static final String TRANSFER_SQL = TransferSQLBase.TRANSFER_BASE + "WHERE t.transfer_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public TransferDto getTransferById(int transferId) {
        TransferDto transfer = null;
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(TRANSFER_SQL, transferId);
            if (result.next()) {
                transfer = mapRowToTransferDto(result);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("No transfer found with ID: " + transferId, e);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DaoException("Multiple transfers found with ID: " + transferId, e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while fetching transfer by ID", e);
        }
        return transfer;
    }

    @Override
    public Integer createTransfer(int typeId, int statusId, int accountFrom, int accountTo, BigDecimal amount) {
        Integer transferId = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?,?,?,?,?) RETURNING transfer_id";
        try {
            transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                    typeId, statusId, accountFrom, accountTo, amount);
            if (transferId == null) {
                throw new DaoException("The transfer was not created");
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while creating transfer", e);
        }
        return transferId;
    }

    private TransferDto mapRowToTransferDto(SqlRowSet results) {
        try {
            return TransferResponseMapper.mapRowToResponse(results);
        } catch (Exception e) {
            throw new DaoException("Error occurred while mapping row to TransferDto", e);
        }
    }

}
