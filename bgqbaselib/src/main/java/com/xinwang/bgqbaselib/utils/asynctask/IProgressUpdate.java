package com.xinwang.bgqbaselib.utils.asynctask;

public interface IProgressUpdate<Progress>{
    void onProgressUpdate(Progress... values);

}
