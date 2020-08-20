package com.wuguangxin.mvp.contract;

/**
 * 文件M实现
 * Created by wuguangxin on 2020-03-19.
 */
public class FileModel implements FileContract.Model {

    @Override
    public void uploadFile(String filePath, FileContract.Callback callback) {
        // http request..
    }

    @Override
    public void deleteFile(String fileUrl, FileContract.Callback callback) {
        // http request..
    }
}
