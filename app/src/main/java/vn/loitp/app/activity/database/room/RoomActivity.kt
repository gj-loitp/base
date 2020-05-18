package vn.loitp.app.activity.database.room

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.base.BaseFontActivity
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_database_room2.*
import vn.loitp.app.R
import vn.loitp.app.app.LApplication

class RoomActivity : BaseFontActivity() {
    private var floorPlanAdapter: FloorPlanAdapter? = null
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
        floorPlanAdapter = FloorPlanAdapter()
        floorPlanAdapter?.apply {
            onClickRootView = {

            }
            onClickUpDate = {

            }
            onClickDelete = {
                
            }
        }
        rvFloorPlan.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        rvFloorPlan.layoutManager = LinearLayoutManager(activity)
        rvFloorPlan.adapter = floorPlanAdapter

        btSaveListFrom0To10.setSafeOnClickListener {
            handleSaveListFrom0To10()
        }
        btSaveListFrom10To20.setSafeOnClickListener {
            handleSaveListFrom10To20()
        }
        btGetList.setSafeOnClickListener {
            handleGetList()
        }
    }

    private fun setupViewModels() {
        homeViewModel = getViewModel(HomeViewModel::class.java)
        homeViewModel?.let { hvm ->
            hvm.saveFloorPlanActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    logD("floorPlanActionLiveData observe " + LApplication.gson.toJson(it))

                }
            })

            hvm.getFloorPlanActionLiveData.observe(this, Observer { actionData ->
                actionData.isDoing?.let {
                    if (it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }
                actionData.data?.let {
                    logD("getFloorPlanActionLiveData observe " + LApplication.gson.toJson(it))
                    floorPlanAdapter?.setListFloorPlan(it)
                }
            })
        }
    }

    private fun handleSaveListFrom0To10() {
        homeViewModel?.saveListFrom0To10()
    }

    private fun handleSaveListFrom10To20() {
        homeViewModel?.saveListFrom10To20()
    }

    private fun handleGetList() {
        homeViewModel?.getList()
    }

}
