package com.google.firebase.ahmad.candaanku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.ahmad.candaanku.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * This class is the link between the acitivities and the SQLite database.
 * 
 * @author Natacha Gabbamonte 0932340
 * @author Caroline Castonguay-Boisvert 084348
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_PATH_SUFFIX = "/databases/";
	private final Context myContext;

	// Jokes table
	public static final String TABLE_JOKES = "jokes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_JOKE = "joke";
	public static final String COLUMN_ANSWER = "answer";
	public static final String COLUMN_CATEGORY = "category";

	// Categories table
	public static final String TABLE_CATEGORIES = "categories";
	public static final String COLUMN_ID_CATEGORIES = "_id";
	public static final String COLUMN_CATEGORY_CATEGORIES = "category";

	// Cerita table
	public static final String TABLE_CERITA = "cerita";
	public static final String COLUMN_ID_CERITA = "_id";
	public static final String COLUMN_JUDUL_CERITA = "judul";
	public static final String COLUMN_ISI_CERITA = "isi";

	// Tekateki table
	public static final String TABLE_TEKATEKI = "tekateki";
	public static final String COLUMN_ID_TEKATEKI = "_id";
	public static final String COLUMN_SOAL_TEKATEKI = "soal";
	public static final String COLUMN_JAWABAN_TEKATEKI = "jawaban";

	private static final String DATABASE_NAME = "candaan.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_JOKES
			+ "( " + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_JOKE + " text not null, " + COLUMN_ANSWER
			+ " text not null, " + COLUMN_CATEGORY + " text not null);";

	// Database creation of the categories table
	private static final String CATEGORIES_CREATE = "create table "
			+ TABLE_CATEGORIES + "( " + COLUMN_ID_CATEGORIES
			+ " integer primary key autoincrement, "
			+ COLUMN_CATEGORY_CATEGORIES + " text not null unique);";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            The context
	 */
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;

	}

	public void CopyDataBaseFromAsset() throws IOException {
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
// Path to the just created empty db
		String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
		File f = new File(myContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
		if (!f.exists())
			f.mkdir();
// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	private String getDatabasePath() {
		return myContext.getApplicationInfo().dataDir
				+ DB_PATH_SUFFIX + DATABASE_NAME;
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		File dbFile = myContext.getDatabasePath(DATABASE_NAME);
		if (!dbFile.exists()) {
			try {
				CopyDataBaseFromAsset();
				System.out.println("Copying sucess from Assets folder");
			} catch (IOException e) {
				throw new RuntimeException("Error creating source database", e);
			}
		}
		return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
	}

	/**
	 * When the DB is created, it's tables are created and populated with
	 * default categories and jokes.
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
	/*	try {
			database.execSQL(DATABASE_CREATE);
			database.execSQL(CATEGORIES_CREATE);

			// Inserts the default categories.
			database.execSQL("insert into " + TABLE_CATEGORIES
					+ " values(null, 'Kids');");
			database.execSQL("insert into " + TABLE_CATEGORIES
					+ " values(null, 'Dumb');");
			database.execSQL("insert into " + TABLE_CATEGORIES
					+ " values(null, 'Geek');");

			// Insert the default jokes.

			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'How many balls of string would it take to reach the moon?', "
					+ "'Just one if it''s long enough!', 'Kids');");
			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'How do we know that the Earth won''t come to an end?', "
					+ "'Because it''s round!', 'Kids');");

			database.execSQL("insert into " + TABLE_JOKES
					+ " values(null, 'What do cats eat for breakfast?', "
					+ "'Mice Krispries!','Dumb');");

			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'What''s the last thing that goes through a bug''s mind when it hits a windshield?','Its butt','Dumb');");
			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'What''s it called when you lend money to a bison?','A BUFFA-LOAN!','Dumb');");

			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'How did the programmer die in the shower?','He read the shampoo bottle instructions: Lather. Rinse. Repeat.','Geek');");
			database.execSQL("insert into "
					+ TABLE_JOKES
					+ " values(null, 'Why do programmers always confuse Halloween and Christmas?','Because OCT 31 = DEC 25','Geek');");

			} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while creating the database:\n"
							+ e.getClass() + " " + e.getMessage());
		}*/
	}

	/**
	 * When upgraded, all tables will be dropped from the database.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOKES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_CERITA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEKATEKI);
		onCreate(db);
	}

	/**
	 * Returns the jokes from all the categories.
	 * 
	 * @return A cursor with all the jokes.
	 */
	public Cursor getJokes() {
		return getReadableDatabase().query(TABLE_JOKES, null, null, null, null,
				null, null);
	}

	/**
	 * Returns the jokes from specific categories.
	 * 
	 * @param categories
	 *            An array of the categories of jokes to be included.
	 * @return A cursor containing the jokes.
	 */
	public Cursor getJokesInCategories(String[] categories) {
		Cursor jokes = null;
		try {
			String whereClause = COLUMN_CATEGORY + " = ?";
			for (int i = 1; i < categories.length; i++)
				whereClause += " OR " + COLUMN_CATEGORY + " = ?";

			jokes = getReadableDatabase().query(TABLE_JOKES, null, whereClause,
					categories, null, null, null);
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while querying jokes in categories:\n"
							+ e.getClass() + " " + e.getMessage());
		}
		return jokes;
	}

	/**
	 * Returns all the categories.
	 * 
	 * @return A cursor with all the categories.
	 */
	public Cursor getCategories() {
		Cursor categoriesCursor = null;
		try {
			categoriesCursor = getReadableDatabase().query(TABLE_CATEGORIES,
					null, null, null, null, null, COLUMN_CATEGORY_CATEGORIES);
		} catch (Exception e) {
			Log.e(MainActivity.TAG, "Exception caught while getting categories:\n"
					+ e.getClass() + " " + e.getMessage());
		}
		return categoriesCursor;
	}

	/**
	 * Adds a new joke to the database.
	 * 
	 * @param joke
	 *            The joke string.
	 * @param answer
	 *            The answer string.
	 * @param category
	 *            The joke's category.
	 * @return The row ID of the newly inserted row, or -1 if an error occurred,
	 *         or -2 if an exception occurred.
	 */
	public long addNewJoke(String joke, String answer, String category) {
		long code;
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_JOKE, joke);
			cv.put(COLUMN_ANSWER, answer);
			cv.put(COLUMN_CATEGORY, category);
			code = getWritableDatabase().insert(TABLE_JOKES, null, cv);
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while adding a joke:\n" + e.getClass()
							+ " " + e.getMessage());
			code = -2;
		}
		return code;
	}

	public long addNewTekateki(String joke, String answer, String category) {
		long code;
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_SOAL_TEKATEKI, joke);
			cv.put(COLUMN_JAWABAN_TEKATEKI, answer);
			code = getWritableDatabase().insert(TABLE_TEKATEKI, null, cv);
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while adding a Tekateki:\n" + e.getClass()
							+ " " + e.getMessage());
			code = -2;
		}
		return code;
	}
	/**
	 * Adds a new category to the database.
	 * 
	 * @param category
	 *            The category string.
	 * @return The row ID of the newly inserted row, or -1 if an error occurred,
	 *         or -2 if an exception occurred.
	 */
	public long addNewCategory(String category) {
		long code;
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_CATEGORY_CATEGORIES, category);
			code = getWritableDatabase().insert(TABLE_CATEGORIES, null, cv);
		} catch (Exception e) {
			Log.e(MainActivity.TAG, "Exception caught while adding a category:\n"
					+ e.getClass() + " " + e.getMessage());
			code = -2;
		}
		return code;
	}

	/**
	 * Deletes the joke with the corresponding ID from the database.
	 * 
	 * @param id
	 *            The id of the joke.
	 * @return Whether the deletion was successful or not.
	 */
	public boolean deleteJoke(int id) {
		Boolean deleted = true;
		try {
			getWritableDatabase().delete(TABLE_JOKES, COLUMN_ID + "=?",
					new String[] { String.valueOf(id) });
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while deleting joke:\n" + "Category id: "
							+ id + "\n" + e.getClass() + " " + e.getMessage());
			deleted = false;
		}

		return deleted;
	}

	/**
	 * Deletes the category with the corresponding ID and all its jokes from the
	 * database.
	 * 
	 * @param id
	 *            The id of the category.
	 * @return Whether the deletion was successful.
	 */
	public boolean deleteCategory(int id) {
		boolean deleted = true;
		try {
			String[] idArray = new String[] { String.valueOf(id) };
			Cursor category = getReadableDatabase().query(TABLE_CATEGORIES,
					null, COLUMN_ID_CATEGORIES + "=?", idArray, null, null,
					null);

			// There should be one and only one category returned.
			if (category.getCount() != 1)
				return false;

			category.moveToNext();

			// Deletes the jokes from the category in the database.
			getWritableDatabase().delete(TABLE_JOKES, COLUMN_CATEGORY + "=?",
					new String[] { category.getString(1) });

			// Checks if the category was successfully deleted.
			if (getWritableDatabase().delete(TABLE_CATEGORIES,
					COLUMN_ID_CATEGORIES + "=?", idArray) != 1)
				deleted = false;

		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while deleting category:\n"
							+ "Category id: " + id + "\n" + e.getClass() + " "
							+ e.getMessage());
			deleted = false;
		}
		return deleted;
	}

	/**
	 * Deletes the category with the corresponding name and all its jokes from
	 * the database.
	 * 
	 * @param category
	 *            The category name.
	 * @return Whether the deletion was successful.
	 */
	public boolean deleteCategory(String category) {
		boolean deleted = true;
		try {
			// Deletes the jokes from the category in the database.
			getWritableDatabase().delete(TABLE_JOKES, COLUMN_CATEGORY + "=?",
					new String[] { category });
			
			// Checks if the category was successfully deleted.
			if (getWritableDatabase().delete(TABLE_CATEGORIES,
					COLUMN_CATEGORY_CATEGORIES + "=?",
					new String[] { category }) !=1)
				deleted = false;
		} catch (Exception e) {
			Log.e(MainActivity.TAG, "Exception caught while deleting category:\n"
					+ "Category name: " + category + "\n" + e.getClass() + " "
					+ e.getMessage());
			deleted = false;
		}
		return deleted;
	}

	public long addNewCerita(String judul, String isi) {
		long code;
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_JUDUL_CERITA, judul);
			cv.put(COLUMN_ISI_CERITA, isi);
			code = getWritableDatabase().insert(TABLE_CERITA, null, cv);
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while adding a cerita:\n" + e.getClass()
							+ " " + e.getMessage());
			code = -2;
		}
		return code;
	}

	public Cursor getCerita() {
		return getReadableDatabase().query(TABLE_CERITA, null, null, null, null,
				null, null);
	}

	public boolean deleteCerita(int id) {
		Boolean deleted = true;
		try {
			getWritableDatabase().delete(TABLE_CERITA, COLUMN_ID + "=?",
					new String[] { String.valueOf(id) });
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while deleting cerita:\n" + "Category id: "
							+ id + "\n" + e.getClass() + " " + e.getMessage());
			deleted = false;
		}

		return deleted;
	}

	public long addNewTekateki(String soal, String jawaban) {
		long code;
		try {
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_SOAL_TEKATEKI, soal);
			cv.put(COLUMN_JAWABAN_TEKATEKI, jawaban);
			code = getWritableDatabase().insert(TABLE_TEKATEKI, null, cv);
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while adding a tekateki:\n" + e.getClass()
							+ " " + e.getMessage());
			code = -2;
		}
		return code;
	}

	public Cursor getTekateki() {
		return getReadableDatabase().query(TABLE_TEKATEKI, null, null, null, null,
				null, null);
	}

	public boolean deleteTekateki(int id) {
		Boolean deleted = true;
		try {
			getWritableDatabase().delete(TABLE_TEKATEKI, COLUMN_ID + "=?",
					new String[] { String.valueOf(id) });
		} catch (Exception e) {
			Log.e(MainActivity.TAG,
					"Exception caught while deleting tekateki:\n" + "Category id: "
							+ id + "\n" + e.getClass() + " " + e.getMessage());
			deleted = false;
		}

		return deleted;
	}

}
