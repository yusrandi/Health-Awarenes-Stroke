package com.use.steps.utils

import android.content.Context
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class  MessageHandler (cx: Context){

    private var ctx: Context = cx;
    private lateinit var loadingDialog: SweetAlertDialog
    private lateinit var errorDialog:SweetAlertDialog

    fun closeAlert(){
        loadingDialog .dismiss()
    }
    fun alertError(msg: String){
        errorDialog = SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
        errorDialog.titleText = "Terjadi Kesalahan !"
        errorDialog.contentText = msg
        errorDialog.setCancelable(false)
        errorDialog.setConfirmClickListener { errorDialog.dismissWithAnimation() }
        errorDialog.show()
    }
    fun alertLoading(){
        loadingDialog = SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE)
        loadingDialog.progressHelper.barColor = Color.parseColor("#FF5B5B")
        loadingDialog.titleText = "Loading"
        loadingDialog.setCancelable(true)
        loadingDialog.show()
    }

    interface MsgHandler{
        fun alertWarning(msg: String)
        fun alertSuccess(msg: String)
    }



}