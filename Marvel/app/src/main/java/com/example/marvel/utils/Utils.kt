package com.example.marvel.utils

import android.R
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest


fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(Charsets.UTF_8))
fun ByteArray.toHex() = joinToString("") { "%02x".format(it) }
fun ts(): String = System.currentTimeMillis().toString()

fun View.showSnackBar(text: String, actionText: String, listener: View.OnClickListener){
    Snackbar.make(this, text, Snackbar.LENGTH_LONG)
        .setAction(actionText, listener)
        .show()
}