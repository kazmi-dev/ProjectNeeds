package com.capra.pdfreader.pdfviewer.alldocuments.wordpptexel.filesreader.freeapp.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

class CustomToast {
    companion object {

        private var toast: Toast? = null

        private fun Context.shortToast(msg: String) {
            toast?.cancel()
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            toast?.show()
        }

        private fun Context.longToast(msg: String) {
            toast?.cancel()
            toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
            toast?.show()
        }

        fun Fragment.shortToast(msg: String) {
            try {
                requireContext().shortToast(msg)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun Fragment.longToast(msg: String) {
            try {
                requireContext().longToast(msg)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}