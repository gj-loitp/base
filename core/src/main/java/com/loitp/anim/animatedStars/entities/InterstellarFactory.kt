package com.loitp.anim.animatedStars.entities

import com.loitp.anim.animatedStars.entities.stars.BaseStar
import com.loitp.anim.animatedStars.entities.stars.RoundyStar
import com.loitp.anim.animatedStars.entities.stars.ShinyStar
import com.loitp.anim.animatedStars.entities.stars.TinyStar

object InterstellarFactory {

    fun create(
        starConstraints: StarConstraints,
        x: Int,
        y: Int,
        color: Int,
        listener: BaseStar.StarCompleteListener
    ): Star {
        val starSize = starConstraints.getRandomStarSize()

        return if (starSize >= starConstraints.bigStarThreshold) {

            // big star ones randomly be Shiny or Round
            if (Math.random() < 0.7) {
                ShinyStar(starConstraints, x, y, color, listener)
            } else {
                RoundyStar(starConstraints, x, y, color, listener)
            }
        } else {
            // others will ne tiny
            TinyStar(starConstraints, x, y, color, listener)
        }
    }

}