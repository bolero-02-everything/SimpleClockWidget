package jp.gr.java_conf.mi.app.simpleclock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;

public class ClockUtils
{
	private static float Time24TextSize = 56;
	private static float Time12TextSize = 34;
	private static final String SetTextSizeMethod = "setTextSize";

	public static void updateClock(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		DateFormat date = android.text.format.DateFormat.getDateFormat(context);
		DateFormat time = android.text.format.DateFormat.getTimeFormat(context);
		DateFormat dow = new SimpleDateFormat("E");
		Date now = new Date();

		RemoteViews v = new RemoteViews(context.getPackageName(), R.layout.clockwidget);

		String timeText = time.format(now);
		float textSize = timeText.length() > 5 ? Time12TextSize : Time24TextSize;

		v.setTextViewText(R.id.text_time, timeText);
		v.setFloat(R.id.text_time, SetTextSizeMethod, textSize);

		String dateText = String.format("%s %s", date.format(now) , dow.format(now));
		v.setTextViewText(R.id.text_date, dateText);

		appWidgetManager.updateAppWidget(appWidgetIds, v);
	}

//	public static void registerAlarm(Context context, Intent it)
//	{
//		long nowS = System.currentTimeMillis() / 1000L;
//		long nextS = ((nowS / 60L) + 1) * 60L;
//		PendingIntent pi = PendingIntent.getService(context, 0, it, PendingIntent.FLAG_CANCEL_CURRENT);
//		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		alarm.setRepeating(AlarmManager.RTC, nextS * 1000L, 60L * 1000L, pi);	//次から毎分
//	}
//
//	public static void unregisterAlarm(Context context, Intent it)
//	{
//		PendingIntent pi = PendingIntent.getService(context, 0, it, PendingIntent.FLAG_CANCEL_CURRENT);
//		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		alarm.cancel(pi);
//	}
}
