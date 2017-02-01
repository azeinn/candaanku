///**
// * Copyright Google Inc. All Rights Reserved.
// * <p/>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p/>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p/>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.google.firebase.codelab.friendlychat;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.google.android.gms.plus.model.people.Person;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.codelab.friendlychat.database.AsyncDBHelper;
//import com.google.firebase.codelab.friendlychat.facebookSignIn.FacebookHelper;
//import com.google.firebase.codelab.friendlychat.facebookSignIn.FacebookResponse;
//import com.google.firebase.codelab.friendlychat.facebookSignIn.FacebookUser;
//import com.google.firebase.codelab.friendlychat.googleAuthSignin.GoogleAuthResponse;
//import com.google.firebase.codelab.friendlychat.googleAuthSignin.GoogleAuthUser;
//import com.google.firebase.codelab.friendlychat.googleAuthSignin.GoogleSignInHelper;
//import com.google.firebase.codelab.friendlychat.googleSignIn.GooglePlusSignInHelper;
//import com.google.firebase.codelab.friendlychat.googleSignIn.GoogleResponseListener;
//import com.google.firebase.codelab.friendlychat.instagramSignIn.InstagramHelper;
//import com.google.firebase.codelab.friendlychat.instagramSignIn.InstagramResponse;
//import com.google.firebase.codelab.friendlychat.instagramSignIn.InstagramUser;
//import com.google.firebase.codelab.friendlychat.linkedInSiginIn.LinkedInHelper;
//import com.google.firebase.codelab.friendlychat.linkedInSiginIn.LinkedInResponse;
//import com.google.firebase.codelab.friendlychat.linkedInSiginIn.LinkedInUser;
//import com.google.firebase.codelab.friendlychat.twitterSignIn.TwitterHelper;
//import com.google.firebase.codelab.friendlychat.twitterSignIn.TwitterResponse;
//import com.google.firebase.codelab.friendlychat.twitterSignIn.TwitterUser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity11 extends AppCompatActivity implements GoogleResponseListener, FacebookResponse, TwitterResponse,
//        LinkedInResponse, GoogleAuthResponse, InstagramResponse {
//
//    private static final String TAG = "MainActivity";
//    public static final String TAG_CERITA = "Candaanku";
//    public static final String TAG_TEKATEKI = "Tekatekiku";
//
//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    private ViewPager viewPager;
//
//    public static int signedAs = 0; // 0 = sign out, 1 = origin, 2 = google, 3 = google +, 4 = facebook, 5 = twitter,
//        // 6 = linkedin, 7 = instagram
//    public FacebookHelper mFbHelper;
//    public GooglePlusSignInHelper mGHelper;
//    public GoogleSignInHelper mGAuthHelper;
//    public TwitterHelper mTwitterHelper;
//    public LinkedInHelper mLinkedInHelper;
//    public InstagramHelper mInstagramHelper;
//
//    public String mUsername;
//    public String mPhotoUrl;
//    // [START declare_auth]
//    // [END declare_auth_listener]
//    public FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    private static AsyncDBHelper asyncDBHelper;
//    ViewPagerAdapter adapter;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
//
//        Ads.showBanner(this);
//
//        asyncDBHelper = new AsyncDBHelper(this);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
//
//        mGHelper = new GooglePlusSignInHelper(this, this);
//
//        //google auth initialization
//        mGAuthHelper = new GoogleSignInHelper(this, null, this);
//
//        //fb api initialization
//        mFbHelper = new FacebookHelper(this,
//                "id,name,email,gender,birthday,picture,cover",
//                this);
//
//        //twitter initialization
//        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
//                R.string.twitter_secrate_key,
//                this,
//                this);
//
//        //linkedIn initializer
//        mLinkedInHelper = new LinkedInHelper(this, this);
//
//        //instagram initializer
//        mInstagramHelper = new InstagramHelper(
//                getResources().getString(R.string.instagram_client_id),
//                getResources().getString(R.string.instagram_client_secret),
//                getResources().getString(R.string.instagram_callback_url), this, this);
//
//        replaceViewPager(4);
//
//
//
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            // User is signed in
//            if(mAuth.getCurrentUser().getDisplayName()!=null){
//                Log.i("mainactivity1", "user logged in");
//                replaceViewPager(4);
//            }
//
//        }
//        else {
//            replaceViewPager(3);
//            Log.d("mainactivity1", "user not logged in");
//        }
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    if(user.getDisplayName()!=null){
//                        replaceViewPager(4);
//                    }
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    replaceViewPager(3);
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
//                // [START_EXCLUDE]
//                //updateUI(user);
//
//                // [END_EXCLUDE]
//            }
//        };
//    }
//
//    public static AsyncDBHelper getAsyncDBHelper() {
//        return asyncDBHelper;
//    }
//
//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setText("Cerita");
//        tabLayout.getTabAt(1).setText("Teka-teki");
//        tabLayout.getTabAt(2).setText("Online");
//        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        //tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new OneFragment(), "Cerita");
//        adapter.addFrag(new TwoFragment(), "Teka-teki");
//        adapter.addFrag(new ThreeFragment(), "Online");
//        //adapter.addFrag(new FourFragment(), "FOUR");
//        viewPager.setAdapter(adapter);
//    }
//    public void replaceViewPager(int frag) {
//        if (adapter!=null) {
//            if (frag == 4) {
//                adapter.replaceFrag(new FourFragment(), 2);
//                Log.i("mainactiity1", "replacefrag = 4");
//            }
//            else if (frag == 3)
//                adapter.replaceFrag(new ThreeFragment(), 2);
//            adapter.notifyDataSetChanged();
//        }
//    }
//    // GoogleApiClient googleApiClient;
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//          /*  if (position==2) {
//                if (mAuth.getCurrentUser() != null) {
//                    replaceFrag(new FourFragment(), 2);
//                    //mFragmentList.add(new FourFragment());
//
//                } else {
//                    replaceFrag(new ThreeFragment(), 2);
//                    //mFragmentList.add(new ThreeFragment());
//                }
//            }
//            //else*/
//
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            // POSITION_NONE makes it possible to reload the PagerAdapter
//            return POSITION_NONE;
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFrag(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//        public void replaceFrag(Fragment fragment, int index) {
//            mFragmentList.remove(index);
//            mFragmentList.add(index, fragment);
//            // do the same for the title
//            //notifyDataSetChanged();
//        }
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.action_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_search:
//                return true;
//
//            case R.id.action_share:
//                return true;
//
//            case R.id.action_refresh:
//                return true;
//
//            case R.id.action_sign_out:
//                //mFirebaseAuth.signOut();
//                //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//                //mFirebaseUser = null;
//                //mUsername = null;
//                //mPhotoUrl = null;
//                //startActivity(new Intent(this, SignInActivity.class));
//                //finish();
//                return true;
//
//            case R.id.action_check_updates:
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mGHelper.disconnectApiClient();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        //handle permissions
//        mGHelper.onPermissionResult(requestCode, grantResults);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //handle results
//        mFbHelper.onActivityResult(requestCode, resultCode, data);
//        mGHelper.onActivityResult(requestCode, resultCode, data);
//        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
//        mTwitterHelper.onActivityResult(requestCode, resultCode, data);
//        mLinkedInHelper.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onFbSignInFail() {
//        Toast.makeText(this, "Facebook sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onFbSignInSuccess() {
//        signedAs = 4;
//        replaceViewPager(4);
//        Toast.makeText(this, "Facebook sign in success", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onFbProfileReceived(FacebookUser facebookUser) {
//        Toast.makeText(this, "Facebook user data: name= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();
//
//        Log.d("Person name: ", facebookUser.name + "");
//        Log.d("Person gender: ", facebookUser.gender + "");
//        Log.d("Person email: ", facebookUser.email + "");
//        Log.d("Person image: ", facebookUser.facebookID + "");
//    }
//
//    @Override
//    public void onFBSignOut() {
//        signedAs = 0;
//        Toast.makeText(this, "Facebook sign out success", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGSignInFail() {
//        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGSignInSuccess(Person person) {
//        signedAs = 2;
//        replaceViewPager(4);
//        Toast.makeText(this, "Google sign in success", Toast.LENGTH_SHORT).show();
//        Log.d("Person display name: ", person.getDisplayName() + "");
//        Log.d("Person birth date: ", person.getBirthday() + "");
//        Log.d("Person gender: ", person.getGender() + "");
//        Log.d("Person name: ", person.getName() + "");
//        Log.d("Person id: ", person.getImage() + "");
//    }
//
//    @Override
//    public void onTwitterError() {
//        Toast.makeText(this, "Twitter sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onTwitterSignIn(@NonNull String userId, @NonNull String userName) {
//        signedAs = 5;
//        replaceViewPager(4);
//        Toast.makeText(this, " User id: " + userId + "\n user name" + userName, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onTwitterProfileReceived(TwitterUser user) {
//        Toast.makeText(this, "Twitter user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLinkedInSignInFail() {
//        Toast.makeText(this, "LinkedIn sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLinkedInSignInSuccess(String accessToken) {
//        signedAs = 6;
//        replaceViewPager(4);
//        Toast.makeText(this, "Linked in signin successful.\n Getting user profile...", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onLinkedInProfileReceived(LinkedInUser user) {
//        Toast.makeText(this, "LinkedIn user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGoogleAuthSignIn(GoogleAuthUser user) {
//        signedAs = 3;
//        replaceViewPager(4);
//        Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGoogleAuthSignInFailed() {
//        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onGoogleAuthSignOut(boolean isSuccess) {
//        Toast.makeText(this, isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onInstagramSignInSuccess(InstagramUser user) {
//        signedAs = 7;
//        replaceViewPager(4);
//        Toast.makeText(this, "Instagram user data: full name name=" + user.getFull_name() + " user name=" + user.getUsername(), Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onInstagramSignInFail(String error) {
//        Toast.makeText(this, "Instagram sign in failed", Toast.LENGTH_SHORT).show();
//    }
//
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        MenuItem register = menu.findItem(R.id.action_sign_out);
//        if(signedAs==0)
//        {
//            register.setVisible(false);
//        }
//        else
//        {
//            register.setVisible(true);
//        }
//        return true;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//}