package com.wuguangxin.mvp.contract

import com.wuguangxin.mvp.IModel
import com.wuguangxin.mvp.IPresenter
import com.wuguangxin.mvp.IView
import com.wuguangxin.mvp.callback.IModelCallback

/**
 * Created by wuguangxin on 2020-03-19.
 */
interface FileContract {
    interface Presenter<M : IModel, V : IView> : IPresenter<M, V> {
        // 上传文件
        fun uploadFile(filePath: String?)

        // 删除文件
        fun deleteFile(url: String?)
    }

    interface View : IView {
        fun onUploadProgress(percent: Int)
        fun onSuccessUploadFile(filePath: String?, imageUrl: String?)
        fun onSuccessDeleteFile(url: String?)
    }

    interface Model : IModel {
        fun uploadFile(filePath: String?, callback: Callback?)
        fun deleteFile(url: String?, callback: Callback?)
    }

    interface Callback : IModelCallback<Any?> {
        fun onUploadProgress(percent: Int)
        fun onSuccessUploadFile(filePath: String?, imageUrl: String?)
        fun onSuccessDeleteFile(url: String?)
    }
}