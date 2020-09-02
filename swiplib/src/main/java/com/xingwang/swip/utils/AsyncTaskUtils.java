package com.xingwang.swip.utils;

import android.os.AsyncTask;
import android.os.Build;

import com.xingwang.swip.utils.asynctask.IDoInBackground;
import com.xingwang.swip.utils.asynctask.IIsViewActive;
import com.xingwang.swip.utils.asynctask.IPostExecute;
import com.xingwang.swip.utils.asynctask.IPreExecute;
import com.xingwang.swip.utils.asynctask.IProgressUpdate;
import com.xingwang.swip.utils.asynctask.IPublishProgress;

public class AsyncTaskUtils<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements IPublishProgress<Progress> {

    private IPreExecute mPreExecute;
    private IProgressUpdate<Progress> mProgressUpdate;
    private IDoInBackground<Params, Progress, Result> mDoInBackground;
    private IIsViewActive mViewActive;
    private IPostExecute<Result> mPostExecute;

    private AsyncTaskUtils() {
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mPreExecute != null) mPreExecute.onPreExecute();
    }
    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (mProgressUpdate != null) mProgressUpdate.onProgressUpdate(values);
    }
    @Override
    public Result doInBackground(Params... params) {
        return mDoInBackground == null ? null : mDoInBackground.doInBackground(this, params);
    }
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mPostExecute != null && (mViewActive == null || mViewActive.isViewActive())) mPostExecute.onPostExecute(result);
    }

    @SafeVarargs

    public final AsyncTask<Params, Progress, Result> start(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return super.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
        } else {
            return super.execute(params);
        }
    }
    @Override

    public void showProgress(Progress[] values) {
        this.publishProgress(values);
    }


    public static <Params, Progress, Result> Builder<Params, Progress, Result> newBuilder() {
        return new Builder<>();
    }

    public static class Builder<Params, Progress, Result> {
        private final AsyncTaskUtils<Params, Progress, Result> mAsyncTask;
        public Builder() {
            mAsyncTask = new AsyncTaskUtils<>();
        }
        public Builder<Params, Progress, Result> setPreExecute(IPreExecute preExecute) {
            mAsyncTask.mPreExecute = preExecute;
            return this;
        }

        public Builder<Params, Progress, Result> setProgressUpdate(IProgressUpdate<Progress> progressUpdate) {
            mAsyncTask.mProgressUpdate = progressUpdate;
            return this;
        }

        public Builder<Params, Progress, Result> setDoInBackground(IDoInBackground<Params, Progress, Result> doInBackground) {
            mAsyncTask.mDoInBackground = doInBackground;
            return this;
        }
        public Builder<Params, Progress, Result> setViewActive(IIsViewActive viewActive) {
            mAsyncTask.mViewActive = viewActive;
            return this;
        }
        public Builder<Params, Progress, Result> setPostExecute(IPostExecute<Result> postExecute) {
            mAsyncTask.mPostExecute = postExecute;
            return this;
        }
        public Builder<Params, Progress, Result> cancel() {
            mAsyncTask.cancel(true);
            return this;
        }
        @SafeVarargs
        public final AsyncTask<Params, Progress, Result> start(Params... params) {
            return mAsyncTask.start(params);
        }

    }

}