package vn.loitp.a.demo.architectureComponent.room

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loitp.annotation.IsFullScreen
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.base.BaseApplication
import com.loitp.core.ext.setSafeOnClickListener
import com.loitp.core.ext.setSafeOnClickListenerElastic
import kotlinx.android.synthetic.main.a_demo_database_room_work.*
import vn.loitp.R
import vn.loitp.a.demo.architectureComponent.room.md.Word
import vn.loitp.a.demo.architectureComponent.room.md.WordViewModel

// https://codinginfinite.com/android-room-tutorial-persistence/
// https://codinginfinite.com/android-room-persistent-rxjava/
// https://codinginfinite.com/android-room-persistence-livedata-example/

@LogTag("WordActivity")
@IsFullScreen(false)
class WordActivityFont : BaseActivityFont() {
    private var wordViewModel: WordViewModel? = null
    private var wordListAdapter: WordListAdapter? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.a_demo_database_room_work
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
        setupViewModels()
    }

    private fun setupViews() {
        lActionBar.apply {
            this.ivIconLeft.setSafeOnClickListenerElastic(
                runnable = {
                    onBaseBackPressed()
                }
            )
            this.ivIconRight?.setImageResource(R.color.transparent)
            this.tvTitle?.text = WordActivityFont::class.java.simpleName
        }
        wordListAdapter = WordListAdapter(object : WordListAdapter.Callback {
            override fun onUpdate(word: Word) {
                handleUpdate(word = word)
            }

            override fun onDelete(word: Word) {
                handleDelete(word)
            }
        })
        recyclerView.adapter = wordListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        btAdd.setSafeOnClickListener {
            handleInsert()
        }
        btDeleteAll.setSafeOnClickListener {
            handleDeleteAll()
        }
        btFindWord.setSafeOnClickListener {
            handleFindWord()
        }
    }

    private fun setupViewModels() {
        wordViewModel = ViewModelProvider(this)[WordViewModel::class.java]
        wordViewModel?.let { vm ->
            vm.listWord?.observe(
                this
            ) { allWords ->
                logD("allWords observe " + BaseApplication.gson.toJson(allWords))
                allWords?.let {
                    wordListAdapter?.setWords(it)
                }
                genFirstData()
            }
            vm.wordFind?.observe(
                this
            ) {
                logD("wordFind observe " + BaseApplication.gson.toJson(it))
            }
        }
    }

    private fun handleUpdate(word: Word) {
        word.word = "Update " + System.currentTimeMillis()
        wordViewModel?.update(word)
    }

    private fun handleDelete(word: Word) {
        wordViewModel?.delete(word)
    }

    private fun handleDeleteAll() {
        wordViewModel?.deleteAll()
    }

    private fun handleInsert() {
        val word = Word()
        word.word = "Add " + System.currentTimeMillis()
        wordViewModel?.insert(word)
    }

    private var isGenFirstDataDone = false
    private fun genFirstData() {
        if (!isGenFirstDataDone) {
            showShortInformation("genFirstData")
            wordViewModel?.genFirstData()
            isGenFirstDataDone = true
        }
    }

    private fun handleFindWord() {
        wordViewModel?.findWord(id = "1")
    }
}
