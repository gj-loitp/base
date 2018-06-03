package vn.loitp.app.activity.customviews.textview.colortextview;

import android.os.Bundle;

import loitp.basemaster.R;
import vn.loitp.app.activity.BaseFontActivity;

public class ColorTextViewActivity extends BaseFontActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //can set color in xml or java code
        /*((ColorTextView) findViewById(R.id.ctv_text))
                .findAndSetStrColor("after", "#ff8000")
                .findAndSetStrColor("and", "#008888");*/
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
        return R.layout.activity_color_textview;
    }

}
