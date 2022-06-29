package com.loitpcore.core.helper.ttt.model.chap

import androidx.annotation.Keep
import com.loitpcore.core.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
class Chaps : BaseModel() {
    @SerializedName("chap")
    @Expose
    var chap: List<Chap> = ArrayList()
}