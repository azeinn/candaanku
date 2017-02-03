/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.ahmad.candaanku;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ahmad.candaanku.fragment.FourFragment;
import com.google.firebase.ahmad.candaanku.fragment.OneFragment;
import com.google.firebase.ahmad.candaanku.fragment.TambahCeritaFragment;
import com.google.firebase.ahmad.candaanku.fragment.ThreeFragment;
import com.google.firebase.ahmad.candaanku.fragment.TwoFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.ahmad.candaanku.database.AsyncDBHelper;
import com.google.firebase.ahmad.candaanku.facebookSignIn.FacebookHelper;
import com.google.firebase.ahmad.candaanku.facebookSignIn.FacebookResponse;
import com.google.firebase.ahmad.candaanku.facebookSignIn.FacebookUser;
import com.google.firebase.ahmad.candaanku.googleAuthSignin.GoogleAuthResponse;
import com.google.firebase.ahmad.candaanku.googleAuthSignin.GoogleAuthUser;
import com.google.firebase.ahmad.candaanku.googleAuthSignin.GoogleSignInHelper;
import com.google.firebase.ahmad.candaanku.twitterSignIn.TwitterHelper;
import com.google.firebase.ahmad.candaanku.twitterSignIn.TwitterResponse;
import com.google.firebase.ahmad.candaanku.twitterSignIn.TwitterUser;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements /*/*GoogleResponseListener, */FacebookResponse,
        TwitterResponse,/* LinkedInResponse, */GoogleAuthResponse/*, InstagramResponse, TabLayout.OnTabSelectedListener */{

    public static final String TAG = "Candaanku";
    public static final String TAG_CERITA = "Cerita";
    public static final String TAG_TEKATEKI = "Tekatekiku";

    private ProgressDialog mProgressDialog;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Menu menu;

    public static int signedAs = 0; // 0 = sign out, 1 = origin, 2 = google, 3 = google +, 4 = facebook, 5 = twitter,
    public static boolean justSigned = false;
    public FacebookHelper mFbHelper;
    //public GooglePlusSignInHelper mGHelper;
    public GoogleSignInHelper mGAuthHelper;
    public TwitterHelper mTwitterHelper;
    //public LinkedInHelper mLinkedInHelper;
    //public InstagramHelper mInstagramHelper;

    public String mUsername;
    public String mPhotoUrl;
    // [START declare_auth]
    // [END declare_auth_listener]
    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //new LoadView(this).execute();

        //Log.d("mainactivity1","sudah loaded");



        //mGHelper = new GooglePlusSignInHelper(this, this);

       //google auth initialization
        mGAuthHelper = new GoogleSignInHelper(this, null, this);

        //fb api initialization
        mFbHelper = new FacebookHelper(this,
                "id,name,email,gender,birthday,picture,cover",
                this);

        //twitter initialization
        mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
                R.string.twitter_secret_key,
                this,
                this);

        //linkedIn initializer
        //mLinkedInHelper = new LinkedInHelper(this, this);

        //instagram initializer
        //mInstagramHelper = new InstagramHelper(getResources().getString(R.string.instagram_client_id),
        //        getResources().getString(R.string.instagram_client_secret), getResources().getString(R.string.instagram_callback_url), this, this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                justSigned = true;

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User has  not signed in, launch the Sign In activity
                    replaceViewPager(3, 2);
                } else {
                    mUsername = user.getDisplayName();
                    if (user.getPhotoUrl() != null)
                        mPhotoUrl = user.getPhotoUrl().toString();
                    replaceViewPager(4, 2);
                }


           /*      if (user != null) {
                    // User is signed in
                    if(user.getDisplayName()!=null){
                        Log.i("mainactiity1", "user.getDisplayName()="+user.getDisplayName());
                        replaceViewPager(4, 2);
                       // if (menu!=null)
                       //     signedInOptionMenu(true);
                    }

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    //if (menu!=null)
                   //     signedInOptionMenu(false);
                    replaceViewPager(3, 2);
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
*/
                // [END_EXCLUDE]
            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        Ads.showBanner(this);
        //tabLayout.setOnTabSelectedListener(this);
    }
