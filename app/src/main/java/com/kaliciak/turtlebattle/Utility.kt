package com.kaliciak.turtlebattle

import android.content.res.Resources

class Utility {
    companion object {
        fun getSystemWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun getSystemHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }
    }
}