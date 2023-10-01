package com.thardal.secureinvoicemanager.base.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.secureinvoice.sms")
public class SmsConfig {

    private String fromNumber;
    private String sidKey;
    private String tokenKey;

    @PostConstruct
    public void init() {
        SmsUtils.setFromNumber(fromNumber);
        SmsUtils.setSidKey(sidKey);
        SmsUtils.setTokenKey(tokenKey);
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getSidKey() {
        return sidKey;
    }

    public void setSidKey(String sidKey) {
        this.sidKey = sidKey;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
