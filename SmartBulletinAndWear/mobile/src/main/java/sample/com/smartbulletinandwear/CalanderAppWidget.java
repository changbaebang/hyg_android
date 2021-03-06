package sample.com.smartbulletinandwear;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.lge.launcher2.smartbulletin.lib.Notification;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Implementation of App Widget functionality.
 */
public class CalanderAppWidget extends AppWidgetProvider {

    private static final String ACTION_PREVIOUS_MONTH
            = "com.example.android.monthcalendarwidget.action.PREVIOUS_MONTH";
    private static final String ACTION_NEXT_MONTH
            = "com.example.android.monthcalendarwidget.action.NEXT_MONTH";
    private static final String ACTION_RESET_MONTH
            = "com.example.android.monthcalendarwidget.action.RESET_MONTH";
    private static final String ACTION_ADD_NOTIFICAION
            = "com.example.android.monthcalendarwidget.action.ADD_NOTIFICAION";

    private static final String PREF_MONTH = "month";
    private static final String PREF_YEAR = "year";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("widget debug", "onUpdate");
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    private void redrawWidgets(Context context) {
        Log.d("widget debug", "redrawWidgets");
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, CalanderAppWidget.class));
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        Log.d("check click", "onReceive : " + action);

        if (ACTION_PREVIOUS_MONTH.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);
            sp.edit()
                    .putInt(PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (ACTION_NEXT_MONTH.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);
            sp.edit()
                    .putInt(PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (ACTION_RESET_MONTH.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().remove(PREF_MONTH).remove(PREF_YEAR).apply();
            redrawWidgets(context);
        } else if(ACTION_ADD_NOTIFICAION.equals(action)){
            ComponentName providerComponentName = new ComponentName(context, CalanderAppWidget.class);
            Notification.sendAddOnceIntent(context, providerComponentName, 0);
            GoogleApiClientSender.init(context);
            GoogleApiClientSender.sendMessage(GoogleApiClientSender.WEAR_MESSAGE_PATH, "clicked smartbulletin");
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        Log.d("widget debug", "onAppWidgetOptionsChanged");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        drawWidget(context, appWidgetId);
    }

    private void drawWidget(Context context, int appWidgetId) {
        Log.d("check click", "drawWidget : " + appWidgetId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Resources res = context.getResources();
        Bundle widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
        boolean shortMonthName = false;
        boolean mini = false;
        int numWeeks = 6;
        if (widgetOptions != null) {
            int minWidthDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            int minHeightDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            shortMonthName = minWidthDp <= res.getInteger(R.integer.max_width_short_month_label_dp);
            mini = minHeightDp <= res.getInteger(R.integer.max_height_mini_view_dp);
            if (mini) {
                numWeeks = minHeightDp <= res.getInteger(R.integer.max_height_mini_view_1_row_dp)
                        ? 1 : 2;
            }
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calander_app_widget);

        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_YEAR);
        int todayYear = cal.get(Calendar.YEAR);
        int thisMonth;

        Log.d("check click", "mini : " + mini);

        if (!mini) {
            thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
        } else {
            thisMonth = cal.get(Calendar.MONTH);
        }
        rv.setTextViewText(R.id.month_label, DateFormat.format(
                shortMonthName ? "MMM yy" : "MMMM yyyy", cal));

        if (!mini) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int monthStartDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, 1 - monthStartDayOfWeek);
        } else {
            int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, 1 - todayDayOfWeek);
        }

        rv.removeAllViews(R.id.calendar);

        RemoteViews headerRowRv = new RemoteViews(context.getPackageName(),
                R.layout.row_header);
        DateFormatSymbols dfs = DateFormatSymbols.getInstance();
        String[] weekdays = dfs.getShortWeekdays();
        for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            RemoteViews dayRv = new RemoteViews(context.getPackageName(), R.layout.cell_header);
            dayRv.setTextViewText(android.R.id.text1, weekdays[day]);
            headerRowRv.addView(R.id.row_container, dayRv);
        }
        rv.addView(R.id.calendar, headerRowRv);

        for (int week = 0; week < numWeeks; week++) {
            RemoteViews rowRv = new RemoteViews(context.getPackageName(), R.layout.row_week);
            for (int day = 0; day < 7; day++) {
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean inYear  = cal.get(Calendar.YEAR) == todayYear;
                boolean isToday = inYear && inMonth && (cal.get(Calendar.DAY_OF_YEAR) == today);

                boolean isFirstOfMonth = cal.get(Calendar.DAY_OF_MONTH) == 1;
                int cellLayoutResId = R.layout.cell_day;
                if (isToday) {
                    cellLayoutResId = R.layout.cell_today;
                } else if (inMonth) {
                    cellLayoutResId = R.layout.cell_day_this_month;
                }
                RemoteViews cellRv = new RemoteViews(context.getPackageName(), cellLayoutResId);
                cellRv.setTextViewText(android.R.id.text1,
                        Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
                if (isFirstOfMonth) {
                    cellRv.setTextViewText(R.id.month_label, DateFormat.format("MMM", cal));
                }
                if (isToday) {
                    Log.d("check click", "set click");
                    //cellRv.setOnClickPendingIntent(android.R.id.text1, getPendingIntent(context));
                    cellRv.setOnClickPendingIntent(android.R.id.text1,
                            PendingIntent.getBroadcast(context, 0,
                                    new Intent(context, CalanderAppWidget.class)
                                            .setAction(ACTION_ADD_NOTIFICAION),
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }

                rowRv.addView(R.id.row_container, cellRv);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            rv.addView(R.id.calendar, rowRv);
        }

        rv.setViewVisibility(R.id.prev_month_button, mini ? View.GONE : View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.prev_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalanderAppWidget.class)
                                .setAction(ACTION_PREVIOUS_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        rv.setViewVisibility(R.id.next_month_button, mini ? View.GONE : View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.next_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalanderAppWidget.class)
                                .setAction(ACTION_NEXT_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        rv.setOnClickPendingIntent(R.id.month_label,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, CalanderAppWidget.class)
                                .setAction(ACTION_RESET_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        rv.setViewVisibility(R.id.month_bar, numWeeks <= 1 ? View.GONE : View.VISIBLE);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
}

