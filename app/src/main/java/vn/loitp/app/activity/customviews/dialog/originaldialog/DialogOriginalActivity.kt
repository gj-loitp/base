package vn.loitp.app.activity.customviews.dialog.originaldialog

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener

import loitp.basemaster.R
import vn.loitp.core.base.BaseFontActivity
import vn.loitp.core.utilities.LDialogUtil
import vn.loitp.utils.util.ToastUtils

class DialogOriginalActivity : BaseFontActivity(), OnClickListener {
    private var testRun: TestRun? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.bt_show_1).setOnClickListener(this)
        findViewById<View>(R.id.bt_show_2).setOnClickListener(this)
        findViewById<View>(R.id.bt_show_3).setOnClickListener(this)
        findViewById<View>(R.id.bt_show_list).setOnClickListener(this)
        findViewById<View>(R.id.bt_progress_dialog).setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        testRun?.cancel(true)
    }

    override fun setFullScreen(): Boolean {
        return false
    }

    override fun setTag(): String {
        return javaClass.simpleName
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_dialog_original
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_show_1 -> show1()
            R.id.bt_show_2 -> show2()
            R.id.bt_show_3 -> show3()
            R.id.bt_show_list -> showList()
            R.id.bt_progress_dialog -> showProgress()
        }
    }

    private fun show1() {
        LDialogUtil.showDialog1(activity, "Title", "Msg", "Button 1") { ToastUtils.showShort("Click 1") }
    }

    private fun show2() {
        LDialogUtil.showDialog2(activity, "Title", "Msg", "Button 1", "Button 2", object : LDialogUtil.Callback2 {
            override fun onClick1() {
                ToastUtils.showShort("Click 1")
            }

            override fun onClick2() {
                ToastUtils.showShort("Click 2")
            }
        })
    }

    private fun show3() {
        LDialogUtil.showDialog3(activity, "Title", "Msg", "Button 1", "Button 2", "Button 3", object : LDialogUtil.Callback3 {
            override fun onClick1() {
                ToastUtils.showShort("Click 1")
            }

            override fun onClick2() {
                ToastUtils.showShort("Click 2")
            }

            override fun onClick3() {
                ToastUtils.showShort("Click 3")
            }
        })
    }

    private fun showList() {
        val size = 50
        val arr = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            arr[i] = "Item $i"
        }
        LDialogUtil.showDialogList(activity, "Title", arr) { position -> ToastUtils.showShort("Click position " + position + ", item: " + arr[position]) }
    }

    private fun showProgress() {
        testRun?.cancel(true)
        testRun = TestRun(activity)
        testRun?.execute()
    }

    private class TestRun internal constructor(private val context: Context) : AsyncTask<Void, Int, Void>() {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = LDialogUtil.showProgressDialog(context, 100, "Title",
                    "Message", false, ProgressDialog.STYLE_HORIZONTAL, null, null)
        }

        override fun doInBackground(vararg voids: Void): Void? {
            progressDialog?.max?.let {
                for (i in 0 until it) {
                    publishProgress(i)
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            values[0]?.let {
                progressDialog?.progress = it
            }
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progressDialog?.dismiss()
        }
    }
}
