package com.xingwang.groupchat.utils;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;
import com.xingwang.groupchat.R;
import com.xingwang.groupchat.callback.DialogAlertCallback;
import com.xingwang.groupchat.callback.DialogAlertCancelCallBack;
import com.xingwang.groupchat.callback.DialogCallback;

/**
 */

public class DialogUtils {
    /**
     * 普通
     *
     * @param
     * @return
     */
    public static BaseNiceDialog obtainCommonDialog(final String contentStr, final DialogAlertCallback dialogAlertCallback) {

        BaseNiceDialog niceDialog = NiceDialog.init()
                .setLayoutId(R.layout.groupchat_dialog_comment_alert)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        Button cancelBtn = (Button) holder.getView(R.id.cancel);
                        Button sureBtn = (Button) holder.getView(R.id.sure);
                        TextView content = holder.getView(R.id.content);
                        content.setText(contentStr);
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        sureBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (dialogAlertCallback != null) {
                                    dialogAlertCallback.sure();
                                }
                            }
                        });
                    }
                })
                .setOutCancel(false)
                .setAnimStyle(R.style.groupchat_center_dialog_animation);
        return niceDialog;

    }

    /**
     * 普通
     *
     * @param
     * @return
     */
    public static BaseNiceDialog obtainCommonDialog(final String contentStr, final DialogAlertCallback dialogAlertCallback, final DialogAlertCancelCallBack dialogAlertCancelCallBack) {
        BaseNiceDialog niceDialog = NiceDialog.init()
                .setLayoutId(R.layout.groupchat_dialog_comment_alert)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        Button cancelBtn = (Button) holder.getView(R.id.cancel);
                        Button sureBtn = (Button) holder.getView(R.id.sure);
                        TextView content = holder.getView(R.id.content);
                        content.setText(contentStr);
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (dialogAlertCancelCallBack != null) {
                                    dialogAlertCancelCallBack.cancel();
                                }
                            }
                        });
                        sureBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (dialogAlertCallback != null) {
                                    dialogAlertCallback.sure();
                                }
                            }
                        });
                    }
                })
                .setOutCancel(false)
                .setAnimStyle(R.style.groupchat_center_dialog_animation);
        return niceDialog;

    }


    /**
     * 单个确认
     *
     * @param
     * @return
     */
    public static BaseNiceDialog obtainSingleDialog(final String contentStr, final DialogAlertCallback dialogAlertCallback) {
        BaseNiceDialog niceDialog = NiceDialog.init()
                .setLayoutId(R.layout.groupchat_dialog_comment_alert)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        Button sureBtn = (Button) holder.getView(R.id.sure);
                        TextView content = holder.getView(R.id.content);
                        content.setText(contentStr);
                        sureBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (dialogAlertCallback != null) {
                                    dialogAlertCallback.sure();
                                }
                            }
                        });
                    }
                })
                .setOutCancel(false)
                .setAnimStyle(R.style.groupchat_center_dialog_animation);
        return niceDialog;

    }

    /**
     * 单个确认
     *
     * @param
     * @return
     */
    public static BaseNiceDialog obtainSingleDialog(String contentStr) {
        return obtainSingleDialog(contentStr, null);

    }

}
