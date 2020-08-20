package com.wuguangxin.mvp.contract;

import com.wuguangxin.mvp.BaseModel;
import com.wuguangxin.mvp.BasePresenter;
import com.wuguangxin.mvp.BaseView;
import com.wuguangxin.mvp.callback.BaseModelCallback;

/**
 * Created by wuguangxin on 2020-03-19.
 */
public interface FileContract {

    interface Presenter<M extends BaseModel, V extends BaseView> extends BasePresenter<M, V> {
        // 上传文件
        void uploadFile(String filePath);
        // 删除文件
        void deleteFile(String url);
    }

    interface View extends BaseView {
        void onUploadProgress(int percent);
        void onSuccessUploadFile(String filePath, String imageUrl);
        void onSuccessDeleteFile(String url);
    }

    interface Model extends BaseModel {
        void uploadFile(String filePath, Callback listener);
        void deleteFile(String url, Callback listener);
    }

    interface Callback extends BaseModelCallback {
        void onUploadProgress(int percent);
        void onSuccessUploadFile(String filePath, String imageUrl);
        void onSuccessDeleteFile(String url);
    }

}
