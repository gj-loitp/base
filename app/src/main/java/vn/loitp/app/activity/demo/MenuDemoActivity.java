package vn.loitp.app.activity.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import loitp.basemaster.R;
import vn.loitp.app.activity.demo.alarmdemoapp.activity.AlarmMeActivity;
import vn.loitp.app.activity.demo.gallery.GalleryDemoSplashActivity;
import vn.loitp.app.activity.demo.sound.SoundActivity;
import vn.loitp.app.activity.demo.texttospeech.TextToSpeechActivity;
import vn.loitp.app.activity.demo.video.VideoActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LActivityUtil;

public class MenuDemoActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.bt_alarm).setOnClickListener(this);
        findViewById(R.id.bt_gallery).setOnClickListener(this);
        findViewById(R.id.bt_video).setOnClickListener(this);
        findViewById(R.id.bt_sound).setOnClickListener(this);
        findViewById(R.id.bt_text_to_speech).setOnClickListener(this);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected Activity setActivity() {
        return this;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_menu_demo;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_alarm:
                intent = new Intent(activity, AlarmMeActivity.class);
                break;
            case R.id.bt_gallery:
                intent = new Intent(activity, GalleryDemoSplashActivity.class);
                break;
            case R.id.bt_video:
                intent = new Intent(activity, VideoActivity.class);
                break;
            case R.id.bt_sound:
                intent = new Intent(activity, SoundActivity.class);
                break;
            case R.id.bt_text_to_speech:
                intent = new Intent(activity, TextToSpeechActivity.class);
                break;

        }
        if (intent != null) {
            startActivity(intent);
            LActivityUtil.tranIn(activity);
        }
    }
}
