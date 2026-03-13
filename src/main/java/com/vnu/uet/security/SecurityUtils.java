package com.vnu.uet.security;

import java.util.Optional;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<String> getCurrentUserLogin() {
        return Optional.of("admin");
    }

    public static UserInFoDetails getInfoCurrentUserLogin() {
        UserInFoDetails user = new UserInFoDetails();
        user.setId(1L);
        user.setLogin("admin");
        user.setEmail("admin@localhost");
        user.setOrgIn("/1/1");
        user.setCustId(1L);
        return user;
    }

    public static boolean isAuthenticated() {
        return true;
    }

    public static boolean isCurrentUserInRole(String authority) {
        return true;
    }
}
