package com.xingwang.classroom.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xingwang.classroom.R;


/**
 * Created by Administrator on 2016/11/1.
 */
public class MyToast {

        private static Toast mToast = null;

        //压制警告
        @SuppressLint("InflateParams")
        public static void myToast(Context contenxt,String str)
        {
            if (contenxt==null)
                return;
            //首先加载一个自定义的布局
            LayoutInflater inflater = LayoutInflater.from(contenxt);

            View view = inflater.inflate(R.layout.widget_custom_toast_classroom, null);

            //然后找到里面的控件，以单纯的textview为例
            TextView tvToastTips = (TextView) view.findViewById(R.id.tvToastTips);

            //避免toast长时间显示
            if (mToast != null)
            {
                mToast.cancel();
            }

            //接下来就是给textview设置信息
            tvToastTips.setText(str);

            //最重要的就是下面了，把布局引用到toast当中
            //获得toast
            mToast = new Toast(contenxt);

            //设置toast显示的位置
            mToast.setGravity(Gravity.BOTTOM, 0, 120);

            //设置弹出显示的时间
            mToast.setDuration(Toast.LENGTH_SHORT);

            //设置布局
            mToast.setView(view);

            //最后一步，show出来
            mToast.show();

        }
        //压制警告
        @SuppressLint("InflateParams")
        public static void myToast(Context context,String str, int BOTTOM)
        {
            if (context==null)
                return;
            //首先加载一个自定义的布局
            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.widget_custom_toast_classroom, null);

            //然后找到里面的控件，以单纯的textview为例
            TextView tvToastTips = (TextView) view.findViewById(R.id.tvToastTips);

            //避免toast长时间显示
            if (mToast != null)
            {
                mToast.cancel();
            }

            //接下来就是给textview设置信息
            tvToastTips.setText(str);

            //最重要的就是下面了，把布局引用到toast当中
            //获得toast
            mToast = new Toast(context);

            //设置toast显示的位置
            mToast.setGravity(Gravity.CENTER, 0, 0);

            //设置弹出显示的时间
            mToast.setDuration(Toast.LENGTH_SHORT);

            //设置布局
            mToast.setView(view);

            //最后一步，show出来
            mToast.show();

        }
        /**
         * 使toast不在显示
         */
        public static void cancleMyToast()
        {
            if (mToast != null)
            {
                mToast.cancel();
            }
        }


}
