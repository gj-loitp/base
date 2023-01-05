package vn.loitp.a.tut.retrofit2

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.base.BaseApplication
import com.loitp.core.utilities.LUIUtil
import com.loitp.restApi.restClient.RestClient2
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.a_retrofit_2.*
import vn.loitp.R

// https://code.tutsplus.com/tutorials/connect-to-an-api-with-retrofit-rxjava-2-and-kotlin--cms-32133

@LogTag("Retrofit2Activity")
@IsFullScreen(false)
class Retrofit2ActivityFont : BaseActivityFont(), Retrofit2Adapter.Listener {
    private var retrofit2Adapter: Retrofit2Adapter? = null
    private var retroCryptoArrayList = ArrayList<RetroCrypto>()
    private val baseURL = "https://api.nomics.com/v1/"
    private lateinit var sampleService: SampleService

    override fun setLayoutResourceId(): Int {
        return R.layout.a_retrofit_2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RestClient2.init(baseURL)
        sampleService = RestClient2.createService(SampleService::class.java)
        setupViews()
        loadData()
    }

    private fun setupViews() {
        lActionBar.apply {
            LUIUtil.setSafeOnClickListenerElastic(
                view = this.ivIconLeft,
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = Retrofit2ActivityFont::class.java.simpleName
        }
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
    }

    private fun loadData() {
        logD("loadData")
        pb.visibility = View.VISIBLE
        compositeDisposable.add(
            sampleService.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    logD("loadData success " + BaseApplication.gson.toJson(it))
                    retroCryptoArrayList.clear()
                    retroCryptoArrayList.addAll(it)
                    retrofit2Adapter =
                        Retrofit2Adapter(cryptoList = retroCryptoArrayList, listener = this)
                    rv.adapter = retrofit2Adapter
                    pb.visibility = View.GONE
                }, {
                    logE("loadData error $it")
                    showShortError(it.toString())
                    pb.visibility = View.GONE
                })
        )
    }

    override fun onItemClick(retroCrypto: RetroCrypto) {
        showShortInformation("You clicked: ${retroCrypto.currency}")
    }
}