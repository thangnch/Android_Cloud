package com.example.facebook_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainActivity;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> loginResult;
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgAvatar = findViewById(R.id.imgAvatar);

        FacebookSdk.setApplicationId("APPID O DAY");
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        mainActivity = this;

        initFaceBook();

        LoginManager.getInstance().registerCallback(callbackManager, loginResult);
        //LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        //LoginManager.getInstance().setAuthType("auth_nonce");

        printKeyHash(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void loginFaceBook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(mainActivity, Arrays.asList("public_profile","email"));
    }

    public void logout(View view) {
        //LoginManager.getInstance().logInWithReadPermissions(mainActivity, Arrays.asList("public_profile","email"));
        AccessToken.setCurrentAccessToken(null);
        LoginManager.getInstance().logOut();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    public void shareFaceBook(View view) {
       // LoginManager.getInstance().logInWithReadPermissions(mainActivity, Arrays.asList("public_profile", "user_friends","email"));
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    //Lấy Avatar
    public URL extractFacebookIcon(String id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL imageURL = new URL("https://graph.facebook.com/" + id
                    + "/picture?type=large");
            return imageURL;
        } catch (Throwable e) {
            return null;
        }
    }

    public void initFaceBook () {
        loginResult = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Login thành công xử lý tại đây
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                // Application code
                                Log.d("full",object.toString());
                                String name = object.optString("name");
                                String id = object.optString("id");

                                URL imageURL = extractFacebookIcon(id);
                                Log.d("imageURL: ",imageURL.toString());

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                URL newurl = null;
                                try {
                                    newurl = new URL(imageURL.toString());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                Bitmap mIcon_val = null;
                                try {
                                    mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                imgAvatar.setImageBitmap(mIcon_val);

                            }
                        });
                Bundle parameters = new Bundle();
                //parameters.putString(getString(R.string.fields), getString(R.string.fields_name));
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };
    }

    //Get keyHash
    public String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

}