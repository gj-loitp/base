package vn.loitp.app.activity.customviews.textview.verticalmarqueetextview;

import android.os.Bundle;

import loitp.basemaster.R;
import vn.loitp.app.activity.BaseFontActivity;

public class VerticalMarqueeTextViewActivity extends BaseFontActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    protected int setLayoutResourceId() {
        return R.layout.activity_vertical_marque_textview;
    }

}
