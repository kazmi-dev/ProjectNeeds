package com.cooptech.pdfreader.util.genericDialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

class GenericDialogView(
    context: Context,
    layoutInflater:(LayoutInflater, ViewGroup?, Boolean)-> ViewBinding,
    bindView:(ViewBinding, AlertDialog)-> Unit
): AlertDialog(context) {

    init {
        val binding = layoutInflater.invoke(LayoutInflater.from(context), null, false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setView(binding.root)
        bindView(binding, this)
    }
}