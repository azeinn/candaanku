package com.google.firebase.ahmad.candaanku;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.ahmad.candaanku.database.AsyncDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class TwoFragment extends Fragment{

    private TextView jokeView = null;
    private TextView answerView = null;

    // For the options menu
    private static final int CHANGE_CATEGORY_ID = 0;

    private Handler handler = new Handler();
    private AsyncDBHelper dbHelper;

    private ArrayList<CharSequence> allCategories;
    private boolean[] areChecked;

    private Cursor currentTekateki = null;
    private Iterator<String[]> tekatekiIterator;
    private ArrayList<String[]> tekatekiArray;
    private String[] currentJoke = null;

    // Used to know the index of each inside the tekatekiIterator and tekatekiArray
    private static final int INDEX_OF_ID = 0;
    private static final int INDEX_OF_QUESTION = 1;
    private static final int INDEX_OF_ANSWER = 2;

    private boolean isCategorySelected = false;

    public TwoFragment() {
        Log.d("mainactivity1", "FourFragment instantiated");
        // Required empty public constructor
        //setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        //setHasOptionsMenu(true);

        dbHelper = MainActivity.getAsyncDBHelper();
        getTekateki();

        jokeView = (TextView) view.findViewById(R.id.textViewQuestion);
        answerView = (TextView) view.findViewById(R.id.textViewAnswer);

        // If no category was selected, will not try and shuffle the empty joke
        // list.
        if (isCategorySelected) {
            saveAndShuffle(currentTekateki);
            showNextJoke();
         }

        // Shows the answer to the current joke
        Button showAnswerButton = (Button) view.findViewById(R.id.buttonShowAnswer);
        showAnswerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopTimer();
                showAnswer();
            }
        });

        // Shows the next joke and hides the answer
        Button nextJokeButton = (Button) view.findViewById(R.id.buttonNextJoke);
        nextJokeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showNextJoke();

            }
        });

        // Deletes the current joke
        Button deleteJokeButton = (Button) view.findViewById(R.id.buttonDeleteJoke);
        deleteJokeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteTekateki();

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showNextJoke();

    }

    /*
     * Gets the Tekateki for the currently selected categories from the database.
     */
    private void getTekateki() {

        // If no category was selected, or the ones selected are all empty,
        // displays a message asking the user to select one and leaves the
        // activity.
        /*SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.TAG_TEKATEKI,
                MODE_PRIVATE);

        Cursor categoryCursor = dbHelper.getCategories();

        ArrayList<String> prefCategories = new ArrayList<String>();
        allCategories = new ArrayList<CharSequence>();
        String category = null;

        // Adds the categories that were chosen by the user to the list.
        while (categoryCursor.moveToNext()) {
            category = categoryCursor.getString(1);
            allCategories.add(category);
            if (prefs.getBoolean(category, true))
                prefCategories.add(category);
        }

        categoryCursor.close();
*/
        Cursor prefsTekateki = dbHelper.getTekateki();
        if (prefsTekateki != null) {
            if (prefsTekateki.getCount() >= 1) {
                isCategorySelected = true;
                currentTekateki = prefsTekateki;
            } else
                displayToastAndLeave(getString(R.string.showjoke_categoryempty));
        } else
            displayToastAndLeave(getString(R.string.showjoke_retrievingerror));
    }

    /*
     * Displays a toast and exits this activity.
     */
    private void displayToastAndLeave(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        isCategorySelected = false;
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /*
     * Saves the tekateki to an array and shuffles them.
     */
    private void saveAndShuffle(Cursor tekateki) {
        tekatekiArray = new ArrayList<String[]>();

        while (tekateki.moveToNext()) {
            tekatekiArray.add(new String[] { "" + tekateki.getInt(INDEX_OF_ID),
                    tekateki.getString(INDEX_OF_QUESTION),
                    tekateki.getString(INDEX_OF_ANSWER) });
        }
        shuffleTekateki();
    }

    /*
     * Shuffles the tekateki and recreates the iterator.
     */
    private void shuffleTekateki() {
        Collections.shuffle(tekatekiArray);
        tekatekiIterator = tekatekiArray.iterator();
    }

    /*
     * Shows the next joke and hides the answer.
     */
    private void showNextJoke() {
        stopTimer();

        if (!tekatekiIterator.hasNext())
            shuffleTekateki();

        currentJoke = tekatekiIterator.next();

        jokeView.setText(currentJoke[INDEX_OF_QUESTION]);
        answerView.setVisibility(TextView.INVISIBLE);
        answerView.setText(currentJoke[INDEX_OF_ANSWER]);

        handler.postDelayed(showAnswerRunnable, 3500);
    }

    /*
     * Shows the answer to the current joke.
     */
    private void showAnswer() {
        answerView.setVisibility(TextView.VISIBLE);
    }

    /*
     * Deletes the joke that is currently shown.
     */
    private void deleteTekateki() {
        tekatekiIterator.remove();

        if (dbHelper.deleteTekateki(Integer.parseInt(currentJoke[INDEX_OF_ID]))) {
            Toast.makeText(getActivity(), getString(R.string.showjoke_deleted),
                    Toast.LENGTH_SHORT).show();

            // If the person deleted all the Tekateki from the selected categories,
            // displays a message and leaves the activity.
            if (tekatekiArray.isEmpty())
                displayToastAndLeave(getString(R.string.showjoke_emptiedcategories));
            else
                showNextJoke();
        } else
            Toast.makeText(getActivity(), getString(R.string.showjoke_deletionerror),
                    Toast.LENGTH_SHORT).show();
    }

    /*
     * Stops the "Show Answer" timer.
     */
    private void stopTimer() {
        handler.removeCallbacks(showAnswerRunnable);
    }

    Runnable showAnswerRunnable = new Runnable() {
        public void run() {
            showAnswer();
        }
    };
}