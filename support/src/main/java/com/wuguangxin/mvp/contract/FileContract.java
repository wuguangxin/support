package com.wuguangxin.mvp.contract;

import com.wuguangxin.mvp.IModel;
import com.wuguangxin.mvp.IPresenter;
import com.wuguangxin.mvp.IView;
import com.wuguangxin.mvp.callback.IModelCallback;

/**
 * Created by wuguangxin on 2020-03-19.
 */
public interface FileContract {

    interface Presenter<M extends IModel, V extends IView> extends IPresenter<M, V> {
        // 上传文件
        void uploadFile(String filePath);
        // 删除文件
        void deleteFile(String url);
    }

    interface View extends IView {
        void onUploadProgress(int percent);
        void onSuccessUploadFile(String filePath, String imageUrl);
        void onSuccessDeleteFile(String url);
    }

    interface Model extends IModel {
        void uploadFile(String filePath, Callback listener);
        void deleteFile(String url, Callback listener);
    }

    interface Callback extends IModelCallback {
        void onUploadProgress(int percent);
        void onSuccessUploadFile(String filePath, String imageUrl);
        void onSuccessDeleteFile(String url);
    }

}
