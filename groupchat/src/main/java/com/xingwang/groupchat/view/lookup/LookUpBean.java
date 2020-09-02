package com.xingwang.groupchat.view.lookup;

/**
 * 实体类继承该实体
 */
public abstract class LookUpBean {
    private boolean isPeak=false;//是不是显示拼音
    private String headStr=null;//首个中文字的英文字母

    boolean isPeak() {
        return isPeak;
    }

    void setPeak(boolean peak) {
        isPeak = peak;
    }

    String getHeadStr() {
        if (headStr==null){
            headStr =Cn2Spell.getPinYinFirstLetter(getLookUpKey());
            //这里没有判断 如果查找出来不包含26个英文字母 会显示getlookup的首个字符
            headStr = Cn2Spell.changeLU(headStr);

        }

        return headStr;
    }
    public abstract String getLookUpKey();

}
