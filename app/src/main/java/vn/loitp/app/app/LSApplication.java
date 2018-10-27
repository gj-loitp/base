package vn.loitp.app.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import loitp.basemaster.R;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.data.ActivityData;
import vn.loitp.data.AdmobData;
import vn.loitp.utils.util.Utils;

//TODO is debug
//TODO Demo -> Android floating video
//TODO Demo -> Video
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
        Constants.setIsDebug(true);
        Utils.init(this);
        //config admob id
        AdmobData.getInstance().setIdAdmobFull(getString(R.string.str_f));
        //config activity transition default
        ActivityData.getInstance().setType(Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT);

        //config realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //config font
        LUIUtil.setFontForAll(vn.loitp.core.common.Constants.FONT_PATH);

        //fcm
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC);

        //big imageview
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
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
