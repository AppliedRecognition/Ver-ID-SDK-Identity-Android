package com.appliedrec.verid.identity;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for simplifying common digital certificate tasks
 * @since 1.0.0
 */
@SuppressWarnings({"WeakerAccess"})
public class CertificateUtil {

    private final X509Certificate certificate;

    /**
     * Constructor
     *
     * @param certificate Certificate
     * @since 1.0.0
     */
    public CertificateUtil(@NonNull X509Certificate certificate) {
        this.certificate = certificate;
    }

    /**
     * Get common name (CN) from the certificate's subject
     *
     * @return Common name
     * @since 1.0.0
     */
    @NonNull
    public String getCommonName() throws Exception {
        Pattern pattern = Pattern.compile("CN=([^,]+)");
        Matcher matcher = pattern.matcher(certificate.getSubjectX500Principal().getName());
        if (matcher.find()) {
            String commonName = matcher.group(1);
            if (commonName != null) {
                return commonName;
            }
        }
        throw new Exception();
    }

    /**
     * Get the certificate's SHA256 fingerprint
     *
     * @return Fingerprint
     * @throws NoSuchAlgorithmException No such algorithm exception
     * @throws CertificateEncodingException Certificate encoding exception
     * @since 1.0.0
     */
    @NonNull
    public byte[] getFingerprint() throws NoSuchAlgorithmException, CertificateEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA256");
        return md.digest(certificate.getEncoded());
    }

    /**
     * Get issuer certificate from a pool of candidate certificates
     *
     * @param candidates Candidate certificates
     * @return Certificate that issued this certificate or {@literal null} if none of the candidates in the pool are the issuer
     * @since 1.0.0
     */
    @Nullable
    public X509Certificate getIssuerCertificate(@NonNull X509Certificate[] candidates) {
        return CertificateUtil.getIssuerCertificate(certificate, candidates);
    }

    /**
     * Get certificate chain for this certificate from a pool of certificates
     *
     * @param certificates Pool of certificates from which to construct the chain
     * @return List with the certificate chain
     * @since 1.0.0
     */
    @NonNull
    public ArrayList<X509Certificate> getChain(@NonNull X509Certificate[] certificates) {
        return CertificateUtil.getChainForCertificate(certificate, certificates);
    }

    /**
     * Get leaf certificates (ones that don't have any children) from a pool of certificates
     *
     * @param certificates Certificate pool
     * @return Leaf certificates
     * @since 1.0.0
     */
    @NonNull
    public static X509Certificate[] getLeafCertsInChain(@NonNull X509Certificate[] certificates) {
        HashSet<X509Certificate> certSet = new HashSet<>();
        for (X509Certificate certificate : certificates) {
            String subject = certificate.getSubjectX500Principal().getName();
            boolean isLeaf = true;
            for (X509Certificate cert : certificates) {
                if (cert.getIssuerX500Principal().getName().equals(subject)) {
                    isLeaf = false;
                    break;
                }
            }
            if (isLeaf) {
                certSet.add(certificate);
            }
        }
        X509Certificate[] certArray = new X509Certificate[certSet.size()];
        certSet.toArray(certArray);
        return certArray;
    }

    /**
     * Get issuer certificate from a pool of candidates
     *
     * @param certificate The certificate for which to find the issuer
     * @param certificates Possible issuer certificates
     * @return The issuer certificate or {@literal null} if the issuer is not among the candidates
     * @since 1.0.0
     */
    @Nullable
    public static X509Certificate getIssuerCertificate(@NonNull X509Certificate certificate, @NonNull X509Certificate[] certificates) {
        if (certificate.getSubjectX500Principal().getName().equals(certificate.getIssuerX500Principal().getName())) {
            return null;
        }
        for (X509Certificate cert : certificates) {
            if (cert.getSubjectX500Principal().getName().equals(certificate.getSubjectX500Principal().getName())) {
                continue;
            }
            if (certificate.getIssuerX500Principal().getName().equals(cert.getSubjectX500Principal().getName())) {
                return cert;
            }
        }
        return null;
    }

    /**
     * Get certificate chain
     *
     * @param certificate The certificate whose chain to find
     * @param certificates Certificates from which to construct the chain
     * @return Certificate chain
     * @since 1.0.0
     */
    @NonNull
    public static ArrayList<X509Certificate> getChainForCertificate(@NonNull X509Certificate certificate, @NonNull X509Certificate[] certificates) {
        ArrayList<X509Certificate> chain = new ArrayList<>();
        chain.add(certificate);
        X509Certificate issuer = getIssuerCertificate(certificate, certificates);
        while (issuer != null) {
            chain.add(issuer);
            issuer = getIssuerCertificate(issuer, certificates);
        }
        return chain;
    }

    /**
     * Get certificate chains from a pool of certificates
     *
     * @param certificates The certificates from which to construct the chains
     * @return Array of certificate chains
     * @since 1.0.0
     */
    @NonNull
    public static ArrayList<X509Certificate>[] getChains(@NonNull X509Certificate[] certificates) {
        X509Certificate[] leafCerts = getLeafCertsInChain(certificates);
        @SuppressWarnings("unchecked")
        ArrayList<X509Certificate>[] chains = new ArrayList[leafCerts.length];
        int i=0;
        for (X509Certificate leaf : leafCerts) {
            chains[i++] = getChainForCertificate(leaf, certificates);
        }
        return chains;
    }

    /**
     * Extract digital certificates from a PEM-encoded string
     *
     * @param pemString PEM string
     * @return Certificates
     * @throws IOException I/O exception
     * @throws CertificateException Certificate exception
     * @since 1.0.0
     */
    @NonNull
    public static X509Certificate[] certificatesFromPemString(@NonNull String pemString) throws IOException, CertificateException {
        Pattern pattern = Pattern.compile("(-----BEGIN\\sCERTIFICATE-----(.|\\n)+?-----END\\sCERTIFICATE-----)");
        Matcher matcher = pattern.matcher(pemString);
        ArrayList<String> pems = new ArrayList<>();
        while (matcher.find()) {
            pems.add(matcher.group(0));
        }
        @SuppressWarnings("CharsetObjectCanBeUsed") byte[] pem = TextUtils.join("\n",pems).getBytes("UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pem);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        ArrayList<X509Certificate> certificates = new ArrayList<>();
        while (bufferedInputStream.available() > 0) {
            Certificate certificate = certificateFactory.generateCertificate(bufferedInputStream);
            if (certificate instanceof X509Certificate) {
                certificates.add((X509Certificate)certificate);
            }
        }
        X509Certificate[] certArray = new X509Certificate[certificates.size()];
        certificates.toArray(certArray);
        return certArray;
    }
}
