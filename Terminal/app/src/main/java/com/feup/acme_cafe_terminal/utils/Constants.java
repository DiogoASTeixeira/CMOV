package com.feup.acme_cafe_terminal.utils;

public class Constants {
    public static final int KEY_SIZE = 512;
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final String KEY_ALGO = "RSA";                    // cryptography family
    public static final String SIGN_ALGO = "SHA256WithRSA";         // signature algorithm
    public static final int CERT_SERIAL = 12121212;                 // certificate serial number (any one does the job)
    public static final String ENC_ALGO = "RSA/ECB/PKCS1Padding";   // encrypt/decrypt algorithm
    public static final String keyname = "myIdKey";                 // common name in the KeyStore and public key certificate
    public static final String ip_address = "192.168.0.104";         // main server address
}
