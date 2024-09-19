package com.rakbank.userservice.service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordEncoder {

    private BCrypt.Hasher hasher;

    public PasswordEncoder(BCrypt.Version version) {
        hasher =  BCrypt.with(version);
    }

    public String encode(String password) {
        return hasher.hashToString(5, password.toCharArray());
    }

}
