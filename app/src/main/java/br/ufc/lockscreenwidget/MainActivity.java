package br.ufc.lockscreenwidget;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE = 1001;
    DevicePolicyManager mDevicePolicyManager;
    ComponentName mDevicePolicyAdmin;
    public final static String INIT = "br.ufc.lockscreen.init";

    private static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*LockScreenReceiver receiver = new LockScreenReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));*/

        mDevicePolicyManager = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDevicePolicyAdmin = new ComponentName(this,
                LockScreenWidget.MyDevicePolicyReceiver.class);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(
                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDevicePolicyAdmin);
        intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.device_admin_description));
        startActivityForResult(intent, REQUEST_ENABLE);


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals(PASSWORD)){
                    String password = sharedPreferences.getString(PASSWORD, "");
                    if(password.length() < 4){
                        ((TextView)findViewById(R.id.tvPassword)).setText(R.string.define_password);
                    }else{
                        ((TextView)findViewById(R.id.tvPassword)).setText("");
                    }
                }

            }
        });
        String password = sp.getString(PASSWORD, "");
        if(password.length() < 4){
            ((TextView)findViewById(R.id.tvPassword)).setText(R.string.define_password);
        }else{
            ((TextView)findViewById(R.id.tvPassword)).setText("");
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Configurações Estabelecidas", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
