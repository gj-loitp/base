package vn.loitp.app.app

import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.common.Constants
import com.core.helper.ttt.db.TTTDatabase
import com.core.utilities.LUIUtil
import com.data.ActivityData
import com.data.AdmobData
import io.realm.Realm
import io.realm.RealmConfiguration
import vn.loitp.app.R
import vn.loitp.app.activity.database.room.db.FNBDatabase

// build release de check
// TODO service -> ko stop service dc

// GIT
// combine 2 commit gan nhat lam 1, co thay doi tren github
/*git reset --soft HEAD~2
git push -f*/

// activity transaction reveal khi finish screen co cai effect sai

@LogTag("LApplication")
class LApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        // config admob id
        AdmobData.instance.idAdmobFull = getString(R.string.str_f)

        // config activity transition default
        ActivityData.instance.type = Constants.TYPE_ACTIVITY_TRANSITION_SLIDELEFT

        // config realm
        val realmConfiguration = RealmConfiguration.Builder(this)
            .name(Realm.DEFAULT_REALM_NAME)
            .schemaVersion(0)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(realmConfiguration)

        // config font
        LUIUtil.fontForAll = Constants.FONT_PATH

        //TODO room
        // room database
        FNBDatabase.getInstance(this)

        // ttt database
//        TTTDatabase.getInstance(this)
    }
}
