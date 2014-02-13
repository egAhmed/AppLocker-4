package com.nukeguys.applocker;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class AppLockService extends Service
{
	LockerThread	lockerThread;
	boolean			isInterrupted;
	List<String>	excludeProcNames;

	@Override
	public void onCreate()
	{
		super.onCreate();

		SharedPreferences preference = PrefUtil.getPref(this);
		excludeProcNames = JSONUtil.makeListFromJson(PrefUtil.getString(preference, "exclusive_processes"));

//		AppLockerDbAdapter dbAdapter = new AppLockerDbAdapter(this);
//		dbAdapter.open();
//		excludeProcNames = dbAdapter.fetchExclusiveProcesses();
//		dbAdapter.close();
		
		isInterrupted = false;
		lockerThread = new LockerThread();
		lockerThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i("LOCKER", "onStartCommand");
		super.onStartCommand(intent, flags, startId);

		// START_STICKY
		// START_NOT_STICKY
		// START_REDELIVER_INTENT
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		isInterrupted = true;
		Log.i("LOCKER", "Service Destroy");
		super.onDestroy();
	}
	
//	private List<String> getRunningAppProcesseNames(int flag)
//	{
//		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//		List<RunningAppProcessInfo> runProc = activityManager.getRunningAppProcesses();
//		List<String> processNames = new ArrayList<String>();
//
//		for (RunningAppProcessInfo procInfo : runProc)
//		{
//			if (procInfo.importance == flag)
//				processNames.add(procInfo.processName);
//		}
//		return processNames;
//	}

	class LockerThread extends Thread
	{
		private String getTopActivityPkgName()
		{
			ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runTask = activityManager.getRunningTasks(1);
			return runTask.get(0).topActivity.getPackageName();
		}

		@Override
		public void run()
		{
			while (!isInterrupted)
			{
				String runPkgName = getTopActivityPkgName();

				Log.i("APP LOCKER", "TopActivity: " + runPkgName);

				if (excludeProcNames.contains(runPkgName))
					Log.i("APP LOCKER", runPkgName + " process is exclude!");
				else
				{
					Log.i("APP LOCKER", runPkgName + " process locked!");
					// 홈화면으로 이동시킴
					Intent homeIntent = new Intent();
					homeIntent.setAction(Intent.ACTION_MAIN);
					homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					homeIntent.addCategory(Intent.CATEGORY_HOME);
					startActivity(homeIntent);

					// Locker 실행
					Intent intent = new Intent(AppLockService.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
				}

				Log.i("APP LOCKER", "================== CYCLE ====================");

				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}