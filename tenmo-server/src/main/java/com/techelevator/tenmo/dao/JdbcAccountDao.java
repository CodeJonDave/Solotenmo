package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No account ID found for user ID: " + userId, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving account ID for user ID: " + userId, ex);
        } catch (Exception ex) {
            throw new DaoException("An unexpected error occurred while retrieving account ID for user ID: " + userId, ex);
        }
    }

    @Override
    public Integer getAccountIdByUsername(String username) {
        String sql = "SELECT a.account_id " +
                "FROM account a" +
                "JOIN tenmo_user u ON a.user_id = u.user_id " +
                "WHERE u.username = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (EmptyResultDataAccessException ex) {
            throw new DaoException("No account ID found for username: " + username, ex);
        } catch (CannotGetJdbcConnectionException ex) {
            throw new DaoException("Unable to connect to the database", ex);
        } catch (DataAccessException ex) {
            throw new DaoException("Data access error while retrieving account ID for username: " + username, ex);
        } catch (Exception ex) {
            throw new DaoException("An unexpected error occurred while retrieving account ID for username: " + username, ex);
        }
    }
}
