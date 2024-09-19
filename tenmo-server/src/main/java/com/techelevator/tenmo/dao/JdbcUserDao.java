package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                user = mapRowToUser(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("No user found with ID: " + userId, e);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DaoException("Multiple users found with ID: " + userId, e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while fetching user by ID", e);
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                User user = mapRowToUser(results);
                users.add(user);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("No users found: ", e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while fetching user by ID", e);
        }
        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        User user = null;
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username = LOWER(TRIM(?));";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
            if (rowSet.next()) {
                user = mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new DaoException("Multiple users found with username: " + username, e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while fetching user by username", e);
        }
        return user;
    }

    @Override
    public User createUser(RegisterUserDto user) {
        User newUser = null;
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (LOWER(TRIM(?)), ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(user.getPassword());
        try {
            int newUserId = jdbcTemplate.queryForObject(sql, int.class, user.getUsername(), password_hash);
            newUser = getUserById(newUserId);
            if (newUser != null) {
                // create account
                sql = "INSERT INTO account (user_id, balance) VALUES (?, ?)";
                jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
            }
        } catch (NullPointerException ex) {
            throw new DaoException("Null pointer exception occurred", ex);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } catch (JdbcUpdateAffectedIncorrectNumberOfRowsException e) {
            throw new DaoException("Unexpected number of rows affected when creating user", e);
        } catch (Exception e) {
            throw new DaoException("An unexpected error occurred while creating user", e);
        }
        return newUser;
    }


    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
