package it.smartcommunitylab.smartchainbackend.service;

import org.apache.commons.lang3.StringUtils;

public class Validator {

    private static final String defaultMessage = "Field cannot be empty";

    public static boolean throwIfInvalid(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(message != null ? message : defaultMessage);
        }
        return true;
    }
}
