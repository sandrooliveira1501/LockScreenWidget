package br.ufc.lockscreenwidget;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class LockScreenWidget extends AppWidgetProvider {

    private static final String INTENT_LOCK_SCREEN= "INTENT_LOCK_SCREEN";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lock_screen_widget);

        views.setOnClickPendingIntent(R.id.lockScreenButton, getPendingSelfIntent(context, INTENT_LOCK_SCREEN));


        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    DevicePolicyManager mDevicePolicyManager;
    ComponentName mDevicePolicyAdmin;

    private static final String PASSWORD = "password";
    private static final String ASK_PASSWORD = "ask_password";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (INTENT_LOCK_SCREEN.equals(intent.getAction())) {

            mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDevicePolicyAdmin = new ComponentName(context,
                    MyDevicePolicyReceiver.class);

            if(mDevicePolicyManager.isAdminActive(mDevicePolicyAdmin)){
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String password = sp.getString(PASSWORD, "");
                boolean ask_password = sp.getBoolean(ASK_PASSWORD, false);
                Log.i(ASK_PASSWORD, ask_password + " - " + password);
                if(password.length() >= 4 && ask_password){
                    mDevicePolicyManager.resetPassword(password, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
                }
                mDevicePolicyManager.lockNow();
            }

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static class MyDevicePolicyReceiver extends DeviceAdminReceiver {

        private static final String LOG_TAG = "LOG_TAG";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(LOG_TAG,
                    "MyDevicePolicyReciever Received: " + intent.getAction());
            super.onReceive(context, intent);
        }

        @Override
        public void onPasswordSucceeded(Context context, Intent intent) {
            super.onPasswordSucceeded(context, intent);
            Log.i("PASSWORDSUCCEEDED", "Limpar password");
            DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDevicePolicyManager.resetPassword("",DevicePolicyManager.RESET_PASSWORD_DO_NOT_ASK_CREDENTIALS_ON_BOOT);
        }
    }

}


