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
package com.google.firebase.codelab.friendlychat;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.firebase.codelab.friendlychat.database.AsyncDBHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {

    public static final String TAG_CERITA = "Candaanku";
    public static final String TAG_TEKATEKI = "Tekatekiku";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    //private int[] tabIcons = {
    //        R.drawable.ic_tab_favourite,
    //        R.drawable.ic_tab_call,
    //        R.drawable.ic_tab_contacts
    //};
    private static AsyncDBHelper asyncDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        Ads.showBanner(this);

        asyncDBHelper = new AsyncDBHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    public static AsyncDBHelper getAsyncDBHelper() {
        return asyncDBHelper;
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Cerita");
        adapter.addFrag(new TwoFragment(), "Teka-teki");
        adapter.addFrag(new ThreeFragment(), "Online");
        //adapter.addFrag(new FourFragment(), "FOUR");
        viewPager.setAdapter(adapter);
    }

    GoogleApiClient googleApiClient;

    class ViewPagerAdapter extends FragmentPagerAdapter {
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
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                return true;

            case R.id.action_share:
                return true;

            case R.id.action_refresh:
                return true;

            case R.id.action_sign_out:
                //mFirebaseAuth.signOut();
                //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                //mFirebaseUser = null;
                //mUsername = null;
                //mPhotoUrl = null;
                //startActivity(new Intent(this, SignInActivity.class));
                //finish();
                return true;

            case R.id.action_check_updates:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}