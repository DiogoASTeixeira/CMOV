package com.feup.acme_cafe.ui.client;

class Constants {
    static final int KEY_SIZE = 512;
    static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    static final String KEY_ALGO = "RSA";                    // cryptography family
    static final String SIGN_ALGO = "SHA256WithRSA";         // signature algorithm
    static final int CERT_SERIAL = 12121212;                 // certificate serial number (any one does the job)
    static final String ENC_ALGO = "RSA/ECB/PKCS1Padding";   // encrypt/decrypt algorithm
    static final String keyname = "myIdKey";                     // common name in the KeyStore and public key certificate
}
