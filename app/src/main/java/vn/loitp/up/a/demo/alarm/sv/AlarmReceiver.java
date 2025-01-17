package vn.loitp.up.a.demo.alarm.sv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import vn.loitp.up.a.demo.alarm.a.AlarmNotification;
import vn.loitp.up.a.demo.alarm.md.Alarm;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, AlarmNotification.class);
        Alarm alarm = new Alarm();

        alarm.fromIntent(intent);
        alarm.toIntent(newIntent);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(newIntent);
    }
}
