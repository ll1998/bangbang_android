package com.autowheel.bangbang.utils

import android.os.Handler
import android.os.Looper
import com.autowheel.bangbang.MyApplication
import es.dmoral.toasty.Toasty

/**
 * Created by Xily on 2019/4/11.
 */
fun toastSuccess(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.success(MyApplication.getInstance(), message).show()
    }
}

fun toastInfo(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.info(MyApplication.getInstance(), message).show()
    }
}

fun toastWarning(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.warning(MyApplication.getInstance(), message).show()
    }
}

fun toastError(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.error(MyApplication.getInstance(), message).show()
    }
}