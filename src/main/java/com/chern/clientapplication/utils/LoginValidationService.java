package com.chern.clientapplication.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LoginValidationService {

    private final static String LOGIN_REGEX = "^[a-zA-Z0-9._-]{3,}$";

    private final Pattern loginPattern = Pattern.compile(LOGIN_REGEX);

    public boolean isValidLogin(String login) {
        if (login == null) {
            return false;
        }
        Matcher loginMatcher = loginPattern.matcher(login);

        return loginMatcher.matches();
    }
}
