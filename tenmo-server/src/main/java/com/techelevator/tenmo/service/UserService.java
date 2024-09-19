package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.exception.UserListServiceException;
import com.techelevator.tenmo.exception.UserServiceException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.SecurityUtils;
import com.techelevator.tenmo.util.BasicLogger;
import com.techelevator.tenmo.util.SQLInjectionCheck;
import com.techelevator.tenmo.util.ValidateId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userDao.getUsers();

            if (users.isEmpty()) {
                BasicLogger.warn("No users found");
            } else {
                BasicLogger.info("User list retrieved");
            }

            return users;
        } catch (DaoException ex) {
            throw new UserListServiceException("Error fetching list of users", ex);
        }
    }

    public User getUserByName(String username) {
        try {
            if (!SQLInjectionCheck.isSafe(username)) {
                throw new UserServiceException("The username had an invalid format: " + username);
            }
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                BasicLogger.warn("User could not be located with username: " + username);
            } else {
                BasicLogger.info("Retrieved user info for user: " + username);
            }
            return user;
        } catch (DaoException ex) {
            throw new UserServiceException("Error fetching user by username", ex);
        }
    }

    public User getUserById(int userId) {
        try {
            ValidateId.validateUserId(userId);

            User user = userDao.getUserById(userId);

            if (user == null) {
                BasicLogger.warn("User could not be located with userId: " + userId);
            } else {
                BasicLogger.info("Retrieved user info: \n" + user.toString());
            }
            return user;
        } catch (IllegalArgumentException ex) {
            throw new UserServiceException("The id input was not valid: " + userId, ex);
        } catch (DaoException ex) {
            throw new UserServiceException("Error fetching user by userId", ex);
        }
    }

    public User getCurrentUser() {
        try {
            String currentUsername = SecurityUtils.getCurrentUsername();
            if (currentUsername == null) {
                BasicLogger.warn("No authenticated user found in security context");
                throw new UserServiceException("Unauthorized access - no authenticated user found");
            }

            User currentUser = userDao.getUserByUsername(currentUsername);
            if (currentUser == null) {
                BasicLogger.warn("User could not be located with the username: " + currentUsername);
                throw new UserServiceException("User could not be located with username: " + currentUsername);
            }

            BasicLogger.info("Retrieved current user info: \n" + currentUser.toString());
            return currentUser;
        } catch (DaoException ex) {
            throw new UserServiceException("Error retrieving the current user");
        }
    }
}
