![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/AppliedRecognition/Ver-ID-SDK-Identity-Android) ![CI](https://github.com/AppliedRecognition/Ver-ID-SDK-Identity-Android/workflows/CI/badge.svg?event=push)

# Ver-ID SDK Identity

### Library that provides a client identity to [Ver-ID SDK](https://github.com/AppliedRecognition/Ver-ID-UI-Android) 1.19.0 and newer

## Installation

 1. [Generate a Github personal access token](https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line). Ensure you select the **read:packages** scope.
 2. Open your module's **build.gradle** file and add the following entries replacing `yourGithubUsername` with your Github user name and `yourGithubPersonalAccessToken` with the access token you generated in the previous step:

    ~~~groovy
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/AppliedRecognition/Ver-ID-SDK-Identity-Android")
            credentials {
                username = yourGithubUsername
                password = yourGithubPersonalAccessToken
            }
        }
    }
    dependencies {
        implementation 'com.appliedrec.verid:identity:[1.0.0,2.0.0['
    }
    ~~~
 3. Sync your Android Studio project.

## Obtaining credentials Ver-ID SDK credentials

1. [Register your app](https://dev.ver-id.com/licensing/). You will need your app's bundle identifier.
2. Registering your app will generate an evaluation licence for your app. The licence is valid for 30 days. If you need a production licence please [contact Applied Recognition](mailto:sales@appliedrec.com).
3. When you finish the registration you'll receive a file called **Ver-ID identity.p12** and a password.

## Creating a Ver-ID SDK identity

### Option 1
1. Copy the **Ver-ID identity.p12** file in your app's assets.
2. Place your password in your app's **AndroidManifest.xml**:

    ~~~xml
    <manifest>
        <application>
            <meta-data 
                android:name="com.appliedrec.verid.password" 
                android:value="your password goes here" />
        </application>
    </manifest>
    ~~~
3. Create an instance of **VerIDSDKIdentity**:

    ~~~java
    try {
        Context context; // Your application context
        VerIDSDKIdentity identity = new VerIDSDKIdentity(context);
    } catch(Exception e) {
    }
    ~~~
    
### Option 2
1. Copy the **Ver-ID identity.p12** file in your app's assets.
2. Create an instance of **VerIDSDKIdentity**:

    ~~~java
    try {
        Context context; // Your application context
        VerIDSDKIdentity identity = new VerIDSDKIdentity(context, "your password goes here");
    } catch(Exception e) {
    }
    ~~~
    
### Option 3
1. Upload the **Ver-ID identity.p12** online.
2. Create an instance of **VerIDSDKIdentity** referencing the URL of the **Ver-ID identity.p12** file:

    ~~~java
    try {
        URL url = new URL("https://ver-id.s3.us-east-1.amazonaws.com/ios/com.appliedrec.verid.licenceclient/test_assets/Ver-ID%20identity.p12")
        VerIDSDKIdentity identity = new VerIDSDKIdentity(url, "your password goes here");
    } catch(Exception e) {
    }
    ~~~
    
### Option 4
1. Store the **Ver-ID identity.p12** in your app.
3. Create an instance of **VerIDSDKIdentity** referencing the **Ver-ID identity.p12** file:

    ~~~java
    try {
        File file = new File("path/to/Ver-ID identity.p12");
        VerIDSDKIdentity identity = new VerIDSDKIdentity(file, "your password goes here");
    } catch(Exception e) {
    }
    ~~~

## Providing your identity to Ver-ID SDK 1.19.0 and newer
[Create an instance](#creating-a-ver-id-sdk-identity) of **VerIDSDKIdentity** and pass it to [**VerIDFactory**](https://appliedrecognition.github.io/Ver-ID-UI-Android/com.appliedrec.verid.core.VerIDFactory.html):

~~~java    
try {
    Context context; // Your application context
    // See above
    VerIDSDKIdentity identity = new VerIDSDKIdentity(context);
    // Construct VerIDFactory with your identity
    VerIDFactory veridFactory = new VerIDFactory(identity);
    // ... use veridFactory to create an instance of VerID
} catch(Exception e) {
}
~~~

## [Reference documentation](https://appliedrecognition.github.io/Ver-ID-SDK-Identity-Android/)
