package com.feup.acme_cafe.ui.login;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String generateEncryptedPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        String encoded = Base64.getEncoder().encodeToString(hash);

        return encoded;
    }
}
