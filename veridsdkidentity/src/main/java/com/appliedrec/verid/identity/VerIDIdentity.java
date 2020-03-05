package com.appliedrec.verid.identity;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

/**
 * Represents an identity of a client using Ver-ID SDK
 * @since 1.1.0
 */
@SuppressWarnings("WeakerAccess")
public final class VerIDIdentity {

    private PrivateKey privateKey;
    private X509Certificate certificate;
    private String commonName;

    /**
     * Default algorithm used when creating digital signatures
     * @since 1.1.0
     */
    public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * Constructor
     * @param inputStream Input stream from which to read the contents of a P12 file containing the identity
     * @param password Password to unlock the p12 content
     * @since 1.1.0
     */
    public VerIDIdentity(@NonNull InputStream inputStream, @NonNull String password) throws Exception {
        init(inputStream, password);
    }

    /**
     * Constructor
     * @param context Application context that contains an asset named "Ver-ID SDK identity.p12" and a manifest meta data entry named "com.appliedrec.verid.password"
     * @since 1.1.0
     */
    public VerIDIdentity(@NonNull Context context) throws Exception {
        String password = context.getApplicationContext().getApplicationInfo().metaData.getString("com.appliedrec.verid.password");
        if (password == null) {
            throw new Exception();
        }
        init(context, password);
    }

    /**
     * Constructor
     * @param context Application context that contains an asset named "Ver-ID SDK identity.p12"
     * @param password Password to unlock the p12 asset
     * @since 1.1.0
     */
    public VerIDIdentity(@NonNull Context context, @NonNull String password) throws Exception {
        init(context, password);
    }

    /**
     * Constructor
     * @param p12URL HTTP or HTTPS URL of p12 file containing the digital certificate and private key used to construct the Ver-ID SDK identity
     * @param password Password to unlock the p12 file
     * @since 1.1.0
     */
    public VerIDIdentity(@NonNull URL p12URL, @NonNull String password) throws Exception {
        if ("https".equalsIgnoreCase(p12URL.getProtocol()) || "http".equalsIgnoreCase(p12URL.getProtocol())) {
            HttpURLConnection connection = (HttpURLConnection) p12URL.openConnection();
            connection.setDoInput(true);
            if (connection.getResponseCode() < 400) {
                init(connection.getInputStream(), password);
                return;
            }
        }
        throw new Exception();
    }

    /**
     * Constructor
     * @param p12File P12 file containing the digital certificate and private key used to construct the Ver-ID SDK identity
     * @param password Password to unlock the p12 file
     * @since 1.1.0
     */
    public VerIDIdentity(@NonNull File p12File, @NonNull String password) throws Exception {
        FileInputStream inputStream = new FileInputStream(p12File);
        init(inputStream, password);
    }

    /**
     * @return Common name from the identity's digital certificate
     * @since 1.1.0
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * @return Digital certificate associated with this identity
     * @since 1.1.0
     */
    public X509Certificate getCertificate() {
        return certificate;
    }

    /**
     * Sign a message
     * @param message Message to sign
     * @return Signature
     * @since 1.1.0
     */
    public byte[] sign(@NonNull byte[] message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(message, DEFAULT_SIGNATURE_ALGORITHM);
    }

    /**
     * Sign a message using a specified algorithm
     * @param message Message to sign
     * @param algorithm Algorithm to use when generating the signature
     * @return Signature
     * @since 1.1.0
     */
    public byte[] sign(@NonNull byte[] message, @NonNull String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    private void init(@NonNull Context context, @NonNull String password) throws Exception {
        InputStream inputStream = context.getApplicationContext().getAssets().open("Ver-ID identity.p12");
        init(inputStream, password);
    }

    private void init(@NonNull InputStream inputStream, @NonNull String password) throws Exception {
        KeyStore p12KeyStore = KeyStore.getInstance("pkcs12");
        p12KeyStore.load(inputStream, password.toCharArray());
        Enumeration<String> enumeration = p12KeyStore.aliases();
        if (enumeration.hasMoreElements()) {
            String alias = enumeration.nextElement();
            certificate = (X509Certificate) p12KeyStore.getCertificate(alias);
            privateKey = (PrivateKey) p12KeyStore.getKey(alias, password.toCharArray());
            commonName = new CertificateUtil(certificate).getCommonName();
        } else {
            throw new Exception();
        }
    }
}