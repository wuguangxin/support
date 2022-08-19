package com.wuguangxin.mvp.contract

/**
 * 文件M实现
 * Created by wuguangxin on 2020-03-19.
 */
class FileModel : FileContract.Model {
    override fun uploadFile(filePath: String?, callback: FileContract.Callback?) {
        // request..
    }

    override fun deleteFile(url: String?, callback: FileContract.Callback?) {
        // request..
    }
}