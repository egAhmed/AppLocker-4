package com.nukeguys.applocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtil
{
	public static SharedPreferences getPref(final Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static SharedPreferences getPref(final Context context, final String name)
	{
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public static void put(final SharedPreferences pref, final String key, final boolean value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void put(final SharedPreferences pref, final String key, final int value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void put(final SharedPreferences pref, final String key, final long value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void put(final SharedPreferences pref, final String key, final String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static boolean getBoolean(final SharedPreferences pref, final String key)
	{
		return pref.getBoolean(key, false);
	}

	public static int getInt(final SharedPreferences pref, final String key)
	{
		return pref.getInt(key, 0);
	}

	public static long getLong(final SharedPreferences pref, final String key)
	{
		return pref.getLong(key, 0);
	}

	public static String getString(final SharedPreferences pref, final String key)
	{
		return pref.getString(key, null);
	}

	public static void remove(final SharedPreferences pref, final String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
}