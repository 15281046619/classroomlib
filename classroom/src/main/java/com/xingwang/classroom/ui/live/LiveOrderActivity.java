package com.xingwang.classroom.ui.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.GoodListBean;
import com.xingwang.classroom.bean.JsonBean;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xingwang.classroom.view.citydata.GetJsonDataUtil;
import com.xinwang.bgqbaselib.utils.AndroidBug5497Workaround;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.KeyBoardHelper;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.base.BaseNetActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.com.cesgroup.numpickerview.NumberPickerView;

public class LiveOrderActivity extends BaseNetActivity implements View.OnClickListener {

    private KeyBoardHelper mKeyBoardHelper;

    private CustomToolbar toolbar;
    private EditText et_buyer_name,et_buyer_tel,et_city,et_adr,et_buyer_msg;
    private NumberPickerView pick_num;

    private TextView tv_good_title,tv_good_price,tv_good_des,tv_buy_num;
    private ImageView img_good_cover;
    private Button btn_submit;

    private GoodListBean.GoodBean goodBean;//货物id

    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private static boolean isLoaded = false;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    ToastUtils.showShort("解析失败");
                    break;
            }
        }
    };

    public static Intent getIntent(Context context, GoodListBean.GoodBean goodBean) {
        Intent intent = new Intent(context, LiveOrderActivity.class);
        intent.putExtra(Constants.DATA,goodBean);
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_order_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        mKeyBoardHelper =new KeyBoardHelper(this);
        mKeyBoardHelper.onCreate();
        AndroidBug5497Workaround.assistActivity(this);

        toolbar=findViewById(R.id.toolbar);

        et_buyer_name=findViewById(R.id.et_buyer_name);
        et_buyer_tel=findViewById(R.id.et_buyer_tel);
        et_city=findViewById(R.id.et_city);
        et_adr=findViewById(R.id.et_adr);
        et_buyer_msg=findViewById(R.id.et_buyer_msg);

        pick_num=findViewById(R.id.pick_num);
        tv_buy_num=findViewById(R.id.tv_buy_num);
        img_good_cover=findViewById(R.id.img_good_cover);
        tv_good_title=findViewById(R.id.tv_good_title);
        tv_good_price=findViewById(R.id.tv_good_price);
        tv_good_des=findViewById(R.id.tv_good_des);
        btn_submit=findViewById(R.id.btn_submit);

        et_city.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        et_city.setText(SharedPreferenceUntils.getSaveCity(this));
        et_adr.setText(SharedPreferenceUntils.getSaveAddress(this));
        et_buyer_name.setText(SharedPreferenceUntils.getSaveName(this));
        String savePhone = SharedPreferenceUntils.getSavePhone(this);
        et_buyer_tel.setText(TextUtils.isEmpty(savePhone)? BeautyDefine.getUserInfoDefine(this).getPhone():savePhone);

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

    }

    private void initData(){

        goodBean= (GoodListBean.GoodBean) getIntent().getSerializableExtra(Constants.DATA);

        tv_good_price.setText(goodBean.getPrice());
        tv_good_des.setText(Html.fromHtml(goodBean.getBody()));
        tv_good_title.setText(goodBean.getTitle());
        GlideUtils.loadAvatar(goodBean.getCover(),img_good_cover);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveOrderActivity.this.finish();
            }
        });

        pick_num.setMinDefaultNum(1)  // 最小限定量
                .setCurrentNum(1) // 当前数量
                .setOnInputNumberListener(new NumberPickerView.OnInputNumberListener() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                        tv_buy_num.setText("共"+charSequence.toString().trim()+"件");
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
    }

    private void showPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ?
                    options1Items.get(options1).getPickerViewText() : "";

            String opt2tx = options2Items.size() > 0
                    && options2Items.get(options1).size() > 0 ?
                    options2Items.get(options1).get(options2) : "";

            String opt3tx = options2Items.size() > 0
                    && options3Items.get(options1).size() > 0
                    && options3Items.get(options1).get(options2).size() > 0 ?
                    options3Items.get(options1).get(options2).get(options3) : "";


            if (opt1tx.equals(opt2tx)){//此时为直辖
                et_city.setText(opt1tx+opt3tx);
            }else {
                et_city.setText(opt1tx+opt2tx+opt3tx);
            }
          }).setTitleText("城市选择")
                .setTitleSize(16)
                .setSubCalSize(15)
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(15)
                .isDialog(true)
                .isRestoreItem(true)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表


                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    private void postData(){

        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("提交中",false));

        requestPost(HttpUrls.URL_GOOD_ORDER(), new ApiParams().with("goods_id", String.valueOf(goodBean.getId()))
                .with("goods_num", tv_buy_num.getText().toString()).with("tel", et_buyer_tel.getText().toString())
                .with("address", et_city.getText().toString()+et_adr.getText().toString()).with("username", et_buyer_name.getText().toString())
                .with("user_tips", et_buyer_msg.getText().toString()), CommonEntity.class, new HttpCallBack<CommonEntity>() {
            @Override
            public void onFailure(String message) {
                BeautyDefine.getOpenPageDefine(LiveOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                ToastUtils.showShort(message);
            }

            @Override
            public void onSuccess(CommonEntity s) {
                SharedPreferenceUntils.savePhone(LiveOrderActivity.this,et_buyer_tel.getText().toString().trim());
                SharedPreferenceUntils.saveAddress(LiveOrderActivity.this,et_adr.getText().toString().trim());
                SharedPreferenceUntils.saveCity(LiveOrderActivity.this,et_city.getText().toString().trim());
                SharedPreferenceUntils.saveName(LiveOrderActivity.this,et_buyer_name.getText().toString().trim());
                BeautyDefine.getOpenPageDefine(LiveOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                MyToast.myLongToast(LiveOrderActivity.this,"下单成功");
                LiveOrderActivity.this.finish();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_city) {
            mKeyBoardHelper.closeInputMethodManager();
            showPickerView();
        } else if (v.getId() == R.id.btn_submit) {
            if (TextUtils.isEmpty(et_buyer_name.getText().toString().trim())){
                ToastUtils.showShort("请填写买家姓名");
                return;
            }else if (TextUtils.isEmpty(et_buyer_tel.getText().toString().trim())){
                ToastUtils.showShort("请填写手机号码");
                return;
            }else if (TextUtils.isEmpty(et_city.getText().toString().trim())){
                ToastUtils.showShort("请填写所在区域");
                return;
            }else if (TextUtils.isEmpty(et_adr.getText().toString().trim())){
                ToastUtils.showShort("请填写地址详情");
                return;
            }

            postData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardHelper!=null){
            mKeyBoardHelper.onDestroy();
        }
    }
}
