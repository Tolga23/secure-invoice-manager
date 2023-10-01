package com.thardal.secureinvoicemanager.base.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import static com.twilio.rest.api.v2010.account.Message.creator;

public class SmsUtils {

    public static String FROM_NUMBER;
    public static String SID_KEY;
    public static String TOKEN_KEY;

    public static void sendSMS(String to, String messageBody) {
        Twilio.init(SID_KEY, TOKEN_KEY);
        Message message = creator(new PhoneNumber(to), new PhoneNumber(FROM_NUMBER), messageBody).create();
        System.out.println(message);
    }

    public static void setFromNumber(final String fromNumber) {
        FROM_NUMBER = fromNumber;
    }

    public static void setSidKey(final String sidKey) {
        SID_KEY = sidKey;
    }

    public static void setTokenKey(final String tokenKey) {
        TOKEN_KEY = tokenKey;
    }
}
