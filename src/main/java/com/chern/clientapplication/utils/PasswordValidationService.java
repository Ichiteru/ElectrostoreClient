package com.chern.clientapplication.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordValidationService {

    private final static String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";

    private final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);

    public boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
    }
}
