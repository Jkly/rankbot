package jkly.extension

import com.google.gson.Gson


inline fun <reified T: Any> String.toObject(gson: Gson): T = gson.fromJson(this, T::class.java)
