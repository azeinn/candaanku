//package com.google.firebase.codelab.friendlychat;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.codelab.friendlychat.database.AsyncDBHelper;
//import com.google.firebase.codelab.friendlychat.MainActivity;
//
///**
// * This activity lets the users add categories to the database.
// *
// * @author Natacha Gabbamonte 0932340
// * @author Caroline Castonguay-Boisvert 084348
// *
// */
//public class TambahKategori extends Activity {
//
//	private EditText catText = null;
//	private AsyncDBHelper database = null;
//
//	/**
//	 * Takes the input from the edit text and tries to save it to the database.
//	 * If the edit text is empty or the inserting fails, an error message will
//	 * be displayed.
//	 */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_addcategory);
//
//		database = MainActivity.getAsyncDBHelper();
//		catText = (EditText) findViewById(R.id.addcat_editTextCat);
//
//		// Add button onClickListener
//		Button addButton = (Button) findViewById(R.id.addcat_buttonAdd);
//		addButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				String displayText = null;
//				String category = catText.getText().toString().trim();
//
//				// The user didn't enter anything.
//				if (category.equals(""))
//					displayText = getString(R.string.addcat_emptycat);
//				else {
//					// addNewCategory returns the inserting point if it worked,
//					// -1 if the category already exists or -2 for any other
//					// error.
//					long code = database.addNewCategory(category);
//
//					if (code == -1)
//						displayText = getString(R.string.addcat_alreadyexists)
//								+ " (" + category + ")";
//					else if (code == -2)
//						displayText = getString(R.string.addcat_error);
//					else
//						displayText = getString(R.string.added);
//				}
//				Toast.makeText(TambahKategori.this, displayText,
//						Toast.LENGTH_SHORT).show();
//				catText.setText("");
//			}
//
//		});
//
//		// onClickListener for the "Go Back" button.
//		Button backButton = (Button) findViewById(R.id.addcat_backButton);
//		backButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				TambahKategori.this.finish();
//			}
//
//		});
//	}
//}
