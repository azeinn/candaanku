package com.google.firebase.ahmad.candaanku.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ahmad.candaanku.MainActivity;
import com.google.firebase.ahmad.candaanku.R;

public class ThreeFragment extends Fragment implements View.OnClickListener/*, GoogleAuthResponse*/ {

    private static final String TAG = "ThreeFragment";
    EditText mEnterUserName;
    private EditText mPasswordField;

     //public FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    // [END declare_auth_listener]

    public ThreeFragment() {
        //Log.d("mainactivity1", "ThreeFragment instantiated");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        //Google api initialization

        //set sign in button
        view.findViewById(R.id.g_login_btn).setOnClickListener(this);
        //view.findViewById(R.id.g_plus_login_btn).setOnClickListener(this);
        view.findViewById(R.id.twitter_login_button).setOnClickListener(this);
        view.findViewById(R.id.bt_act_login_fb).setOnClickListener(this);
        //view.findViewById(R.id.linkedin_login_button).setOnClickListener(this);
        //view.findViewById(R.id.instagram_login_button).setOnClickListener(this);

        mEnterUserName = (EditText)  view.findViewById(R.id.edit_user_name);
        mPasswordField = (EditText)  view.findViewById(R.id.field_password);
        view.findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        view.findViewById(R.id.email_create_account_button).setOnClickListener(this);

        // [START initialize_auth]
        //mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]


        return view;
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.email_create_account_button:
                if (!validateForm()) {
                    return;
                }
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                ((MainActivity)getActivity()).createAccount(mEnterUserName.getText().toString(), mPasswordField.getText().toString());
                break;
            case R.id.email_sign_in_button:
                if (!validateForm()) {
                    return;
                }
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                ((MainActivity)getActivity()).signIn(mEnterUserName.getText().toString(), mPasswordField.getText().toString());
                break;
               //case R.id.submit_username:
             //   updateName();
            //    break;
            case R.id.g_login_btn:
                ((MainActivity)getActivity()).mGAuthHelper.performSignIn(getActivity());
                break;
           // case R.id.g_plus_login_btn:
           //     ((MainActivity)getActivity()).mGHelper.performSignIn();
           //     break;
            case R.id.bt_act_login_fb:
                ((MainActivity)getActivity()).mFbHelper.performSignIn(getActivity());
                break;
            case R.id.twitter_login_button:
                ((MainActivity)getActivity()).mTwitterHelper.performSignIn();
                break;
          //  case R.id.linkedin_login_button:
           //     ((MainActivity)getActivity()).mLinkedInHelper.performSignIn();
           //     break;
           // case R.id.instagram_login_button:
          //      ((MainActivity)getActivity()).mInstagramHelper.performSignIn();
          //      break;
        }
    }



    private void displaySnackBarNoConnectivity() {

        Snackbar snackbar = Snackbar
                .make(getActivity().findViewById(R.id.content_frame), "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void SubmitClicked(View view)
    {


        if(ValidateForm()) {
            //BackgroudLocationService is started
            //startService(new Intent(getBaseContext(), BackgroundLocationService.class));
            //Toast to display the start service
            Toast.makeText(getActivity(), "Service started", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean ValidateForm()
    {
        boolean valid=true;
        if(!checkInternetConnectivity(getActivity()))
        {
            displaySnackBarNoConnectivity();
            valid=false;
        }
        return valid;
    }

    private boolean checkInternetConnectivity(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;

    }






    private boolean validateUpdateDetail()
    {
        boolean valid=true;

        String username=mEnterUserName.getText().toString();
        if(TextUtils.isEmpty(username))
            if (TextUtils.isEmpty(username)) {
                mEnterUserName.setError("Required.");
                valid = false;
            } else {
                mEnterUserName.setError(null);
            }
        return valid;

    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEnterUserName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEnterUserName.setError("Required.");
            valid = false;
        } else {
            mEnterUserName.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
/*
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {



            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));



            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);

            //If username is empty
            if(user.getDisplayName()==null)
            {
                Log.d(TAG,"Empty NAme");
                findViewById(R.id.edit_user_name).setVisibility(View.VISIBLE);
                findViewById(R.id.submit_username).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_button).setVisibility(View.GONE);

            }
            else {
                findViewById(R.id.edit_user_name).setVisibility(View.GONE);
                findViewById(R.id.submit_username).setVisibility(View.GONE);
                findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            }
        } else {

            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        }
    }
*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity)getActivity()).hideProgressDialog();
    }
/*
    @Override
    public void onGoogleAuthSignIn(GoogleAuthUser user) {
        Toast.makeText(getActivity(), "Google user data: name= " + user.name + " email= " + user.email, Toast.LENGTH_SHORT).show();
        //GoogleSignInAccount account = user.getSignInAccount();
        firebaseAuthWithGoogle(user);
    }
    private void firebaseAuthWithGoogle(GoogleAuthUser user) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + user.idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(user.idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Authentication Success. Please Wait",
                                    Toast.LENGTH_SHORT).show();
                            MainActivity.justSigned = true;
                            MainActivity.signedAs = 3;
                            ((MainActivity)getActivity()).replaceViewPager(4);
                        }
                        //hideProgressDialog();
                    }
                });
    }

    @Override
    public void onGoogleAuthSignInFailed() {
        Toast.makeText(getActivity(), "Google sign in failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGoogleAuthSignOut(boolean isSuccess) {
        MainActivity.justSigned = false;
        Toast.makeText(getActivity(), isSuccess ? "Sign out success" : "Sign out failed", Toast.LENGTH_SHORT).show();
    }
*/
}