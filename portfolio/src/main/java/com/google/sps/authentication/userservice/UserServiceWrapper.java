package com.google.sps.authentication.userservice;

import com.google.appengine.api.users.UserService;

/** Wrapper that handles the interaction with User Service API. */
public final class UserServiceWrapper {
    private final UserService userService;

    public UserServiceWrapper(UserService userService) {
        this.userService = userService;
    }

    public boolean isUserLoggedIn() {
        return this.userService.isUserLoggedIn();
    }

    public String getUserEmail() {
        return this.userService.getCurrentUser().getEmail();
    }

    public String createLogoutURL(String urlToRedirectToAfterUserLogsOut) {
        return this.userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
    }

    public String createLoginURL(String urlToRedirectToAfterUserLogsIn) {
        return this.userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
    }
}
