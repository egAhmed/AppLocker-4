package com.nukeguys.applocker;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AppLockService extends Service 
{	
	LockerThread lockerThread;
	boolean isInterrupted; 
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		List<String> excludeProcNames = new ArrayList<String>();
		excludeProcNames.add(getPackageName());
		excludeProcNames.add("com.sec.android.app.launcher");
		excludeProcNames.add("com.android.contacts");
		excludeProcNames.add("com.android.mms");
		
		isInterrupted = false;
		lockerThread = new LockerThread(excludeProcNames);
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

	class LockerThread extends Thread
	{
		List<String> initialProcNames = new ArrayList<String>();
		List<String> excludeProcNames;
		
		
		public LockerThread(List<String> excludeProcNames) 
		{
			this.excludeProcNames = excludeProcNames;
			initialProcNames = getRunningAppProcesseNames(RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
		}
		
		private List<String> getRunningAppProcesseNames(int flag)
		{
			ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runProc = activityManager.getRunningAppProcesses();
			List<String> processNames = new ArrayList<String>();
			
			for(RunningAppProcessInfo procInfo : runProc)
			{
				if (procInfo.importance == flag)
					processNames.add(procInfo.processName);
			}
			return processNames;
		}
		
//		private List<String> getInstalledApplications(int flag)
//		{
//			PackageManager packageManager = getPackageManager();
//			List<ApplicationInfo> installedApp = packageManager.getInstalledApplications(flag);
//			
//			List<String> processNames = new ArrayList<String>();
//			for (ApplicationInfo info : installedApp)
//			{
//				processNames.add(info.processName);
//			}
//			return processNames;
//		}

//		private List<String> getLauncherActivitiesName()
//		{
//			PackageManager packageManager = getPackageManager();
//			Intent intent = new Intent(Intent.ACTION_MAIN, null);
//			intent.addCategory(Intent.CATEGORY_LAUNCHER);
//			List<ResolveInfo> launchActivity = packageManager.queryIntentActivities(intent, 0);
//			List<String> processNames = new ArrayList<String>();
//			for (ResolveInfo info : launchActivity)
//			{
//				processNames.add(info.activityInfo.processName);
//			}
//			return processNames;
//		}
		
		private boolean isLockProcess(String procName)
		{
			if(!isInitialProcess(procName) && !isExcludeProcess(procName))
				return true;
			return false;
		}
		
		private boolean isInitialProcess(String procName)
		{
			return initialProcNames.contains(procName);
		}
		
		private boolean isExcludeProcess(String procName)
		{
			return excludeProcNames.contains(procName);
		}
		
		@Override
		public void run() 
		{
			while(!isInterrupted)
			{
				List<String> curProcNames = getRunningAppProcesseNames(RunningAppProcessInfo.IMPORTANCE_FOREGROUND);
					
				for(String procName : curProcNames)
				{
					if(isLockProcess(procName))
					{
						Log.i("APP LOCKER", procName + " process locked!");
						//activityManager.killBackgroundProcesses(process.processName);
						//Process.sendSignal(process.pid, Process.SIGNAL_KILL);
			
						//홈화면으로 이동시킴
						Intent homeIntent = new Intent();
						homeIntent.setAction(Intent.ACTION_MAIN);
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						homeIntent.addCategory(Intent.CATEGORY_HOME);
						startActivity(homeIntent);
						
						// Locker 실행
						Intent intent = new Intent(AppLockService.this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						
						break;
					 }
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