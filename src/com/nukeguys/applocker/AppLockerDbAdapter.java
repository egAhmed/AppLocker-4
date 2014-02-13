package com.nukeguys.applocker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppLockerDbAdapter
{
	private static final String DATABASE_NAME = "locker.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_NAME = "exclusive_processes";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	
	private final Context context;
	
	private class DatabaseHelper extends SQLiteOpenHelper
	{
		public DatabaseHelper()
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			try
			{
				db.execSQL("DROP TABLE IF EXISTS CRAETE TABLE exclusive_processes(_id integer PRIMARY KEY autoincrement, process_name varchar not null)");
			}
			catch(Exception ex)
			{
				Log.e("DB", "Exception in CREATE_SQL", ex);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			onCreate(db);
		}
	}
	
	public AppLockerDbAdapter(Context context)
	{
		this.context = context;
	}
	
	public AppLockerDbAdapter open()
	{
		dbHelper = new DatabaseHelper();
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
		db.close();
	}
	
	public long addExclusiveProcess(String processName)
	{
		ContentValues values = new ContentValues();
		values.put("process_name", processName);
		return db.insert(TABLE_NAME, null, values);
	}
	
	public List<String> fetchExclusiveProcesses()
	{
		List<String> exclusive_processes = new ArrayList<String>();
		
		Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
		
		if (cursor.moveToFirst())
		{
			do
			{
				exclusive_processes.add(cursor.getString(1));
			}while(cursor.moveToNext());
		}
		
		return exclusive_processes;
		
		
	}
}
