package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.dao.*;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcBalanceDao implements BalanceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBalanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalanceById(int userId) {
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No balance found for user with Id: " + userId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving balance", ex);
        }
    }

    @Override
    public int incrementBalanceById(int userId, BigDecimal amount) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?";
        try {
            return jdbcTemplate.update(sql, amount, userId);
        }catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No balance found for user with Id: " + userId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataIntegrityViolationException ex) {
            throw new DaoException("Data integrity violation while updating balance", ex);
        } catch (InvalidDataAccessApiUsageException ex) {
            throw new DaoException("Invalid data access API usage", ex);
        } catch (TypeMismatchDataAccessException ex) {
            throw new DaoException("Type mismatch error during data access", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while updating balance", ex);
        }
    }

    @Override
    public int decrementBalanceById(int userId, BigDecimal amount) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?";
        try {
            return jdbcTemplate.update(sql, amount, userId);
        }catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No balance found for user with Id: " + userId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataIntegrityViolationException ex) {
            throw new DaoException("Data integrity violation while updating balance", ex);
        } catch (InvalidDataAccessApiUsageException ex) {
            throw new DaoException("Invalid data access API usage", ex);
        } catch (TypeMismatchDataAccessException ex) {
            throw new DaoException("Type mismatch error during data access", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while updating balance", ex);
        }
    }
}
