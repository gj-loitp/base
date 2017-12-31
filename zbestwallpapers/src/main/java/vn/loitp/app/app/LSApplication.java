package vn.loitp.app.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import loitp.basemaster.R;
import vn.loitp.data.AdmobData;
import vn.loitp.utils.util.Utils;

public class LSApplication extends MultiDexApplication {
    private final String TAG = LSApplication.class.getSimpleName();
    private static LSApplication instance;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (gson == null) {
            gson = new Gson();
        }
        Utils.init(this);
        AdmobData.getInstance().setIdAdmobFull(getString(R.string.str_f));
        Fresco.initialize(getApplicationContext());
    }

    public Gson getGson() {
        return gson;
    }

    public static LSApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
