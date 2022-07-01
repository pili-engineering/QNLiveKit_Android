

## qlive-sdk: 低代码互动直播sdk

qlive-sdk是七牛云推出的一款互动直播低代码解决方案sdk。只需几行代码快速接入互动连麦pk直播。


```                  
                                                   
                              +---------------+     +---> RoomListPage //房间列表UI实现页面
                              |               |     |
                          +---+   QLiveUIKIT  +--- -+
                          |   |               |     |
                          |   +---------------+     +---> RoomPage    //直播间页面UI实现
                          |       uikit sdk   
                          | 
                          |                        
                          |                         
                          |                         +---> createRoom  //创建房间接口
                          |   +---------------+     |
+----------------------+  |   |               |     +---> listRoom    //房间列表接口
|                      |  +---+     QRooms    +-----+
|      QLive           |  |   |               |     +---> deleteRoom  //删除房间接口
|                      |  |   +---------------+     |
+----------------------+  |       房间管理接口        +---> getRoomInfo //获取房间信息接口
                          |
                          | 
                          | 
                          |                         +--->  QChatRoomService //聊天室服务 
                          |   +----------------+    |
                          |   |                |    +--->  QLinkMicService  //连麦业务服务
                          +---+   QLiveClient   +---+
                              |                |    +--->  QPKService       //pk业务服务
                              +----------------+    |
                                 推拉流房间客户端      +--->  QPublicChatService //房间里公屏消息服务       
                                   无UI版本sdk       |
                                                    +--->  QRoomService     //房间频道业务 
                                                    |    
                                                    +--->  QDanmakuService  //弹幕服务 

```    

## sdk接入

