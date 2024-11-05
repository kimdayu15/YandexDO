package com.gems.yandexdo

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences {

    companion object {
        private var INSTANCE: SharedPreferences? = null


        fun getInstance(context: Context): SharedPreferences {
            if (INSTANCE == null) {
                INSTANCE = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
            }
            return INSTANCE!!
        }
    }

}
