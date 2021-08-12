package com.xinwang.shoppingcenter.bean;

import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.CommonEntity;

/**
 * Date:2021/7/28
 * Time;10:26
 * author:baiguiqiang
 */
public class CaseBean extends CommonEntity {
    private CasesListBean.CaseBean data;

    public CasesListBean.CaseBean getData() {
        return data;
    }

    public void setData(CasesListBean.CaseBean data) {
        this.data = data;
    }
}
