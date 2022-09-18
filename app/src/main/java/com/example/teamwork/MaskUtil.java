package com.example.teamwork;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

public class MaskUtil {
    public static ProgressDialog showProgressDialog(String message, AppCompatActivity mActivity) {
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }
    public static ProgressDialog firshowProgressDialog(String message, firstFragment mActivity){
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity.getContext());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }
    public static ProgressDialog secshowProgressDialog(String message, FragmentSecond mActivity){
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity.getContext());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }
    public static ProgressDialog thishowProgressDialog(String message, FragmentThird mActivity){
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity.getContext());
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }

}
