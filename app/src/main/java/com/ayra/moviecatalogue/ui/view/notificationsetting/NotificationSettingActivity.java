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
import com.ayra.moviecatalogue.notification.NewReleaseNotification;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationSettingActivity extends AppCompatActivity {

    @BindView(R.id.switch_reminder)
    SwitchCompat reminderSwitch;
    @BindView(R.id.switch_release)
    SwitchCompat releaseSwitch;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private NewReleaseNotification newReleaseNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        newReleaseNotification = new NewReleaseNotification(this.getApplicationContext());
        String SHARED_PREF = "shared_preferences";
        sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        ButterKnife.bind(this);

        // Set Action Bar
        setUpActionBar();

        // Check User Switch Compat Condition
        checkSwitch();

        // Listener On Switch
        switchListener();

    }

    private void setUpActionBar() {
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void checkSwitch() {
        boolean reminderCondition = sharedPreferences.getBoolean("daily_reminder", false);
        boolean releaseCondition = sharedPreferences.getBoolean("release_reminder", false);
        reminderSwitch.setChecked(reminderCondition);
        if (!reminderCondition) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("reminder");
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("reminder");
        }
        releaseSwitch.setChecked(releaseCondition);
        if (!releaseCondition) {
            newReleaseNotification.cancelReleaseNotification(getApplicationContext());
        } else {
            newReleaseNotification.setUpRelease();
        }
    }

    private void switchListener() {
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
        releaseSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            sharedEditor = sharedPreferences.edit();
            sharedEditor.putBoolean("release_reminder", isChecked);
            sharedEditor.apply();
            if (isChecked) {
                newReleaseNotification.setUpRelease();
                String titleMsg = getString(R.string.notification);
                String msg = getString(R.string.on);
                Toast.makeText(NotificationSettingActivity.this,
                        titleMsg + " : " + msg,
                        Toast.LENGTH_SHORT).show();
            } else {
                newReleaseNotification.cancelReleaseNotification(getApplicationContext());
                String titleMsg = getString(R.string.notification);
                String msg = getString(R.string.off);
                Toast.makeText(NotificationSettingActivity.this,
                        titleMsg + " : " + msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
