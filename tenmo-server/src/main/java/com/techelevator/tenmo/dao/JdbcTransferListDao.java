package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.TransferResponseMapper;
import com.techelevator.tenmo.util.TransferSQLBase;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferListDao implements TransferListDao {
    private static final String PENDING_SQL = TransferSQLBase.TRANSFER_BASE +
            "WHERE a_from.user_id = ? " +
            "AND ts.transfer_status_desc = 'Pending'";

    private static final String HISTORY_SQL = TransferSQLBase.TRANSFER_BASE +
            "WHERE a_from.user_id = ? " +
            "OR a_to.user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<TransferDto> getTransferHistory(int userId) {
        return getTransferList(userId, HISTORY_SQL);
    }


    @Override
    public List<TransferDto> getPendingTransfers(int userId) {
        return getTransferList(userId, PENDING_SQL);
    }

    private List<TransferDto> getTransferList(int userId, String sqlString) {
        List<TransferDto> transfers = new ArrayList<>();
        try {
            BasicLogger.info("Executing SQL: " + sqlString + " with parameter: " + userId);
            SqlRowSet results;

            // Determine if it is a pending query (which only takes one userId) or history query (which takes two)
            if (sqlString.equals(PENDING_SQL)) {
                results = jdbcTemplate.queryForRowSet(sqlString, userId);
                BasicLogger.info(results.toString());// Pass only one parameter for pending
            } else {
                results = jdbcTemplate.queryForRowSet(sqlString, userId, userId);
                BasicLogger.info(results.toString());// Pass two parameters for history
            }
            while (results.next()) {
                TransferDto transferResponseDto = TransferResponseMapper.mapRowToResponse(results);
                BasicLogger.info(transferResponseDto.toString());
                transfers.add(transferResponseDto);
            }
        } catch (CannotGetJdbcConnectionException e) {
            BasicLogger.error("EXCEPTION THROWN IN dao ", e);
            throw new DaoException("Unable to connect to server or database", e);
        } catch (EmptyResultDataAccessException e) {
            BasicLogger.error("EXCEPTION THROWN IN DAO", e);
            throw new DaoException("No users found: ", e);
        } catch (Exception e) {
            BasicLogger.error("EXCEPTION THROWN IN Dao ", e);
            throw new DaoException("An unexpected error occurred while fetching user by ID", e);
        }
        return transfers;
    }

}
