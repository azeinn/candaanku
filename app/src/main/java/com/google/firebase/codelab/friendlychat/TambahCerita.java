//package com.google.firebase.codelab.friendlychat;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.codelab.friendlychat.database.AsyncDBHelper;
//import com.google.firebase.codelab.friendlychat.MainActivity1;
//
//import java.util.ArrayList;
//
///**
// * This activity lets the users add a joke to the database. It also offers a
// * menu that lets the users google search any of the existing categories.
// *
// * @author Natacha Gabbamonte 0932340
// * @author Caroline Castonguay-Boisvert 084348
// *
// */
//public class TambahCerita extends Activity {
//
//	private ArrayList<String> categories = new ArrayList<String>();
//	private RadioGroup radioGroup = null;
//	private EditText jokeText = null;
//	private EditText answerText = null;
//
//	private AsyncDBHelper database = null;
//
//	private static final int GOOGLE_SEARCH_ID = 1;
//
//	/**
//	 * Adds all the categories to the ListView and validates input from the
//	 * user, then tries to insert the new joke in the database. If an error is
//	 * encountered during the process, an error message will be displayed.
//	 */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addjoke);
//
//		database = MainActivity1.getAsyncDBHelper();
//
//		// Adds all the categories to the ListView.
//		addCategoriesToList();
//
//		jokeText = (EditText) findViewById(R.id.editTextJoke);
//		answerText = (EditText) findViewById(R.id.editTextAns);
//
//		// AddJoke button onClickListener
//		Button addButton = (Button) findViewById(R.id.buttonAdd);
//		addButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				String errorMessage = "";
//				String joke = jokeText.getText().toString().trim();
//				String answer = answerText.getText().toString().trim();
//
//				if (joke.equals(""))
//					errorMessage = getString(R.string.addjoke_nojoke);
//				else if (answer.equals(""))
//					errorMessage = getString(R.string.addjoke_noanswer);
//
//				if (errorMessage != "") {
//					Toast.makeText(TambahCerita.this, errorMessage,
//							Toast.LENGTH_LONG).show();
//				} else {
//					// Add the new valid joke.
//					RadioButton selectedButton = (RadioButton) radioGroup
//							.findViewById(radioGroup.getCheckedRadioButtonId());
//					String category = selectedButton.getText().toString();
//
//					database.addNewJoke(joke, answer, category);
//					Toast.makeText(TambahCerita.this, R.string.addjoke_added,
//							Toast.LENGTH_LONG).show();
//
//					jokeText.setText("");
//					answerText.setText("");
//				}
//			}
//
//		});
//
//		Button backButton = (Button) findViewById(R.id.addjoke_backButton);
//		backButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				TambahCerita.this.finish();
//			}
//
//		});
//	}
//
//	/**
//	 * Adds the Google Search option to the options menu.
//	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(Menu.NONE, GOOGLE_SEARCH_ID, Menu.NONE, getString(R.string.addjoke_google));
//		getMenuInflater().inflate(R.menu.activity_splash, menu);
//		return true;
//	}
//
//	/**
//	 * Launches a google search on the selected category.
//	 */
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case GOOGLE_SEARCH_ID:
//			showCategoriesDialog();
//			break;
//		}
//		return false;
//	}
//
//	/*
//	 * Displays the categories dialog when the user clicks on the google search
//	 * item in the options menu.
//	 */
//	private void showCategoriesDialog() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		ListView menuListView = (ListView) inflater.inflate(
//				R.layout.menu_listview, null);
//
//		Cursor categoriesCursor = database.getCategories();
//
//		// Adds all the categories to the ArrayList
//		ArrayList<String> categories = new ArrayList<String>();
//		while (categoriesCursor.moveToNext())
//			categories.add(categoriesCursor.getString(1));
//
//		categoriesCursor.close();
//
//		menuListView.setAdapter(new ArrayAdapter<String>(this,
//				R.layout.menu_textview, categories));
//		menuListView.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view,
//									int position, long id) {
//				TextView textView = (TextView) view;
//				Intent googleSearch = new Intent(Intent.ACTION_VIEW, Uri
//						.parse(getString(R.string.addjoke_googleURL)
//								+ textView.getText().toString()));
//				startActivity(googleSearch);
//			}
//
//		});
//
//		TextView titleTextView = (TextView) inflater.inflate(
//				R.layout.menu_textview, null);
//		titleTextView.setText(R.string.addjoke_title);
//
//		builder.setCustomTitle(titleTextView);
//		builder.setView(menuListView);
//		builder.setNegativeButton(R.string.addjoke_cancel, null);
//
//		builder.create().show();
//	}
//
//	/*
//	 * Will add all the categories from the database to the ListView.
//	 */
//	private void addCategoriesToList() {
//		Cursor categoryCursor = database.getCategories();
//
//		// There are no categories. The activity will exit.
//		if (categoryCursor.getCount() <= 0) {
//			categoryCursor.close();
//			Toast.makeText(this, getString(R.string.addjoke_nocat),
//					Toast.LENGTH_LONG).show();
//			this.finish();
//		}
//
//		// Adds the categories to the ArrayList
//		while (categoryCursor.moveToNext())
//			categories.add(categoryCursor.getString(1));
//
//		categoryCursor.close();
//		radioGroup = (RadioGroup) findViewById(R.id.addjoke_radioGroup);
//
//		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		// Adds the radio buttons to the ListView
//		RadioButton newRadioButton;
//		for (String c : categories) {
//			newRadioButton = (RadioButton) inflater.inflate(
//					R.layout.addjoke_radiobutton, null);
//			newRadioButton.setText(c);
//			radioGroup.addView(newRadioButton);
//		}
//
//		// Sets the first category as selected.
//		if (radioGroup.getChildCount() > 0)
//			radioGroup.getChildAt(0).performClick();
//	}
//}
