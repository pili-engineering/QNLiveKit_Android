import com.sun.javadoc.*;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javadoc.ProgramElementDocImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类说明：打印类及其字段、方法的注释<br>
 * 使用javadoc实现<br>
 * 需要在工程中加载jdk中的包$JAVA_HOME/lib/tools.jar
 * <p>
 * <p>
 * 文档工具https://cf.qiniu.io/pages/viewpage.action?pageId=72910016
 */
public class Docletuse {

    private static HashMap<String, String> links = new HashMap<>();
    public static void main(String[] args) {

        links.put("下载地址", "https://github.com/pili-engineering/QNLiveKit_Android/tree/main/app-sdk");
        links.put("混淆配置参考", "https://github.com/pili-engineering/QNLiveKit_Android/blob/main/app/proguard-rules.pro");

        DocFormat format = new DocFormat();
        format.name = "使用指南";
        format.home = false;
        format.describe = new DocFormat.Describe();
        format.describe.content.add("安卓使用指南");
        format.reflect = links;
        DocFormat.BlockItem filedItem = new DocFormat.BlockItem();

        DocFormat.ElementItem i1 = new DocFormat.ElementItem();
        i1.name ="下载sdk";
        i1.desc.add("{{\" + 下载地址 + \"}}");
        filedItem.elements.add(i1);



        DocFormat.ElementItem i2 = new DocFormat.ElementItem();
        i2.name ="配置aar";
        i2.desc.add("\n" +
                "//使用 sdk方式依赖\n" +
                "\n" +
                "//七牛imsdk 必选\n" +
                "implementation project(':app-sdk:depends_sdk_qnim')  //其他版本下载地址-(https://github.com/pili-engineering/QNDroidIMSDK/tree/main/app/libs)\n" +
                "//七牛rtc 主播推流必选  观众要连麦必选\n" +
                "implementation project(':app-sdk:depends_sdk_qrtc')  //其他版本下载地址-(https://github.com/pili-engineering/QNRTC-Android/tree/master/releases)\n" +
                "//七牛播放器  观众拉流端必选 \n" +
                "implementation project(':app-sdk:depends_sdk_piliplayer') //其他版本下载地址-(https://developer.qiniu.com/pili/1210/the-android-client-sdk)\n" +
                "\n" +
                "//低代码无ui sdk 必选\n" +
                "implementation project(':app-sdk:qlive-sdk') \n" +
                "implementation 'com.squareup.okhttp3:okhttp:4.2.2' //okhttp 4版本以上 必选\n" +
                "implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9' //kotlin协程 必选\n" +
                "\n" +
                "\n" +
                "\n" +
                "//以下为 UIkit 的依赖包 不使用UIkit则不需要\n" +
                "implementation project(':app-sdk:qlive-sdk-uikit')  //UIkit包\n" +
                "//谷歌官方UI组件\n" +
                "implementation \"androidx.swiperefreshlayout:swiperefreshlayout:1.1.0\"\n" +
                "implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.0'\n" +
                "implementation 'androidx.recyclerview:recyclerview:1.1.0'\n" +
                "implementation 'androidx.appcompat:appcompat:1.2.0'\n" +
                "implementation 'androidx.cardview:cardview:1.0.0'\n" +
                "//UIkit依赖两个知名的开源库\n" +
                "implementation 'com.github.bumptech.glide:glide:4.11.0' //图片加载\n" +
                "implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.44' //列表适配器\n" +
                "\n" +
                "```\n" +
                "UIkit也可以直接使用源码模块-可直接修改代码\n" +
                "```\n" +
                "  implementation project(':liveroom-uikit')\n" +
                "```");
        filedItem.elements.add(i2);




        DocFormat.ElementItem i3 = new DocFormat.ElementItem();
        i3.name ="混淆配置";
        i3.desc.add("如果你的项目需要混淆 参考{{\" + 混淆配置参考 + \"}}");
        filedItem.elements.add(i3);



        DocFormat.ElementItem i4 = new DocFormat.ElementItem();
        i4.name ="UIKIT使用";
        i4.desc.add("\n" +
                "//初始化\n" +
                "QLive.init(context ,new QTokenGetter(){\n" +
                "        //业务请求token的方法 \n" +
                "        void getTokenInfo( QLiveCallBack<String> callback){\n" +
                "             //当token过期后如何获取token\n" +
                "            GetTokenApi.getToken(callback);\n" +
                "        }\n" +
                "});\n" +
                "\n" +
                "\n" +
                "//登陆 登陆成功后才能使用qlive\n" +
                "QLive.auth(new QLiveCallBack<Void>{})\n" +
                "\n" +
                "//可选 绑定用户资料 第一次绑定后没有跟新个人资料可不用每次都绑定 \n" +
                "//也可以在服务端绑定用户 服务端也可以调用\n" +
                "Map ext = new HashMap()\n" +
                "ext.put(\"vip\",\"1\"); // 可选参数，接入用户希望在直播间的在线用户列表/资料卡等UI中显示自定义字段如vip等级等等接入方的业务字段\n" +
                "ext.put(\"xxx\",\"xxx\");//扩展多个字段\n" +
                "//跟新/绑定 业务端的用户信息\n" +
                "QLive.setUser(new QUserInfo( \"your avatar\",\"your nickname\", ext) ,new QLiveCallBack<Void>{});\n" +
                "\n" +
                "\n" +
                "QliveUIKit liveUIKit = QLive.getLiveUIKit()\n" +
                "//跳转到直播列表页面\n" +
                "liveUIKit.launch(context);");
        filedItem.elements.add(i4);


        System.out.println(format.toJson());
    }


}
