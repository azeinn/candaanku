package com.myproject.firebase.ahmad.candaanku.twitterSignIn;

import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by multidots on 6/17/2016.<p>
 * This is response listener for twitter profile calls.
 */
public interface TwitterResponse {
    /**
     * This method will call when twitter sign in fails.
     */
    void onTwitterError();

    /**
     * This method will execute when twitter app is authorized by the user and access token is received.
     *
     * @param userId   twitter user id.
     * @param userName twitter user name
     */
    //void onTwitterSignIn(@NonNull String userId, @NonNull String userName);
    void onTwitterSignIn(TwitterSession session);

    /**
     * This method will execute when user profile is received.
     *
     * @param user {@link TwitterUser} profile.
     */
    void onTwitterProfileReceived(TwitterUser user);
}
