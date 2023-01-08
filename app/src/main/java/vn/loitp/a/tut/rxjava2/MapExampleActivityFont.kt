package vn.loitp.a.tut.rxjava2

import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.a_rxjava2_flowable.*
import vn.loitp.R
import vn.loitp.a.tut.rxjava2.md.ApiUser
import vn.loitp.a.tut.rxjava2.md.User
import vn.loitp.a.tut.rxjava2.u.RxJavaUtils.Companion.apiUserList
import vn.loitp.a.tut.rxjava2.u.RxJavaUtils.Companion.convertApiUserListToUserList

// https://github.com/amitshekhariitbhu/RxJava2-Android-Samples
@LogTag("MapExampleActivity")
@IsFullScreen(false)
class MapExampleActivityFont : BaseActivityFont() {

    override fun setLayoutResourceId(): Int {
        return R.layout.a_rxjava2_flowable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = MapExampleActivityFont::class.java.simpleName
        }
        btn.setSafeOnClickListener {
            doSomeWork()
        }
    }

    /*
     * Here we are getting ApiUser Object from api server
     * then we are converting it into User Object because
     * may be our database support User Not ApiUser Object
     * Here we are using Map Operator to do that
     */
    private fun doSomeWork() {
        observable
            .subscribeOn(Schedulers.io()) // Be notified on the main thread
            .observeOn(AndroidSchedulers.mainThread())
            .map { apiUsers ->
                convertApiUserListToUserList(apiUsers)
            }
            .subscribe(observer)
    }

    private val observable: Observable<List<ApiUser>>
        get() = Observable.create { listObservableEmitter: ObservableEmitter<List<ApiUser>> ->
            if (!listObservableEmitter.isDisposed) {
                listObservableEmitter.onNext(apiUserList)
                listObservableEmitter.onComplete()
            }
        }

    private val observer: Observer<List<User>>
        get() = object : Observer<List<User>> {
            override fun onSubscribe(d: Disposable) {
                logD("onSubscribe : " + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
                textView.append("\nonNext")
                for (user in userList) {
                    textView.append("\nfirstname : ${user.firstname}")
                }
            }

            override fun onError(e: Throwable) {
                textView.append("\nonError : ${e.message}")
            }

            override fun onComplete() {
                textView.append("\nonComplete")
            }
        }
}
