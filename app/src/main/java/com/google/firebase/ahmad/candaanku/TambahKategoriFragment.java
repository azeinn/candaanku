//package com.google.firebase.codelab.friendlychat;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.codelab.friendlychat.database.AsyncDBHelper;
//
///**
// * This activity lets the users add categories to the database.
// *
// * @author Natacha Gabbamonte 0932340
// * @author Caroline Castonguay-Boisvert 084348
// *
// */
//public class TambahKategoriFragment extends Fragment {
//
//	private EditText catText = null;
//	private AsyncDBHelper database = null;
//
//	public TambahKategoriFragment() {
//		Log.d("mainactivity1", "FourFragment instantiated");
//// Required empty public constructor
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//							 Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.activity_addcategory, container, false);
//
//		database = MainActivity.getAsyncDBHelper();
//		catText = (EditText) view.findViewById(R.id.addcat_editTextCat);
//
//		// Add button onClickListener
//		Button addButton = (Button) view.findViewById(R.id.addcat_buttonAdd);
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
//				Toast.makeText(getActivity(), displayText,
//						Toast.LENGTH_SHORT).show();
//				catText.setText("");
//			}
//
//		});
//
//		// onClickListener for the "Go Back" button.
//		Button backButton = (Button) view.findViewById(R.id.addcat_backButton);
//		backButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				//getActivity().getSupportFragmentManager().popBackStack();
//				((MainActivity)getActivity()).replaceViewPager(11,0);
//				//TambahKategoriFragment.this.finish();
//			}
//
//		});
//
//		return view;
//	}
//}
