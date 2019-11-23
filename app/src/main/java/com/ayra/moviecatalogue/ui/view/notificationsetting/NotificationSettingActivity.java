package com.ayra.moviecatalogue.ui.view.notificationsetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.ayra.moviecatalogue.R;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationSettingActivity extends AppCompatActivity {

    @BindView(R.id.switch_reminder)
    SwitchCompat reminderSwitch;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        String SHARED_PREF = "reminder";
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        ButterKnife.bind(this);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        checkSwitch();

        reminderSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            sharedEditor = sharedPreferences.edit();
            sharedEditor.putBoolean("daily_reminder", isChecked);
            sharedEditor.apply();
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic("reminder");
                String titleMsg = getString(R.string.notification);
                String msg = getString(R.string.on);
                Toast.makeText(NotificationSettingActivity.this,
                        titleMsg + " : " + msg,
                        Toast.LENGTH_SHORT).show();
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("reminder");
                String titleMsg = getString(R.string.notification);
                String msg = getString(R.string.off);
                Toast.makeText(NotificationSettingActivity.this,
                        titleMsg + " : " + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkSwitch() {
        boolean reminderCondition = sharedPreferences.getBoolean("daily_reminder", false);
        reminderSwitch.setChecked(reminderCondition);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