1 下载sdk
[下载地址](https://github.com/pili-engineering/QNLiveKit_Android/tree/main/app-sdk)

2 参考dome工程的build.gradle文件 配置aar
```
//使用 sdk方式依赖

//七牛imsdk 必选
implementation project(':app-sdk:depends_sdk_qnim')  //其他版本下载地址-(https://github.com/pili-engineering/QNDroidIMSDK/tree/main/app/libs)
//七牛rtc 主播推流必选  观众要连麦必选
implementation project(':app-sdk:depends_sdk_qrtc')  //其他版本下载地址-(https://github.com/pili-engineering/QNRTC-Android/tree/master/releases)
//七牛播放器  观众拉流端必选 
implementation project(':app-sdk:depends_sdk_piliplayer') //其他版本下载地址-(https://developer.qiniu.com/pili/1210/the-android-client-sdk)

//低代码无ui sdk 必选
implementation project(':app-sdk:qlive-sdk') 
implementation 'com.qiniu:happy-dns:1.0.0' // 七牛dns 必选项目
implementation 'com.squareup.okhttp3:okhttp:4.2.2' //okhttp 4版本以上 必选
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9' //kotlin协程 必选



//以下为 UIkit 的依赖包 不使用UIkit则不需要
implementation project(':app-sdk:qlive-sdk-uikit')  //UIkit包
//谷歌官方UI组件
implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.0'
implementation 'androidx.recyclerview:recyclerview:1.1.0'
implementation 'androidx.appcompat:appcompat:1.2.0'
//UIkit依赖两个知名的开源库
implementation 'com.github.bumptech.glide:glide:4.11.0' //图片加载
implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.44' //列表适配器

```
UIkit也可以直接使用源码模块-可直接修改代码
```
  implementation project(':liveroom-uikit')
```

3 混淆配置
如果你的项目需要混淆 [qlivesdk混淆配置参考](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/app/proguard-rules.pro
)


## 使用说明
### UIKIT
```java
//初始化
QLive.init(context ,new QTokenGetter(){
        //业务请求token的方法 
        void getTokenInfo( QLiveCallBack<String> callback){
             //当token过期后如何获取token
            GetTokenApi.getToken(callback);
        }
});


//登陆 登陆成功后才能使用qlive
QLive.auth(new QLiveCallBack<Void>{})

//可选 绑定用户资料 第一次绑定后没有跟新个人资料可不用每次都绑定 
//也可以在服务端绑定用户 服务端也可以调用
Map ext = new HashMap()
ext.put("vip","1"); // 可选参数，接入用户希望在直播间的在线用户列表/资料卡等UI中显示自定义字段如vip等级等等接入方的业务字段
ext.put("xxx","xxx");//扩展多个字段
//跟新/绑定 业务端的用户信息
QLive.setUser(new QUserInfo( "your avatar","your nickname", ext) ,new QLiveCallBack<Void>{});


QliveUIKit liveUIKit = QLive.getLiveUIKit()
//跳转到直播列表页面
liveUIKit.launch(context);
```

### 自定义UI

![alt 属性文本](http://qrnlrydxa.hn-bkt.clouddn.com/qlive5.png)



**直接修改开源代码**
uikit使用源码依赖，直接修改源码
- 优点：快捷
- 缺点：官方更新UI逻辑代码后不方便同步

**无侵入式自定义UI**
uikit使用sdk依赖


拷贝布局文件--只需拷贝需要修改的页面，拷贝至接入的工程并且重新命名
- kit_activity_room_player.xml //[观众直播间布局 ](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_player.xml)
- kit_activity_room_pusher.xm  //[主播直播间布局](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_pusher.xml)
- kit_activity_room_list.xml   //[房间列表页布局](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_list.xml)

clear重新编译编译-->androidStudio预览看到如上效果图

- 1修改拷贝文件的布局任意属性，比如边距，文本颜色，样式等等
- 2调用替换布局文件
```
val roomPage = QLive.getLiveUIKit().getPage(RoomPage::class.java)
//自定义房间页面观众房间的布局
roomPage.playerCustomLayoutID = R.layout.customXXXlayout
//自定义房间页面主播房间的布局
roomPage.anchorCustomLayoutID = R.layout.customXXlayout

val roomListPage = QLive.getLiveUIKit().getPage(RoomListPage::class.java)
//自定义房间列表页面布局
roomListPage.customLayoutID = R.layout.customXlayout
```

#### 修改现有的UI组件

修改拷贝的布局文件或者源布局文件

案列：
```
 ....
 // 找到房间背景图
 <com.qlive.uikitcore.QKitImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        android:src="@drawable/kit_dafault_room_bg" />
 ....

改变背景图片

 <com.qlive.uikitcore.QKitImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        android:src="@drawable/my_room_bg" />
tip: 所有的安卓自带基础UI都可以修改属性 如边距,父容器排列，文本颜色等等
```

如果要替换UI里面的逻辑代码
创建自定义UI组件 继承QLiveComponent

案列：
```kotlin
//自定义一个公告UI组件--参考原来的公告实现
class CustomNoticeView :FrameLayout, QLiveComponent {
    
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        //自己的公告布局
        LayoutInflater.from(context).inflate(R.layout.customnoticeview,this,true)
    }

    //绑定UI组件上下文 context中包涵UI实现安卓平台功能的字段如activity fragmentManager
    override fun attachKitContext(context: QLiveUIKitContext) {}
    //绑定房间客户端 通过client可以获取业务实现
    override fun attachLiveClient(client: QLiveClient) {}
    //进入回调 在这个阶段可以提前根据liveId提前初始化一些UI
    override fun onEntering(liveID: String, user: QLiveUser) { }
    
    //加入回调 房间加入成功阶段 已经拿到了QLiveRoomInfo
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        //设置房间公告文本 公告文本roomInfo中字段取出
       tvNotice.setText("房间公告："+roomInfo.notice)
    }
    //离开回调
    override fun onLeft() {
        //房间切换/离开 清空UI
        tvNotice.setText("")
    }
    // 销毁
    override fun onDestroyed() { }
    //安卓activity生命周期 安卓页面 onresume onpause等等
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {}
}
//自定义UI可以参考原来的实现修改成自定义实现
//提示：所有的UI组件不需要在activity绑定操作 只需要继承QLiveComponent就能完成所有工作
```
然后在拷贝布局文件里或者源码布局里替换原来内置的UI组件
```
 ....
 ....
  //原来的UI组件
  <com.qlive.uikitpublicchat.RoomNoticeView
                    android:layout_width="238dp"
                    android:layout_height="wrap_content"
                    android:layout_marginStart="8dp"
                    android:layout_marginBottom="2dp"
                    android:background="@drawable/kit_shape_40000000_6"
                    android:orientation="vertical"
                    android:paddingStart="8dp"
                    android:paddingTop="16dp"
                    android:paddingEnd="8dp"
                    android:paddingBottom="16dp"
                    android:textColor="#ffffff"
                    android:textSize="13sp"
                    tools:text="官方公告" />
   .... 
   ....

  //改成你自己的
  <CustomNoticeView
       android:layout_width="238dp"
       android:layout_height="wrap_content"/>

//提示不需要修改activity
```
#### 删除内置UI组件
直接在拷贝的布局文件或或者源码布局里里删除

#### 添加UI组件

```kotlin
class CustomView :FrameLayout, QLiveComponent {
   //  实现自己额外的多个UI布局
}
//在拷贝的布局文件里或者源码布局里你想要的位置添加即可
//提示不需要修改activity
```


#### 添加功能组件

功能组件只关心事件 没有附件到UI上 比如关心自己被踢事件-显示弹窗

自定义组件继承 QLiveComponent 处理自定义关心的事件
```
class CustomFunctionComponent : QLiveComponent {
    //
    lateinit var context: QLiveUIKitContext
    //绑定UI组件上下文
    override fun attachKitContext(context: QLiveUIKitContext) {
        this.context = context
    }
    //绑定房间客户端
    override fun attachLiveClient(client: QLiveClient) {
        
        //注册聊天室监听
        client.getService(QChatRoomService::class.java).addServiceListener(object :
            QChatRoomServiceListener {
            override fun onUserKicked(memberID: String) {
                if(memberID==meId){
                    //显示提示弹窗
                   showDialog( context.fragmentManager,"你被踢了")
                }
            }
        })
    }
    //进入回调
    override fun onEntering(liveId: String, user: QLiveUser) { }
    // 加入回调
    override fun onJoined(roomInfo: QLiveRoomInfo) {
    }
    //离开回调
    override fun onLeft() { }
    // 销毁
    override fun onDestroyed() { }
    //安卓activity生命周期
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {}
}

  //添加到房间业务
  roomPage.addFunctionComponent(CustomFunctionComponent())
```
方法2 修改源码

#### 自定义房间列表页面
如果需要将房间列表页面添加到你想要的页面比如app首页的viewpager切换

方式1 使用RoomListView
在想要添加的布局xml里面添加RoomListView

```
  <com.qlive.uikit.component.RoomListView
        android:id="@+id/roomListView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

```
在activivity代码里配置样式
```
//可选 替换列表item适配
roomListView .setRoomAdapter(new CustomAdapter)

//可选 设置列表数据为空的占位icon
roomListView.setEmptyPlaceholderIcon(R.id.emptyicon)

//可选 设置列表数据为空的占位提示
roomListView.setEmptyPlaceholderTips("房间列表为空")

//必选 启动 
roomListView.attachKitContext(new QUIKitContext(this,supportFragmentManager,this,this))
```

方式2
```
//使用 qlive数据api 获取房间等数据自定义UI
QLive.getRooms().listRoom(..）
QLive.getRooms().createRoom(..）
```
方式3
修改UIkit源码



### 使用内置七牛美颜（可选）
内置商汤美颜插件，依赖则有不依赖则没有
```java
拷贝源码模块uikit-beauty
implementation project(":uikit-uicomponnets:uikit-beauty")
```
- 联系七牛商务获取美颜认证lic文件 重命名SenseME.lic放在assets文件下->运行sdk已经带了美颜滤镜贴纸功能
- 修改美颜调节面板UI及拷贝购买的模型文件至uikit-beauty/assets



### 无UI
```java
无UI

//初始化
QLive.init(context ,new QTokenGetter(){
        //业务请求token的方法 
        void getTokenInfo( QLiveCallBack<String> callback){
             //当token过期后如何获取token
            GetTokenApi.getToken(callback);
        }
});


//登陆 登陆成功后才能使用qlive
QLive.auth(new QLiveCallBack<Void>{})

//可选 绑定用户资料 第一绑定后没有跟新个人资料可以不用每次都绑定
Map ext = new HashMap()
ext.put("vip","1"); // 可选参数，接入用户希望在直播间的在线用户列表/资料卡等UI中显示自定义字段如vip等级等等接入方的业务字段
 ext.put("xxx","xxx");//扩展多个字段
//跟新/绑定 业务端的用户信息
QLive.setUser(new QUserInfo( "your avatar","your nickname", ext) ,new QLiveCallBack<Void>{});


//创建房间
QRooms rooms = QLive.getRooms();
QCreateRoomParam param = new QCreateRoomParam();
param.setTitle("xxxtitle");
rooms.createRoom( param, new QLiveCallBack<QLiveRoomInfo>{
    void onSuccess(QLiveRoomInfo roomInfo){}
    void onError(int code, String msg) {}
});

// 主播推流
//创建推流client
 QPusherClient client = QLive.createPusherClient();
 
QMicrophoneParam microphoneParams = new QMicrophoneParam();
microphoneParam.setSampleRate(48000);
//启动麦克风模块
client.enableMicrophone(microphoneParam);

QCameraParam cameraParam = new QCameraParam()
cameraParam.setFPS(15)
//启动摄像头模块
client.enableCamera(cameraParam,findViewById(R.id.renderView));

//注册房间端监听
client.setLiveStatusListener(new: QLiveStatusListener{})

//加入房间
client.joinRoom( roomID, new QLiveCallBack<QLiveRoomInfo> {
     void onSuccess(QLiveRoomInfo roomInfo){}
     void onError(int code, String msg) {}
});

//关闭
client.closeRoom(new QLiveCallBack<Void> {
     void onSuccess(Void) {}
     void onError(int code, String msg) {}
 });
//销毁
client.destroy();



//用户拉流房间
QPlayerClient client = QLive.createPlayerClient();

//注册房间端监听
client.setLiveStatusListener(new: QLiveStatusListener{})

//加入房间
client.joinRoom( roomID, new QLiveCallBack<QLiveRoomInfo> {
     override void onSuccess(QLiveRoomInfo roomInfo){}
     override void onError(int code, String msg) {}
 });

//播放
client.play(findViewById(R.id.renderView));

//离开房间
client.leaveRoom(new QLiveCallBack<Void> {
    void onSuccess(Void) {}
    void onError(int code, String msg) {}
});
//销毁
client.destroy(); 
```

### 使用插件服务

```
client.getService(QxxxService::class.java) //获取某个业务插件服务
//案列
client.getService(QPKService::class.java)?.start(20 * 1000, receiverRoomID, receiver.userId, null,object : QLiveCallBack<QPKSession> {})
```
### 无UISDK实现连麦

```kotlin
//邀请监听
private val mInvitationListener = object : QInvitationHandlerListener {
    //收到邀请
    override fun onReceivedApply(qInvitation: QInvitation) {
        //todo 显示申请弹窗  qInvitation.getInitiator()获取到申请方资料 显示UI 
        // 点击按钮  拒绝操作
        client!!.getService(QLinkMicService::class.java) .invitationHandler.reject(qInvitation.invitationID, null,callBack)
        //点击按钮 接受操作 
        client!!.getService(QLinkMicService::class.java) .invitationHandler.accept(qInvitation.invitationID, null,callBack)
        //收到对方取消    
        override fun onApplyCanceled(qInvitation: QInvitation) {}
        //发起超时对方没响应
        override fun onApplyTimeOut(qInvitation: QInvitation) {}
        //对方接受
        override fun onAccept(qInvitation: QInvitation) {
            //对方接受后调用开始上麦 传摄像头麦克参数 自动开启相应的媒体流
            client?.getService(QLinkMicService::class.java)?.audienceMicHandler
                ?.startLink( null, QCameraParam() , QMicrophoneParam(),callback )
        }
        //对方拒绝
        override fun onReject(qInvitation: QInvitation) { }
    }

    //麦位麦位监听
    private val mQLinkMicServiceListener = object :  QLinkMicServiceListener {
        //有人上麦了
        override fun onLinkerJoin(micLinker: QMicLinker) {

            //麦上用户和主播 设置这个用户预览 直播用户无需设置
            val preview =QPushTextureView(context)
            linkService.setUserPreview(micLinker.user?.userId ?: "", preview)
            //跟新连麦UI 如果要显示头像 micLinker里取到上麦者资料 
        }
        //有人下麦了
        override fun onLinkerLeft(micLinker: QMicLinker) {
            //移除设置的预览UI
            //跟新连麦UI 比如去掉麦上头像
        }

        override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {
            //跟新连麦UI 
        }

        override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
            //跟新连麦UI
        }
        //某个用户被踢麦
        override fun onLinkerKicked(micLinker: QMicLinker, msg: String) { }

        override fun onLinkerExtensionUpdate(micLinker: QMicLinker, extension: QExtension) {}
    }
    //混流适配 房主负责混流
    private val mQMixStreamAdapter =
        object : QAnchorHostMicHandler.QMixStreamAdapter {
            /**
             * 连麦开始如果要自定义混流画布和背景
             * 返回空则主播推流分辨率有多大就多大默认实现
             * @return
             */
            override fun onMixStreamStart(): QMixStreamParams? {
                return null
            }

            /**
             * 混流布局适配
             * @param micLinkers 所有连麦者
             * @return 返回重设后的每个连麦者的混流布局
             */
            override fun onResetMixParam(
                micLinkers: MutableList<QMicLinker>,
                target: QMicLinker,
                isJoin: Boolean
            ): MutableList<QMergeOption> {
                //将变化后的麦位换算成连麦混流参数  
                //比如 案例
                val ops = ArrayList<QMergeOption>()
                val lastY = 200
                micLinkers.forEachIndex {index, linker ->
                    ops.add(QMergeOption().apply {
                        uid = linker.user.userId
                        cameraMergeOption = CameraMergeOption().apply {
                            isNeed = true
                            x = 720 //麦位x
                            y = lastY + 180 + 15 //每个麦位 依次往下排15个分辨率间距
                            z = 0
                            width = 180
                            height = 180
                        }
                        microphoneMergeOption = MicrophoneMergeOption().apply {
                            isNeed = true
                        }
                    })
                    lastY=lastY+180+15
                }
            }
        }

    //观众端连麦器监听
    private val mQAudienceMicHandler = object : QAudienceMicHandler.QLinkMicListener {
        override fun onRoleChange(isLinker: Boolean) {
            if (isLinker) {
                //我上麦了 切换到了连麦模式
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    //对原来麦上的人设置预览
                }
            } else {
                //我下麦了 切换拉流模式
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    //移除对原来设置麦位移除设置预览view
                    removePreview(mLinkerAdapter.indexOf(it), it)
                }
            }
        }
    }

//主播设置混流适配  
    client!!.getService(QLinkMicService::class.java).anchorHostMicHandler.setMixStreamAdapter( mQMixStreamAdapter  )
//观众设置观众连麦处理监听
    client!!.getService(QLinkMicService::class.java).audienceMicHandler.addLinkMicListener( mQAudienceMicHandler )
//主播和观众都关心麦位监听 
    client!!.getService(QLinkMicService::class.java).addMicLinkerListener(mQLinkMicServiceListener)
//注册邀请监听
    client.getService(QLinkMicService::class.java).invitationHandler.addInvitationHandlerListener( mInvitationListener )

    //点击某个按钮 发起对某个主播申请 或者主播邀请用户
    client!!.getService(QLinkMicService::class.java).invitationHandler.apply(10 * 1000, room.liveID, room.anchor.userId, null,callback )
```

```kotlin
如果不使用内置的邀请系统 比如外接匹配系统或者直接上麦不需要邀请
//todo
// 别的邀请或者匹配
//直接调用上麦方法
client?.getService(QLinkMicService::class.java)?.audienceMicHandler
    ?.startLink( null, QCameraParam() , QMicrophoneParam(),callback )

```

### 无UISDK 实现PK

```kotlin
//邀请监听
private val mPKInvitationListener = object : QInvitationHandlerListener {
    //收到邀请
    override fun onReceivedApply(pkInvitation: QInvitation) {
        //拒绝操作
        client!!.getService(QPKService::class.java) .invitationHandler.reject(pkInvitation.invitationID, null,callBack)
        //接受 
        client!!.getService(QPKService::class.java) .invitationHandler.accept(pkInvitation.invitationID, null,callBack)
        //收到对方取消    
        override fun onApplyCanceled(pkInvitation: QInvitation) {}
        //发起超时对方没响应
        override fun onApplyTimeOut(pkInvitation: QInvitation) {}
        //对方接受
        override fun onAccept(pkInvitation: QInvitation) {
            //对方接受后调用开始pk
            client?.getService(QPKService::class.java)?.start(20 * 1000, invitation.receiverRoomID, invitation.receiver.userId, null,callBack)
        }
        //对方拒绝
        override fun onReject(pkInvitation: QInvitation) { }
    }

    //pk监听
    private val mQPKServiceListener = object : QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            //主播设置对方主播预览
            client.getService(QPKService::class.java).setPeerAnchorPreView(findviewbyid(...))
            //主播和观众都显示pk覆盖UI
        }
        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            //主播和观众都隐藏pk覆盖UI
        }
        override fun onStartTimeOut(pkSession: QPKSession) {}
        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {
        }
    }
    //混流适配
    private val mQPKMixStreamAdapter = object :  QPKMixStreamAdapter {
        //pk对方进入了 返回混流参数（同连麦）
        override fun onPKLinkerJoin(pkSession: QPKSession): MutableList<QMergeOption> {
            return LinkerUIHelper.getPKMixOp(pkSession, user!!)
        }
        //pk开始了 如果修改整个直播混流面板（同连麦）
        override fun onPKMixStreamStart(pkSession: QPKSession): QMixStreamParams {
            return QMixStreamParams()
        }

    }

//添加pk监听
    client!!.getService(QPKService::class.java).addServiceListener(mQPKServiceListener)
//主播注册混流适配   
    client!!.getService(QPKService::class.java).setPKMixStreamAdapter(mQPKMixStreamAdapter)
//注册邀请监听
    client.getService(QPKService::class.java).invitationHandler.addInvitationHandlerListener( mPKInvitationListener )

    //点击某个按钮 发起对某个主播邀请
    client!!.getService(QPKService::class.java).invitationHandler.apply(10 * 1000, room.liveID, room.anchor.userId, null,callback )

```
```kotlin
如果不使用内置的邀请系统 比如外接匹配pk系统
//todo
        别的邀请或者匹配
//直接调用开始PK方法
client?.getService(QPKService::class.java)?.start(20 * 1000, invitation.receiverRoomID, invitation.receiver.userId, null,callBack)

```


# 接口说明
## 初始化
```java
class QLive {
    static void init(Context context, QTokenGetter tokenGetter); // 初始化
    static void auth(QLiveCallBack<Void> callBack)//认证/登陆
    static void setUser(QUserInfo userInfo ,QLiveCallBack<Void> callBack); //绑定用户信息
    static QPusherClient createPusherClient();  //创建主播端
    static QPlayerClient createPlayerClient();  //创建观众端
    static QLiveUIKit getLiveUIKit();           //获得uikit
    static QRooms getRooms();                   //获得直播场景
}

class QLiveUIKit{
    <T extends QPage> T getPage(Class<T> pageClass); //获取内置UI页面
    void launch(Context context);        //启动 跳转直播列表页面
}

//获取用户token的回调
interface QTokenGetter{
    void getTokenInfo( QLiveCallBack<String> callback);//实现获取token的方法
}

class QUserInfo{
    String avatar;
    String nick;
    Map<String,String> extension; //扩展字段
}

class QRooms{
    void createRoom(QCreateRoomParam param, QLiveCallBack<QLiveRoomInfo> callBack);   //创建房间
    void deleteRoom(String roomID, QLiveCallBack<void> callBack);                    //删除房间
    void listRoom( int pageNumber, int pageSize, QLiveCallBack<List<QLiveRoomInfo>> callBack); //房间列表
    void getRoomInfo(String roomID, QLiveCallBack<List<QLiveRoomInfo>> callBack);    //根据ID搜索房间信息
}

class QLiveRoomInfo {
    String roomID;                 //房间ID
    String title;                  //标题
    String notice;                 //公告
    String coverURL;               //封面
    Map<String, String> extension; //房间扩展字段
    QLiveUser anchor;              //主播信息
    String roomToken;
    String pkID;                   //当前房间正在进行的PK场次
    long onlineCount;              //在线人数
    long startTime;                //开始时间戳
    long endTime;                  //结束时间戳
    String chatID;                 //聊天室ID
    String pushURL;                 //推流地址
    String hlsURL;                 //拉流地址
    String rtmpURL;                //拉流地址
    String flvURL;                 //拉流地址
    double pv;
    double uv;
    int totalCount;                //总人数
    int totalMics;                 //当前连麦数量 
    int liveStatus;                //房间状态 0已创建 1发布 2关闭
    int anchorStatus;              //主播状态 0离线 1在线
}

//创建房间参数
class QCreateRoomParam {
    String title;                      //标题
    String notice;                     //公告
    String coverURL;                   //封面 
    HashMap<String,String> extension; //扩展字段
}
```

## 主播/观众 客户端

```java
//主播客户端
class QPusherClient {

    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setLiveStatusListener(QLiveStatusListener liveStatusListener);          //房间状态监听
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间
    void closeRoom( QLiveCallBack<Void> callBack);                               //关闭房间
    void destroy();                                                              //销毁

    void setConnectionStatusLister(QConnectionStatusLister connectionStatusLister);//推流连接状态监听
    void enableCamera(QCameraParam cameraParam,QRenderView renderView);          //启动视频采集 和预览
    void enableMicrophone(QMicrophoneParam microphoneParam);                     //启动麦克参数
    void switchCamera(QLiveCallBack<QCameraFace> callBack);                        //切换摄像头
    void muteCamera(boolean muted,QLiveCallBack<Boolean> callBack);                //禁/不禁用本地视频流
    void muteMicrophone(boolean muted,QLiveCallBack<Boolean> callBack);            //禁/不禁用本地麦克风流
    void setVideoFrameListener(QVideoFrameListener frameListener);                 //设置本地摄像头数据监听
    void setAudioFrameListener(QAudioFrameListener frameListener);                 //设置本地音频数据监听
}

//观众客户端
class QPlayerClient {

    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setLiveStatusListener(QLiveStatusListener liveStatusListener);          //房间状态监听
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间
    void leaveRoom( QLiveCallBack<Void> callBack);                               //关闭房间
    void destroy();                                                              //销毁
    void play(QRenderView renderView);                                           //绑定播放器渲染视图

    void setPlayerEventListener(QPlayerEventListener playerEventListener);       //设置拉流端事件回调  
}

//直播间状态变化回调
interface QLiveStatusListener {
    void onLiveStatusChanged(QLiveStatus liveStatus);                             //直播间状态变化 业务状态
}

//直播状态枚举  低代码服务端定义的业务状态
enum QLiveStatus {
    PREPARE,       //直播准中 （已经创建）
    ON,            // 直播间已发布  （已经发布，可以开播和拉流
    ANCHOR_ONLINE,  //主播上线
    ANCHOR_OFFLINE, //主播离线
    OFF            //直播间关闭
};

//摄像头状态枚举
enum QCameraFace{
    FRONT,BACK
};

//主播推流连接状态
interface QConnectionStatusLister{
    void onConnectionStatusChanged(QRoomConnectionState state);  //rtc推流链接状态
}

//麦克风参数 参数都有默认值
class QMicrophoneParam {
    int sampleRate = 48000;
    int bitsPerSample = 16;
    int channelCount = 1;
    int bitrate = 64000;
}

//摄像头参数 
class QCameraParam {
    int width = 720;
    int height = 1280;
    int FPS = 25;
    int bitrate = 1000;
}

//房间里的用户
class QLiveUser {
    String userID;
    String imUID;  //im账户ID 用于向他发c2c
    String avatar;
    String nick;
    Map<String,String> extension; //用户扩展字段 
}

//推流器连接状态
enum QRoomConnectionState{
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    RECONNECTED;
}

//拉流播放器回调
interface QPlayerEventListener {
    void onPrepared(int preparedTime); //拉流器准备中
    void onInfo(int what, int extra);  //拉流器信息回调
    void onBufferingUpdate(int percent); //拉流缓冲跟新
    void onVideoSizeChanged(int width, int height); //视频尺寸变化回调
    void onError(int errorCode);
}
```

## QLiveService

```java
//连麦服务
interface QLinkMicService extends QLiveService {
    void removeServiceListener(QLinkMicServiceListener listener);
    void addServiceListener(QLinkMicServiceListener listener);                        //添加连麦麦位监听
    List<QMicLinker> getAllLinker();                                                  //所有麦位 
    void setUserPreview(String uID, QRenderView preview);                             //设置某个连麦者的预览  
    void kickOutUser(String uID, String msg, QLiveCallBack<Void> callBack);           // 踢麦
    void updateExtension(QMicLinker micLinker, QExtension extension,QLiveCallBack<Void> callBack); //跟新麦位扩展字段
    QAudienceMicHandler getAudienceMicHandler();                                        // 用户连麦器 
    QAnchorHostMicHandler getAnchorHostMicHandler();                                    //主播连麦器
    QInvitationHandler getInvitationHandler();                                        //邀请处理器   
}

interface QLinkMicServiceListener{
    void onLinkerJoin(QMicLinker micLinker);                                            //有人上麦回调
    void onLinkerLeft(QMicLinker micLinker);                                             //有人下麦回调
    void onLinkerMicrophoneStatusChange(QMicLinker micLinker);                           //麦上麦克风变化回调
    void onLinkerCameraStatusChange(QMicLinker micLinker);                               //麦上摄像头变化回调
    void onLinkerKicked(QMicLinker micLinker, String msg);                               //踢人事件回调
    void onLinkerExtensionUpdate(QMicLinker micLinker, QExtension extension);            //麦上扩展字段变化回调 
}

//主播连麦处理器 主播操作
class QAnchorHostMicHandler {
    void setMixStreamAdapter(QMixStreamAdapter mixStreamAdapter);                       //设置混流适配器
}

//连麦混流适配器
interface QMixStreamAdapter {
    QMixStreamParams onMixStreamStart();//连麦开始如果要自定义混流画布和背景 返回空则主播推流分辨率有多大就多大默认实现
    /**
     * 混流布局适配
     * @param micLinkers 变化后所有连麦者
     * @param target  当前变化的连麦者
     * @param isJoin  当前变化的连麦者是新加还是离开
     * @return 返回重设后的每个连麦者的混流布局 = 返回变化的所有麦位的 x y w h 参数
     */
    List<QMergeOption> onResetMixParam(List<QMicLinker> micLinkers, QMicLinker target, boolean isJoin); //混流适配 将变化后的麦位列表视频成混流参数

}
//用户连麦处理器
class QAudienceMicHandler{
    void removeListener(QLinkMicListener listener);
    void addListener(QLinkMicListener listener);

    /**
     * 开始上麦
     * @param extension        麦位扩展字段
     * @param cameraParams     摄像头参数 空代表不开 
     * @param microphoneParams 麦克参数  空代表不开
     * @param callBack         上麦成功失败回调
     */
    void startLink(HashMap<String,String> extension,CameraParams cameraParams, MicrophoneParams microphoneParams, QLiveCallBack<Void> callBack  );//上麦 
    void stopLink();                                                               //下麦  
    void switchCamera(QLiveCallBack<QCameraMode> callBack);                        //切换摄像头
    void muteCamera(boolean muted,QLiveCallBack<Boolean> callBack);                //禁/不禁用本地视频流
    void muteMicrophone(boolean muted,QLiveCallBack<Boolean> callBack);            //禁/不禁用本地麦克风流

    setVideoFrameListener(QVideoFrameListener frameListener);
    setAudioFrameListener(QAudioFrameListener frameListener);

    interface QLinkMicListener{
        onRoleChange(boolean isLinker);                                             //本地角色变化 自己上麦下麦后本地角色变化
    }
}
```

```java
//PK服务
interface QPKService extends QLiveService{
    void removeServiceListener(QPKServiceListener pkServiceListener);
    void addServiceListener(QPKServiceListener pkServiceListener);
    void setMixStreamAdapter(QPKMixStreamAdapter mixAdapter);                       //设置pk混流适配器

    /**
     * 开始pk
     *
     * @param timeoutTimestamp 等待对方流超时时间 单位毫秒 超时后不再响应对方发流
     * @param receiverRoomID   接受方所在房间ID
     * @param receiverUID      接收方用户ID
     * @param extension        扩展字段
     * @param callBack         操作回调函数
     */
    void start(long timeoutTimestamp ,String receiverRoomID, String receiverUID, HashMap<String, String> extension, QLiveCallBack<QPKSession> callBack);
    void stop(QLiveCallBack<Void> callBack);                                        //结束pk
    void setPeerAnchorPreView(QRenderView view);                                    //设置对方预览
    QInvitationHandler getInvitationHandler();                                      //获取连麦邀请处理器
}

//pk监听
interface QPKServiceListener{
    void onStart(QPKSession pkSession);                                             //pk开始
    void onStop(QPKSession pkSession,int code,String msg);                          //pk结束
    void onStartTimeOut(QPKSession pkSession);                                      //开始超时 与对方主播建立链接超时      
}
//pk混流适配
interface QPKMixStreamAdapter{
    QMixStreamParam onPKMixStreamStart(QPKSession pkSession);   //pk 开始返回 混流画布参数
    List<QMergeOption> onPKLinkerJoin(QPKSession pkSession);    //PK 开始如何混流 返回主播和对方主播的 x y z w h参数
    List<QMergeOption> onPKLinkerLeft();                        //PK结束如果还有其他普通连麦者如何混流 如果没有则不回调自动恢复单路转推
    QMixStreamParams onPKMixStreamStop()//当pk结束后如果还有其他普通连麦者 如何混流 如果pk结束后没有其他连麦者 则不会回调 返回空则默认之前的不变化
}

//pk会话
class QPKSession {
    String sessionID;
    QLiveUser initiator; //发起方
    QLiveUser receiver;  //接收方
    String initiatorRoomID; //发起方所在房间ID
    String receiverRoomID;  //接收方所在房间ID
    Map<String, String> extension;  //PK扩展字段
    int status;                     //状态
    long startTimeStamp;            //开始时间戳
}
```

```java

//呼叫邀请信令
class QInvitationHandler{
    void apply(long expiration, String receiverRoomID,String receiverUID, HashMap<String,String>extension, QLiveCallBack<QInvitation> callBack);//发起邀请或者申请
    void cancelApply(int invitationID,QLiveCallBack<Void> callBack);                                  //取消申请
    void accept(int invitationID, HashMap<String,String> extension,QLiveCallBack<Void> callBack);    //接受
    void reject(int invitationID, HashMap<String,String> extension,  QLiveCallBack<Void> callBack ); //拒绝
    void removeInvitationHandlerListener(QInvitationHandlerListener listener);                        //添加邀请监听
    void addInvitationHandlerListener(QInvitationHandlerListener listener);
}

interface QInvitationHandlerListener{
    void onReceivedApply(QInvitation invitation);   //收到申请/邀请                     
    void onApplyCanceled(QInvitation invitation);   //对方取消申请 
    void onApplyTimeOut(QInvitation invitation);    //申请/邀请 超时
    void onAccept(QInvitation invitation);          //被接受
    void onReject(QInvitation invitation);          //被拒绝
}

class QInvitation{
    QLiveUser initiator;            //邀请方
    QLiveUser receiver;             //接受方
    String initiatorRoomID;         //邀请方所在房间ID 
    String receiverRoomID;          //接收方所在房间ID 
    HashMap<String,String> extension; //扩展字段

    @JSONField(serialize = false)
    int invitationID;               //邀请ID   
}
```
```java

//用户的连麦混流参数
class QMergeOption {
    String uID;                                 //用户ID  
    CameraMergeOption  cameraMergeOption;       //摄像头参数       
    MicrophoneMergeOption microphoneMergeOption;//麦克风参数

    //用户的摄像头连麦混流参数
    class CameraMergeOption  {
        boolean isNeed = true; //是否需要
        int x = 0;
        int y = 0;
        int z = 0;
        int width = 0;
        int height = 0;
    }
    ///用户麦克风连麦混流参数
    class MicrophoneMergeOption  {
        boolean isNeed = true;
    }
}
//画布
class QMixStreamParam{
    int mixStreamWidth;  //画布宽
    int mixStringHeight; //画布高
    int mixBitrate;      //码率
    int FPS;             //帧率
}
```

```java
interface QChatRoomService extends QLiveService {
    void removeServiceListener(QChatRoomServiceListener chatServiceListener);
    void addServiceListener(QChatRoomServiceListener chatServiceListener);
    void sendCustomC2CMsg(String msg, String memberID, QLiveCallBack<Void> callBack);  //发c2c
    void sendCustomGroupMsg(String msg, QLiveCallBack<Void> callBack);                 //发群消息
    void kickUser(String msg, String memberID, QLiveCallBack<Void> callBack);          //踢出聊天室
    void muteUser(boolean isMute ,String msg, String memberID, long duration ,QLiveCallBack<Void> callBack); //禁言
    void addAdmin( String memberID, QLiveCallBack<Void> callBack);                      //添加管理员
    void removeAdmin(String msg, String memberID, QLiveCallBack<Void> callBack);        //移除管理员
}

//聊天室回调
interface QChatRoomServiceListener{
    void onUserJoin(String memberID);                                    //有人加入聊天室
    void onUserLeft(String memberID);                                    //有人离开聊天室
    void onReceivedC2CMsg(String msg, String fromID, String toID);       //收到自定义c2c 
    void onReceivedGroupMsg(String msg, String fromID, String toID);     //收到自定义聊天室消息
    void onUserKicked(String memberID);                                  //有人被踢出聊天室
    void onUserBeMuted(boolean isMute, String memberID, long duration);  //有人被禁言
    void onAdminAdd(String memberID);                                    //有人被设置为管理员
    void onAdminRemoved(String memberID, String reason);                 //有人被移除管理员    
}
```

```java
//房间服务
interface QRoomService {
    void removeServiceListener(QRoomServiceListener listener);
    void addServiceListener(QRoomServiceListener listener);
    QLiveRoomInfo getRoomInfo();
    void getRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);                     //刷新房间信息
    void updateExtension(QExtension extension, QLiveCallBack<void> callBack);    //跟新房间扩展字段
    void getOnlineUser(QLiveCallBack<List<QLiveUser>> callBack);                 //当前房间现在用户

}

interface QRoomServiceListener{
    void onRoomExtensionUpdate(QExtension extension);  //房间扩展字段跟新
}
```

```java
//公屏消息服务
interface QPublicChatService extends QLiveService{
    void addServiceLister(QPublicChatServiceLister lister);
    void removeServiceLister(QPublicChatServiceLister lister);
    void sendPublicChat(String msg, QLiveCallBack<QPublicChat> callBack);         //发送公屏聊天消息
    void sendWelCome(String msg, QLiveCallBack<QPublicChat> callBack);            //发送进入房间消息
    void sendByeBye(String msg, QLiveCallBack<QPublicChat> callBack);            //发送离开房间消息
    void sendLike(String msg, QLiveCallBack<QPublicChat> callBack);               //发送点赞消息
    void sendCustomPubChat(String action, String msg,  QLiveCallBack<QPublicChat> callBack); //发送自定义要显示在公屏上的消息
    void pubMsgToLocal(QPublicChat publicchat);                                    //往本地公屏插入消息不发送到对端
}

interface QPublicChatServiceLister {
    void onReceivePublicChat(QPublicChat pubChat); //收到公屏消息
}

//公屏消息实体
class QPublicChat {
    String action;      //公聊消息类型
    QLiveUser sendUser; //发送者
    String content;     //内容
    String senderRoomID;//房间ID
}

```
```java
//弹幕服务
interface QDanmakuService extends QLiveService {
    void addServiceLister(QDanmakuServiceListener listener);
    void removeServiceLister(QDanmakuServiceListener listener);
    void sendDanmaku(String msg, HashMap<String,String> extension, QLiveCallBack<QDanmaku> callBack);//发弹幕
}
//弹幕监听
interface QNDanmakuServiceListener {
    void onReceiveDanmaku(QDanmaku danmaku);  //收到弹幕消息
}
//弹幕实体
class QDanmaku {
    QLiveUser sendUser;
    String content;
    String senderRoomID;
    HashMap<String, String> extension;
}

```

## UIKIT

```java
//直播间列表
class RoomListPage extends QPage {
    void setCustomLayoutId(int layoutID); //替换整体布局
}

//直播间页面
class RoomPage {

    void  setPlayerCustomLayoutId(int layoutID); //替换整体布局 替换观众端
    void setAnchorCustomLayoutId(int layoutID); //替换整体布局 替换主播端

    ShowPKApplyFunctionComponent showPKApplyComponent ;//主播收到pk邀请 展示弹窗 事件监听功能组件 
    ShowLinkMicApplyFunctionComponent showLinkMicApplyComponent ;//主播收到连麦申请 展示弹窗 事件监听功能组件
    PlayerShowBeInvitedComponent playerShowBeInvitedComponent; //用户收到主播连麦邀请 展示弹窗

    addFunctionComponent(QRoomComponent component); //注册自定义功能组件

    //根据QLiveRoomInfo自动判断跳转主播页面还是观众页面
    void startRoomActivity(@NotNull Context context, @NotNull QLiveRoomInfo roomInfo, @Nullable QLiveCallBack callBack);
    //跳转观众直播间
    void startPlayerRoomActivity(@NotNull Context context, @NotNull String liveRoomId, @Nullable QLiveCallBack callBack);
    //跳转已经存在的主播直播间
    void startAnchorRoomActivity(@NotNull Context context, @NotNull String liveRoomId, @Nullable QLiveCallBack callBack);
    // 跳转主播开播预览页面
    void startAnchorRoomWithPreview(@NotNull Context context, @Nullable QLiveCallBack callBack);
}
```

```Java
//kit基础类说明 非dom
//组件
interface QComponent {
    //绑定UI上下文
    void attachKitContext(QUIKitContext context);
}
//房间内的UI组件
interface QLiveComponent  {

    //生命周期 绑定UI上下文
    void attachKitContext(QLiveUIKitContext context);
    //生命周期  绑定房间客户端
    void attachLiveClient(QLiveClient client);
    //生命周期  房间正在进入
    void onEntering(String roomID,QLiveUser user);                         //正在加入房间
    //生命周期  房间加入成功
    void onJoined(QRoomInfo roomInfo);                                     //加入了某个房间  
    //生命周期  当前房间已经离开 - 我是观众-离开 我是主播对应关闭房间
    void onLeft();                                                         //离开了某个房间 
    //生命周期  client销毁 == 房间页面将要退出
    void onDestroyed();                                                    //client销毁
}

//内置房间里的UI型号组件基类
class QLiveView{
    <T extends QLiveComponent> void replace(Class<T> replaceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);                             //移除/禁用 
}
//内置UI型号组件基类
class QView{
    <T extends QComponent> void replace(Class<T> replaceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);
}
//内置功能型号组件 - 处理事件没有UI
class QLiveFunctionComponent{
    <T extends QLiveComponent> void replace(Class<T> replaceClass); //替换成你的处理器
    void setIsEnable(boolean isEnable);
}

/**
 * uikit UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 */
class QUIKitContext(
        val androidContext: Context,//安卓上下文
        val fm: FragmentManager,   //显示弹窗上下文
        val currentActivity: Activity,  //当前activity
        val lifecycleOwner: LifecycleOwner, //页面生命周期
)

/**
 * uikit 房间里的UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 * 2能获取房间client 主要资源和关键操作
 */
class QLiveUIKitContext(
        val androidContext: Context, //安卓上下文
        val fm: FragmentManager,     //显示弹窗上下文
        val currentActivity: Activity, //当前activity
        val lifecycleOwner: LifecycleOwner, //页面生命周期
        val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit, //离开房间操作
        val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit,//创建并加入操作
        val getPlayerRenderViewCall: () -> QPlayerRenderView?, //获取当前播放器预览窗口
        val getPusherRenderViewCall: () -> QPushRenderView?,  //获取推流预览窗口
        )
```


