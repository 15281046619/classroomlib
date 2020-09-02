package com.xingwang.swip.utils.asynctask;

public interface IProgressUpdate<Progress>{
    void onProgressUpdate(Progress... values);

}
