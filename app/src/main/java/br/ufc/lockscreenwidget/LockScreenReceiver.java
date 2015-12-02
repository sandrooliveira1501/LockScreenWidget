package br.ufc.lockscreenwidget;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver {
    public LockScreenReceiver() {
    }

    DevicePolicyManager mDevicePolicyManager;
    ComponentName mDevicePolicyAdmin;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Log.i("SCREEN_OFF", "Screen OFF");

            mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            mDevicePolicyAdmin = new ComponentName(context,
                    LockScreenWidget.MyDevicePolicyReceiver.class);

            if(mDevicePolicyManager.isAdminActive(mDevicePolicyAdmin)){

                SharedPreferences sharedPreferences = context.getSharedPreferences("LOCK_SCREEN", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Log.i("PASSWORD", sharedPreferences.getBoolean("RESET_PASSWORD", false) + "");
                if(sharedPreferences.getBoolean("RESET_PASSWORD", false) == false){
                    editor.putBoolean("RESET_PASSWORD", true);
                    editor.commit();
                }else{
                    Log.i("PASSWORD", "RESET PASSWORD");
                    mDevicePolicyManager.resetPassword("",DevicePolicyManager.RESET_PASSWORD_DO_NOT_ASK_CREDENTIALS_ON_BOOT);
                    editor.putBoolean("RESET_PASSWORD", false);
                    editor.commit();
                }
            }
        }
    }
}
