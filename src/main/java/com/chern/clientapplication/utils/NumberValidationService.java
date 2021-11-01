package com.chern.clientapplication.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NumberValidationService {

    private final static String INTEGER_REGEX = "[0-9]{1,4}";
    private final static String DOUBLE_REGEX = "^[0-9]{1,5}+.[0-9]{2}+$";

    private final Pattern integerPattern = Pattern.compile(INTEGER_REGEX);
    private final Pattern doublePattern = Pattern.compile(DOUBLE_REGEX);

    public boolean isValidInteger(Integer number){
            if (number == null) {
                return false;
            }
            Matcher loginMatcher = integerPattern.matcher(String.valueOf(number));

            return loginMatcher.matches();

    }

    public boolean isValidInteger(String number){
        if (number == "") {
            return false;
        }
        Matcher loginMatcher = integerPattern.matcher(number);

        return loginMatcher.matches();

    }
    public boolean isValidDouble(String number){
        if (number == "") {
            return false;
        }
        Matcher loginMatcher = doublePattern.matcher(number);

        return loginMatcher.matches();

    }
}
