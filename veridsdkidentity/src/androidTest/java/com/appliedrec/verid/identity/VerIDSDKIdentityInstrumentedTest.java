package com.appliedrec.verid.identity;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Signature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VerIDSDKIdentityInstrumentedTest {

    private Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    private final String correctPassword = "dummy";
    private final String commonName = "verid.client.identity";

    @Test
    public void testCreateClient_failMissingCredentials() {
        try {
            new VerIDSDKIdentity(getContext());
            fail();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testCreateClient_succeeds() {
        try {
            new VerIDSDKIdentity(getIdentityInputStream(), correctPassword);
        } catch (Exception e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void testCreateClient_failMissingPassword() {
        try {
            new VerIDSDKIdentity(getIdentityInputStream(), null);
            fail();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testCreateClient_failMissingIdentityFile() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[]{0,0,0,0,0,0,0,0});
            new VerIDSDKIdentity(inputStream, correctPassword);
            fail();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testCreateClient_failInvalidPassword() {
        try {
            new VerIDSDKIdentity(getIdentityInputStream(), "nonsense");
            fail();
        } catch (Exception ignore) {
        }
    }

    @Test
    public void testCreateIdentityFromRemoteURL_succeeds() {
        try {
            URL url = new URL("https://ver-id.s3.us-east-1.amazonaws.com/ios/com.appliedrec.verid.licenceclient/test_assets/Ver-ID%20identity.p12");
            new VerIDSDKIdentity(url, correctPassword);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testClientCommonName_matches() {
        try {
            VerIDSDKIdentity identity = new VerIDSDKIdentity(getIdentityInputStream(), correctPassword);
            assertEquals(identity.getCommonName(), commonName);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSignMessage_succeeds() {
        try {
            VerIDSDKIdentity identity = new VerIDSDKIdentity(getIdentityInputStream(), correctPassword);
            byte[] message = {0,0,0,0,0,0,0,0};
            identity.sign(message);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testVerifySignature_succeeds() {
        try {
            VerIDSDKIdentity identity = new VerIDSDKIdentity(getIdentityInputStream(), correctPassword);
            byte[] message = {0,0,0,0,0,0,0,0};
            Signature signature = Signature.getInstance(VerIDSDKIdentity.DEFAULT_SIGNATURE_ALGORITHM);
            signature.initVerify(identity.getCertificate());
            signature.update(message);
            if (!signature.verify(identity.sign(message))) {
                throw new Exception("Failed to verify settings signature");
            }
        } catch (Exception e) {
            fail();
        }
    }

    private InputStream getIdentityInputStream() throws IOException {
        return InstrumentationRegistry.getInstrumentation().getContext().getAssets().open("Ver-ID identity.p12");
    }
}
