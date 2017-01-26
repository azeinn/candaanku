package com.google.firebase.codelab.friendlychat.facebookSignIn;

import com.facebook.AccessToken;

/**
 * Created by multidots on 6/16/2016.
 */
public interface FacebookResponse {
    void onFbSignInFail();

    void onFbSignInSuccess(AccessToken token);
    //void onFbSignInSuccess();

    void onFbProfileReceived(FacebookUser facebookUser);

    void onFBSignOut();
}
