package vn.loitp.up.a.demo.twoInstanceActivity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.common.NOT_FOUND
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import com.loitp.core.ext.tranIn
import vn.loitp.R
import vn.loitp.databinding.A3Binding

@LogTag("Activity3")
@IsFullScreen(false)
class Activity3 : BaseActivityFont() {

    private lateinit var binding: A3Binding

//    override fun setLayoutResourceId(): Int {
//        return NOT_FOUND
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = A3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(runnable = {
                onBaseBackPressed()
            })
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = Activity3::class.java.simpleName
        }
        binding.btGoTo1.setSafeOnClickListener {
            val intent = Intent(this, Activity1::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            this.tranIn()
        }
        binding.btGoTo2.setSafeOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            this.tranIn()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        logD("onNewIntent")
        if (intent.flags or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT > 0) {
            mIsRestoredToTop = true
        }
    }

    private var mIsRestoredToTop = false
    override fun finish() {
        super.finish()//correct
        // 4.4.2 platform issues for FLAG_ACTIVITY_REORDER_TO_FRONT,
        // reordered activity back press will go to home unexpectly,
        // Workaround: move reordered activity current task to front when it's finished.
        val tasksManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        tasksManager.moveTaskToFront(taskId, ActivityManager.MOVE_TASK_NO_USER_ACTION)
    }

    override fun onDestroy() {
        logD("onDestroy")
        super.onDestroy()
    }
}
