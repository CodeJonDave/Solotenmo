package com.techelevator.tenmo.handler;

import com.techelevator.tenmo.model.AuthenticatedUser;

public interface Handler {
    void setAuthUser(AuthenticatedUser currentUser);
}
