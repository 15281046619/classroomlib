package com.xingwang.classroomlib;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;


import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.view.X5WebView;


import java.lang.annotation.Annotation;


/**
 * Date:2021/6/10
 * Time;9:00
 * author:baiguiqiang
 */
public class TestWebViewActivity extends BaseNetActivity {


    private long curtime;

    @Override
    protected int layoutResId() {
        return R.layout.activity_test_webview;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        X5WebView webView =findViewById(R.id.web_view);
        curtime =System.currentTimeMillis();
        webView.setWebViewClient(new JsX5WebViewClient(webView,this){
            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                super.onPageFinished(view, url);

                LogUtil.i("loadTime",System.currentTimeMillis()-curtime+"");
            }

        });



        webView. addJavascriptInterface(new js(),"javaInterFace");
        webView.loadData(CommentUtils.getWebNewData("<h2 class=\"ql-align-center\"><img src=\"http://oss.xw518app.xw518.com/default/2021/06/08/152214_千万补贴计划.jpg\"></h2><video class=\"ql-video ql-align-center\" controls=\"controls\" width=\"100%\" type=\"video/mp4\" src=\"http://www.xw518.com/_res/fwws.mp4\"></video><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2020/0108/20200108010531516.jpg\"></p><p class=\"ql-align-center\">非洲猪瘟（英文名称：African Swine fever，简称：ASF）是由非洲猪瘟病毒（英文名称：African Swine fever virus，简称：ASFV）感染家猪和各种野猪（如非洲野猪、欧洲野猪等）引起一种急性、出血性、烈性传染病。世界动物卫生组织（OIE）将其列为法定报告动物疫病，该病也是我国重点防范的一类动物疫情。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903101434978.jpg\" alt=\"非洲猪瘟防治\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">其特征是发病过程短，急性和急性感染死亡率高达100%，临床表现为<span style=\"color: rgb(255, 0, 0);\">发热（达40～42℃），心跳加快，呼吸困难，部分咳嗽，眼、鼻有浆液性或粘液性脓性分泌物，皮肤发绀，淋巴结、肾、胃肠粘膜明显出血</span>，非洲猪瘟临床症状与猪瘟症状相似。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903102258616.jpg\" alt=\"非洲猪瘟预防\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">在<span style=\"color: rgb(255, 0, 0);\">耳、鼻、腋下、腹、会阴、尾、脚无毛部分</span>呈界线明显的<span style=\"color: rgb(255, 0, 0);\">紫色斑，耳朵紫斑部分常肿胀，中心深暗色分散性出血，边缘褪色</span>，尤其在腿及腹壁皮肤肉眼可见到。显微镜所见，于真皮内小血管，尤其在乳头状真皮呈严重的充血和肉眼可见的紫色斑，血管内发生纤维性血栓，血管周围有许多之嗜酸球，耳朵紫斑部分上皮之基层组织内，可见到血管血栓性小坏死现象，切开胸腹腔、心包、胸膜、腹膜上有许多澄清、黄或带血色液体，尤其在腹部内脏或肠系膜上表部分，小血管受到影响更甚，于内脏浆液膜可见到棕色转变成浅红色之瘀斑，即所谓的麸斑（Bran Flecks），尤其于小肠更多，直肠壁深处有暗色出血现象，肾脏有弥漫性出血情形，胸膜下水肿特别明显，及心包出血。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">（1）在<strong>淋巴节</strong>有猪瘟罕见的某种程度之出血现象，上表或切面似血肿之结节较淋巴节多。</p><p class=\"ql-align-center\">（2）<strong>脾脏肿大</strong>，髓质肿胀区呈深紫黑色，切面突起，淋巴滤胞小而少，有7%猪脾脏发生小而暗红色突起三角形栓塞情形。</p><p class=\"ql-align-center\">（3）<strong>循环系统：</strong>心包液特别多，少数病例中呈混浊且含有纤维蛋白，但多数心包下及次心内膜充血。</p><p class=\"ql-align-center\">（4）<strong>呼吸系统：</strong>喉、会厌有瘀斑充血及扩散性出血，比猪瘟更甚，瘀斑有发生於气管前三分之一处，镜检下，肠有充血而没有出血病灶，肺泡则呈现出血现象，淋巴球呈破裂。</p><p class=\"ql-align-center\">（5）<strong>肝：</strong>肉眼检查显正常，充血暗色或斑点大多异常，近胆部分组织有充血及水肿现象，小叶间结缔组织有淋巴细胞、浆细胞（Plasma Cell）及间质细胞浸润，同时淋巴球之核破裂为其特征。</p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903111533320.jpg\" alt=\"非洲猪瘟解剖图\"></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903103109967.jpg\" alt=\"非洲猪瘟疫情\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">2018年8月3日我国首次出现非洲猪瘟疫情，截至2019年6月30日，发生143起非洲猪瘟疫情中，有131起解除了疫区封锁，25个省份的疫区全部解除封锁；2019年共发生疫情44起。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903103513285.jpg\" alt=\"非洲猪瘟病原\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">非洲猪瘟病毒是非洲猪瘟科非洲猪瘟病毒属的重要成员，病毒有些特性类似虹彩病毒科和痘病毒科。病毒粒子的直径为175－215纳米，呈20面体对称，有囊膜。基因组为双股线状DNA，大小170－190kb。在猪体内，非洲猪瘟病毒可在几种类型的细胞浆中，尤其是网状内皮细胞和单核巨噬细胞中复制。该病毒可在钝缘蜱中增殖，并使其成为主要的传播媒介。 本病毒能从被<span style=\"color: rgb(255, 0, 0);\">感染猪之血液、组织液、内脏，及其他排泄物中证实出来，低温暗室内存在血液中之病毒可生存六年，室温中可活数周</span>，加热被病毒感染的血液55℃30分钟或60℃10分钟，病毒将被破坏，许多脂溶剂和消毒剂可以将其破坏。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903103819746.jpg\" alt=\"非洲猪瘟影响\"></p><p class=\"ql-align-center\">①非洲猪瘟传播快，治疗难，一旦得病，疾病治疗无效，猪瘟预防是关键！</p><p class=\"ql-align-center\">②由于非洲猪瘟问题，全球猪肉进口需求激增、价格明显上扬，而作为生猪饲料的谷物和油籽，尤其是大豆的需求则出现收缩。</p><p class=\"ql-align-center\">③养殖户损失惨重，挫败养猪积极性及信心。</p><p class=\"ql-align-center\">④非洲猪瘟不是“人畜共患病”，不传染人，但对猪来说是非常致命的，养殖者需要注意。</p><p class=\"ql-align-center\">⑤猪肉供应不够，猪肉价格上涨，国家大力支持养殖朋友复养。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><span style=\"color: rgb(255, 0, 0);\">兴旺猪药技术老师说：金猪复养黄金时期已经到来，赶快行动起来吧！兴旺帮你预防非洲猪瘟！</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903104101161.jpg\" alt=\"猪复养\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong>兴旺抗非复养介绍：</strong></p><p class=\"ql-align-center\">猪肉现历史高新，为了让复养之路更加顺畅，为了让养猪人不再受非瘟的阻挠，作为建厂已有十六年的兽药品牌，兴旺药业从去年开始便召集整个猪药研发团队，着手开始进行非瘟预防方法的研究。在今年7月成功研制出了一套非瘟预防方案，可以帮助朋友们更加的复养，为大家建立强大的信心，成为养猪人复养之路上的“贴身保镖”</p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903112617327.jpg\" alt=\"抗非复养\"></p><p class=\"ql-align-center\"><img src=\"http://oss.xw518app.xw518.com/default/2021/05/12/145244_20200108103552382.jpg\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">兴旺非瘟预防方案是兴旺药业针对非瘟疫情研发的一套药物组合预防方案【非温卫士】，结合传统中医的“<strong>扶正祛邪</strong>”的思路，通过<span style=\"color: rgb(255, 0, 0);\">饮水和拌料的形式让药物进入猪的体内，使猪能快速的吸收药效，针对非洲猪瘟这种囊膜型病毒，迅速控制其无法在猪体内进行复制增殖，成功做到预防</span>。只要大家严格按照用药指导使用，针对周边发病或本猪场进行拔牙处理后等情况，可让自己猪场的猪只绝大多数存活。目前已经有3000多个养猪户选择了兴旺非温卫士。效果治疗！</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><img src=\"http://oss.xw518app.xw518.com/default/2021/05/12/145221_20200108105932672.jpg\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">我国非洲猪瘟爆发至今，生物防控、精准清除、提高免疫力等预防方法我们已经耳熟能详，但这些措施的局限性也显而易见，兴旺猪药技术老师们日夜研发了控制非洲猪瘟的预防方案【非温卫士】且进行了上百次临床试验，下面一起看看试验数据结果：</p><p class=\"ql-align-center\"><img src=\"http://zy.xw518.com/uploadfile/2020/0108/20200108111116737.jpg\"></p><p class=\"ql-align-center\"><img src=\"http://oss.xw518app.xw518.com/default/2021/05/12/145154_20200108111158836.jpg\"></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><span style=\"color: rgb(255, 0, 0);\">、</span></p><p class=\"ql-align-center\"><strong style=\"color: rgb(255, 0, 0);\">说明：此方案针对一般猪瘟预防，特殊用药情况要咨询兴旺技术老师！</strong></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">&nbsp;</p><p class=\"ql-align-center\"><strong>兴旺完整复养服务：</strong></p><p class=\"ql-align-center\"><strong>①专业指导：</strong>兴旺猪药资深技术老师一对一指导</p><p class=\"ql-align-center\"><strong>②育肥管理：</strong>从仔猪进栏到育肥出栏</p><p class=\"ql-align-center\"><strong>③疾病防治：</strong>从常见疾病防治到非瘟专项预防</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong>兴旺猪药技术老师说：</strong></p><p class=\"ql-align-center\"><span style=\"color: rgb(255, 0, 0);\">非洲猪瘟的确可怕，但是放弃治疗更可怕，你只需要用一头猪的钱到兴旺买猪瘟预防药，试一试，兴旺就能让你减少更多的损失！</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong><img src=\"http://zy.xw518.com/uploadfile/2019/0903/20190903105354447.jpg\" alt=\"兴旺介绍\"></strong></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\">兴旺2003年成立，至今服务客户上千万，正规农业部GMP认证，目前，公司拥有以<strong>疾病预防学、兽医临床学、动物营养学等归国博士和国内知名教授为核心的高素质科研团队</strong>。主要致力于将动物的疾病预防与治疗技术有机结合起来，针对不同的养殖地区和养殖单位，提供高技术含量的新型兽用产品和行之有效的系统方案。</p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong>兴旺愿景：</strong><span style=\"color: rgb(255, 0, 0);\">成为全球知名的、受广大用户信赖的互联网兽药企业！</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong>兴旺使命：</strong><span style=\"color: rgb(255, 0, 0);\">让养殖业健康可持续发展，让养殖户脱贫致富、食品(无抗养殖)</span></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><strong>非温卫士成果：</strong></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><a href=\"http://zy.xw518.com/show-20-1141-1.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(0, 128, 0);\">博鳌论坛圆满落幕，兴旺“非温卫士”大放异彩！</a></p><p class=\"ql-align-center\"><br></p><p class=\"ql-align-center\"><a href=\"http://zy.xw518.com/show-20-1173-1.html\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(0, 128, 0);\">热烈祝贺兴旺非温卫士荣获“名牌产品奖”</a></p>"),
                "text/html;charset=utf-8", "utf-8");
    }
    class js implements JavascriptInterface{

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}
