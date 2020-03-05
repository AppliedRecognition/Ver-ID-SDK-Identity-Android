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
 * @since 1.0.0
 * @deprecated 1.1.0
 * @see VerIDIdentity
 */
@SuppressWarnings("WeakerAccess")
public final class VerIDSDKIdentity {

    private final VerIDIdentity verIDIdentity;

    /**
     * Default algorithm used when creating digital signatures
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity#DEFAULT_SIGNATURE_ALGORITHM
     */
    public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * Constructor
     * @param inputStream Input stream from which to read the contents of a P12 file containing the identity
     * @param password Password to unlock the p12 content
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity(InputStream, String)
     */
    public VerIDSDKIdentity(@NonNull InputStream inputStream, @NonNull String password) throws Exception {
        verIDIdentity = new VerIDIdentity(inputStream, password);
    }

    /**
     * Constructor
     * @param context Application context that contains an asset named "Ver-ID SDK identity.p12" and a manifest meta data entry named "com.appliedrec.verid.password"
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity(Context)
     */
    public VerIDSDKIdentity(@NonNull Context context) throws Exception {
        verIDIdentity = new VerIDIdentity(context);
    }

    /**
     * Constructor
     * @param context Application context that contains an asset named "Ver-ID SDK identity.p12"
     * @param password Password to unlock the p12 asset
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity(Context, String)
     */
    public VerIDSDKIdentity(@NonNull Context context, @NonNull String password) throws Exception {
        verIDIdentity = new VerIDIdentity(context, password);
    }

    /**
     * Constructor
     * @param p12URL HTTP or HTTPS URL of p12 file containing the digital certificate and private key used to construct the Ver-ID SDK identity
     * @param password Password to unlock the p12 file
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity(URL, String)
     */
    public VerIDSDKIdentity(@NonNull URL p12URL, @NonNull String password) throws Exception {
        verIDIdentity = new VerIDIdentity(p12URL, password);
    }

    /**
     * Constructor
     * @param p12File P12 file containing the digital certificate and private key used to construct the Ver-ID SDK identity
     * @param password Password to unlock the p12 file
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity(File, String)
     */
    public VerIDSDKIdentity(@NonNull File p12File, @NonNull String password) throws Exception {
        verIDIdentity = new VerIDIdentity(p12File, password);
    }

    /**
     * @return Common name from the identity's digital certificate
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity#getCommonName()
     */
    public String getCommonName() {
        return verIDIdentity.getCommonName();
    }

    /**
     * @return Digital certificate associated with this identity
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity#getCertificate()
     */
    public X509Certificate getCertificate() {
        return verIDIdentity.getCertificate();
    }

    /**
     * Sign a message
     * @param message Message to sign
     * @return Signature
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity#sign(byte[])
     */
    public byte[] sign(@NonNull byte[] message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verIDIdentity.sign(message, VerIDIdentity.DEFAULT_SIGNATURE_ALGORITHM);
    }

    /**
     * Sign a message using a specified algorithm
     * @param message Message to sign
     * @param algorithm Algorithm to use when generating the signature
     * @return Signature
     * @since 1.0.0
     * @deprecated 1.1.0
     * @see VerIDIdentity#sign(byte[], String)
     */
    public byte[] sign(@NonNull byte[] message, @NonNull String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return verIDIdentity.sign(message, algorithm);
    }
}