/*
    public class LoadView extends AsyncTask<Void, Void, Void> {
        private MainActivity mainActivity1 ;

        public LoadView ( MainActivity context ) {
            this.mainActivity1 = context ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainActivity1.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncDBHelper = new AsyncDBHelper(mainActivity1);

            mGAuthHelper = new GoogleSignInHelper(mainActivity1, null, mainActivity1);

            //fb api initialization
            mFbHelper = new FacebookHelper(mainActivity1,
                    "id,name,email,gender,birthday,picture,cover",
                    mainActivity1);

            //twitter initialization
            mTwitterHelper = new TwitterHelper(R.string.twitter_api_key,
                    R.string.twitter_secret_key,
                    mainActivity1,
                    mainActivity1);
            //Do all DB related Task Here
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mainActivity1.hideProgressDialog();
        }
    }
    */
//
//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        Log.d("mainactivity1", "tab.getPosition()="+tab.getPosition());
//        int pos = tab.getPosition();
//        /*if (pos==2) {
//            if (justSigned && signedAs>0) {
//                replaceViewPager(4);
//                //mFragmentList.add(new FourFragment());
//
//            } else {
//                replaceViewPager(3);
//                //mFragmentList.add(new ThreeFragment());
//            }
//
//        }*/
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
//

    public static AsyncDBHelper getAsyncDBHelper() {
        return SplashActivity.asyncDBHelper;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText("Cerita");
        tabLayout.getTabAt(1).setText("Teka-teki");
        tabLayout.getTabAt(2).setText("Online");
        //tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        //tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Cerita");
        adapter.addFrag(new TwoFragment(), "Teka-teki");
        adapter.addFrag(new ThreeFragment(), "Online");
        //adapter.addFrag(new FourFragment(), "FOUR");
        viewPager.setAdapter(adapter);
    }

    public void replaceViewPager(int frag, int pos) {
        if (adapter!=null) {
            switch (frag) {
                case 1:
                    adapter.replaceFrag(new TambahCeritaFragment(), pos);
                    break;
                case 2:
                    adapter.replaceFrag(new OneFragment(), pos);
                    break;

                case 3:
                    adapter.replaceFrag(new ThreeFragment(), pos);
                    break;
                case 4:
                    adapter.replaceFrag(new FourFragment(), pos);
                    break;

//                case 5:
//                    adapter.replaceFrag(new TambahCeritaFragment(), pos);
//                    break;
//                case 11:
//                    adapter.replaceFrag(new OneFragment(), pos);
//                    break;
//                case 21:
//                    adapter.replaceFrag(new TwoFragment(), pos);
//                    break;
                default: break;
            }
            adapter.notifyDataSetChanged();
           // Log.i("mainactiity1", "replacefrag = "+frag + "pos="+pos);
        }
    }
    // GoogleApiClient googleApiClient;

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
             return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public void replaceFrag(Fragment fragment, int index) {
            mFragmentList.remove(index);
            mFragmentList.add(index, fragment);
            Log.i("mainactiity1", "replaceFrag fragment= "+fragment + "index="+index);
            // do the same for the title
            //notifyDataSetChanged();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void hideOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    public void showOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        this.menu = menu;
        //showOption(R.id.action_sign_in);
        hideOption(R.id.action_sign_out);
        return true;
    }
