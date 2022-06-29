package com.loitpcore.restapi.flickr.model.photosetgetlist

import androidx.annotation.Keep
import com.loitpcore.core.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
class Description : BaseModel() {
    @SerializedName("_content")
    @Expose
    var content: String? = null
}