package vn.loitp.app.activity.customviews.layout.constraintlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import loitp.basemaster.R;
import vn.loitp.app.activity.BaseFontActivity;

public class ConstraintlayoutActivity extends BaseFontActivity {
    private Button button;
    private Button bt0;
    private Button bt1;
    private Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraintlayout);
        button = (Button) findViewById(R.id.button);
        bt0 = (Button) findViewById(R.id.bt_0);
        bt1 = (Button) findViewById(R.id.bt_1);
        bt2 = (Button) findViewById(R.id.bt_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);
            }
        });
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt2.setVisibility(View.GONE);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt2.setVisibility(View.VISIBLE);
            }
        });
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
        return R.layout.activity_constraintlayout;
    }
}
