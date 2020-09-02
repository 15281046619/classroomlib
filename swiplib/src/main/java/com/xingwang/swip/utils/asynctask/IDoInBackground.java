package com.xingwang.swip.utils.asynctask;

public interface IDoInBackground<Params,Progress,Result> {

    Result doInBackground(IPublishProgress<Progress> publishProgress, Params... params);

}