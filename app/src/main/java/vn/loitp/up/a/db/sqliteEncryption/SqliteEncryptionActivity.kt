package vn.loitp.up.a.db.sqliteEncryption

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.base.BaseApplication
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.printBeautyJson
import com.loitp.core.ext.setSafeOnClickListenerElastic
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import vn.loitp.R
import vn.loitp.databinding.ADbSqliteEncryptionBinding

/**
 * Created by Loitp on 15.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */

@LogTag("SqliteEncryptionActivity")
@IsFullScreen(false)
class SqliteEncryptionActivity : BaseActivityFont(), View.OnClickListener {
    private lateinit var bikeDatabase: BikeDatabase
    private lateinit var binding: ADbSqliteEncryptionBinding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ADbSqliteEncryptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bikeDatabase = BikeDatabase(this)

        setupView()
        getAllBike()
    }

    private fun setupView() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = SqliteEncryptionActivity::class.java.simpleName
        }
        binding.btAddBike.setOnClickListener(this)
        binding.btClearAll.setOnClickListener(this)
        binding.btGetBikeWithId.setOnClickListener(this)
    }

    private fun showProgress() {
        binding.pb.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.pb.visibility = View.GONE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btAddBike -> {
                addBike()
            }
            R.id.btClearAll -> {
                clearAllBike()
            }
            R.id.btGetBikeWithId -> {
                getBikeWithId(2)
            }
        }
    }

    private fun getAllBike() {
        logD("getAllBike")
        showProgress()
        compositeDisposable.add(
            Single.create<List<Bike>> {
                val bikeList = bikeDatabase.allBike
                if (bikeList.isEmpty()) {
                    it.onError(Throwable("bikeList isNullOrEmpty"))
                } else {
                    it.onSuccess(bikeList)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        logD("getAllBike success " + it.size)
                        for (bike in it) {
                            addButtonByBike(bike)
                        }
                        hideProgress()
                    },
                    {
                        logE("getAllBike failed: $it")
                        hideProgress()
                    }
                )
        )
    }

    private fun addButtonByBike(bike: Bike) {
        addButtonById(bike.id)
    }

    private fun addButtonById(idBike: Long?) {
        val button = Button(this)
        val bike = bikeDatabase.getBike(idBike)
        logD("addButton bike " + BaseApplication.gson.toJson(bike))
        if (bike != null) {
            button.printBeautyJson(bike)
            button.isAllCaps = false
            button.gravity = Gravity.START
            button.setOnClickListener {
                updateBike(bike, button)
            }
            button.setOnLongClickListener {
                deleteBike(bike, button)
                true
            }
            binding.ll.addView(button)
        }
    }

    private fun addBike() {
        showProgress()
        val size = bikeDatabase.bikeCount
        logD("size: $size")
        val bike = Bike()
        bike.name = "GSX " + (size + 1)
        bike.branch = "Suzuki " + (size + 1)
        bike.hp = size
        bike.price = size * 2
        bike.imgPath0 = "path0 " + System.currentTimeMillis()
        bike.imgPath1 = "path1 " + System.currentTimeMillis()
        bike.imgPath2 = "path2 " + System.currentTimeMillis()
        compositeDisposable.add(
            Single.create<Long> {
                val id = bikeDatabase.addBike(bike)
                if (id == BikeDatabase.RESULT_FAILED) {
                    it.onError(Throwable("id == BikeDatabase.RESULT_FAILED"))
                } else {
                    it.onSuccess(id)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { idBike ->
                        if (idBike != BikeDatabase.RESULT_FAILED) {
                            addButtonById(idBike)
                        }
                        hideProgress()
                    },
                    { t ->
                        logE("addBike failed: $t")
                        hideProgress()
                    }
                )
        )
    }

    private fun clearAllBike() {
        logD("clearAllContact")
        binding.ll.removeAllViews()
        bikeDatabase.clearAllBike()
        getAllBike()
    }

    @Suppress("unused")
    private fun getBikeWithId(id: Long) {
        showProgress()
        compositeDisposable.add(
            Single.create<Bike> {
                val bike = bikeDatabase.getBike(id)
                if (bike == null) {
                    it.onError(Throwable("Bike with ID=$id not found"))
                } else {
                    it.onSuccess(bike)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { bike ->
                        showShortInformation("Found: " + BaseApplication.gson.toJson(bike))
                        hideProgress()
                    },
                    { t ->
                        logE("addBike failed: $t")
                        showShortInformation("addBike failed: $t")
                        hideProgress()
                    }
                )
        )
    }

    private fun updateBike(
        bike: Bike,
        button: Button
    ) {
        bike.name = "Monster " + System.currentTimeMillis()
        bike.branch = "Ducati"
        bike.hp = bike.hp?.plus(1)
        bike.price = bike.price?.plus(2)
        showProgress()
        compositeDisposable.add(
            Single.create<Long> {
                val id = bikeDatabase.updateBike(bike)
                if (id == BikeDatabase.RESULT_FAILED) {
                    it.onError(Throwable("updateBike id == BikeDatabase.RESULT_FAILED"))
                } else {
                    it.onSuccess(id)
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        button.printBeautyJson(bike)
                        hideProgress()
                    },
                    { t ->
                        logE("updateBike failed: $t")
                        showShortError("updateBike failed: $t")
                        hideProgress()
                    }
                )
        )
    }

    private fun deleteBike(
        bike: Bike,
        button: Button
    ) {
        val result = bikeDatabase.deleteBike(bike)
        logD("deleteContact result $result")
        binding.ll.removeView(button)
    }
}