/*
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        MenuItem menuItemSign_out = menu.findItem(R.id.action_sign_out);
        if (justSigned && signedAs>0) {
            menuItemSign_out.setTitle("Signed Out");
        }
        else {
            menuItemSign_out.setTitle("Signed Im");

        }
        justSigned = false;
        return super.onPrepareOptionsMenu(menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_quit:
                MainActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void performSignout() {
        mAuth.signOut();
        switch (signedAs) {
            // 0 = sign out, 1 = origin, 2 = google, 3 = google +, 4 = facebook, 5 = twitter,
            case 1:
                break;
            case 2:
                mGAuthHelper.performSignOut();
                break;
            case 3:
                //mGHelper.signOut();
                break;
            case 4:
                mFbHelper.performSignOut();
                break;
            case 5:
                break;
            case 6:
                //mLinkedInHelper.logout();
                break;
            case 7:
                break;
        }
        signedAs = 0;
        mUsername = null;
        mPhotoUrl = null;
        //mFirebaseAuth.signOut();
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        //mFirebaseUser = null;
        //startActivity(new Intent(this, SignInActivity.class));
        //finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //mGHelper.disconnectApiClient();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        //handle permissions
        //mGHelper.onPermissionResult(requestCode, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handle results
        mFbHelper.onActivityResult(requestCode, resultCode, data);
        //mGHelper.onActivityResult(requestCode, resultCode, data);
        mGAuthHelper.onActivityResult(requestCode, resultCode, data);
        mTwitterHelper.onActivityResult(requestCode, resultCode, data);
        //mLinkedInHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFbSignInFail() {
        Toast.makeText(this, "Gagal login ke Facebook.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbSignInSuccess(AccessToken token) {
        showProgressDialog();
        //replaceViewPager(4);
        firebaseAuthwithFacebook(token);
        //Toast.makeText(this, "Loading data..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {
        Toast.makeText(this, "Facebook user data: nama= " + facebookUser.name + " email= " + facebookUser.email, Toast.LENGTH_SHORT).show();

        Log.d("Person name: ", facebookUser.name + "");
        Log.d("Person gender: ", facebookUser.gender + "");
        Log.d("Person email: ", facebookUser.email + "");
        Log.d("Person image: ", facebookUser.facebookID + "");
    }

    @Override
    public void onFBSignOut() {
        signedAs = 0;
        Toast.makeText(this, "Berhasil loout dari Facebook", Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onGSignInFail() {
        Toast.makeText(this, "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGSignInSuccess(Person person) {
        signedAs = 2;
        //replaceViewPager(4);
        Toast.makeText(this, "Google sign in success", Toast.LENGTH_SHORT).show();
        Log.d("Person display name: ", person.getDisplayName() + "");
        Log.d("Person birth date: ", person.getBirthday() + "");
        Log.d("Person gender: ", person.getGender() + "");
        Log.d("Person name: ", person.getName() + "");
        Log.d("Person id: ", person.getImage() + "");
    }
*/

    @Override
    public void onTwitterError() {
        Toast.makeText(this, "Gagal login ke Twitter.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterSignIn(TwitterSession session) {
        showProgressDialog();
        //replaceViewPager(4);
        //Toast.makeText(this, "Loading data..", Toast.LENGTH_SHORT).show();
        firebaseAuthWithTwitter(session);
        //replaceViewPager(4);
        //Toast.makeText(this, " User id: " +  userId + "\n user name" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTwitterProfileReceived(TwitterUser user) {
        Toast.makeText(this, "Twitter user data: nama= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onLinkedInSignInFail() {
        Toast.makeText(this, "LinkedIn sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkedInSignInSuccess(String accessToken) {
        signedAs = 6;
        //replaceViewPager(4);
        Toast.makeText(this, "Linked in signin successful.\n Getting user profile...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLinkedInProfileReceived(LinkedInUser user) {
        Toast.makeText(this, "LinkedIn user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        showProgressDialog();
        //replaceViewPager(4);
        //Toast.makeText(this, "Loading data..", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        //GoogleSignInAccount account = user.getSignInAccount();
        firebaseAuthWithGoogle(user);
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(this, "Gagal login ke Google.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        //justSigned = false;
        Toast.makeText(this, isSuccess ? "Berhasil logout dari Google" : "Gagal logout dari Google", Toast.LENGTH_SHORT).show();
    }
/*
    @Override
    public void onInstagramSignInSuccess(InstagramUser user) {
        signedAs = 7;
        //replaceViewPager(4);
        Toast.makeText(this, "Instagram user data: full name name=" + user.getFull_name() + " user name=" + user.getUsername(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInstagramSignInFail(String error) {
        Toast.makeText(this, "Instagram sign in failed", Toast.LENGTH_SHORT).show();
    }
*/
//////////////////////////////////////////////////////////////////////////////////////////////////////
public void createAccount(String email, String password) {
    Log.d(TAG, "createAccount:" + email);

    showProgressDialog();

    // [START create_user_with_email]
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                         Toast.makeText(MainActivity.this, "Otentikasi gagal.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Akun user berhasil dibuat", Toast.LENGTH_SHORT).show();
                    }
                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                }
            });
    // [END create_user_with_email]
}

    public void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Otentikasi gagal",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            replaceViewPager(4, 2);
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void firebaseAuthWithTwitter(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Otentikasi gagal.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            signedAs = 1;

                        }
                        hideProgressDialog();

                        // ...
                    }
                });
    }

    private void firebaseAuthwithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Otentikasi gagal.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Otentikasi berhasil. Mengakses data..",
                                    Toast.LENGTH_SHORT).show();
                            //justSigned = true;
                            signedAs = 4;
                            //replaceViewPager(4);

                        }
                        hideProgressDialog();

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleAuthUser user) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + user.idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(user.idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Otentikasi gagal.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Otentikasi berhasil. Mengakses data..",
                                    Toast.LENGTH_SHORT).show();
                            //justSigned = true;
                            signedAs = 3;
                            //replaceViewPager(4);
                        }
                        hideProgressDialog();
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
    }

}