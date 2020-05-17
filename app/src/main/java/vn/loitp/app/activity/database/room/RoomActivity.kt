package vn.loitp.app.activity.database.room

import android.os.Bundle
import androidx.lifecycle.Observer
import com.core.base.BaseFontActivity
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_database_room2.*
import vn.loitp.app.R
import vn.loitp.app.app.LApplication

class RoomActivity : BaseFontActivity() {

    private var homeViewModel: HomeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModels()
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String? {
        return "loitpp" + javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_database_room2
    }

    private fun setupView() {
        btSaveList.setSafeOnClickListener {
            handleSaveList()
        }
        btGetList.setSafeOnClickListener {
            handleGetList()
        }
    }

    private fun setupViewModels() {
        homeViewModel = getViewModel(HomeViewModel::class.java)
        homeViewModel?.let { hvm ->
            hvm.saveFloorPlanActionLiveData.observe(this, Observer { actionData ->
                val data = actionData.data
                data?.let {
                    logD("floorPlanActionLiveData observe " + LApplication.gson.toJson(it))
                }
            })

            hvm.getFloorPlanActionLiveData.observe(this, Observer { actionData ->
                val data = actionData.data
                data?.let {
                    logD("getFloorPlanActionLiveData observe " + LApplication.gson.toJson(it))
                }
            })
        }
    }

    private fun handleSaveList() {
        homeViewModel?.saveList()
    }

    private fun handleGetList() {
        homeViewModel?.getList()
    }

}
