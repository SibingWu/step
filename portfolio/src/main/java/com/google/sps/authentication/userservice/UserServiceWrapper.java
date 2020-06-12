package com.google.sps.authentication.userservice;

import com.google.appengine.api.users.UserService;

/** Wrapper that handles the interaction with User Service API. */
public final class UserServiceWrapper {
    private final UserService userService;

    public UserServiceWrapper(UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks if the user is logged in.
     * @return The status if the user is logged in.
     */
    public boolean isUserLoggedIn() {
        return this.userService.isUserLoggedIn();
    }

    /**
     * Gets the email that the user uses to log in.
     * @return The email used to log in.
     */
    public String getUserEmail() {
        return this.userService.getCurrentUser().getEmail();
    }

    /**
     * Gets the url for logging out.
     * @param urlToRedirectToAfterUserLogsOut Url where the user will be redirected to after logging out.
     * @return Url for logging out.
     */
    public String createLogoutURL(String urlToRedirectToAfterUserLogsOut) {
        return this.userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
    }

    /**
     * Gets the url for logging in.
     * @param urlToRedirectToAfterUserLogsIn Url where the user will be redirected to after logging in.
     * @return Url for logging out.
     */
    public String createLoginURL(String urlToRedirectToAfterUserLogsIn) {
        return this.userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
    }
}
