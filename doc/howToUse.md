
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
                                                    | 
                                                    +--->  QShoppingService //电商购物服务 

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
implementation 'com.squareup.okhttp3:okhttp:4.2.2' //okhttp 4版本以上 必选
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9' //kotlin协程 必选



//以下为 UIkit 的依赖包 不使用UIkit则不需要
implementation project(':app-sdk:qlive-sdk-uikit')  //UIkit包
//谷歌官方UI组件
implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.3.0'
implementation 'androidx.recyclerview:recyclerview:1.1.0'
implementation 'androidx.appcompat:appcompat:1.2.0'
implementation 'androidx.cardview:cardview:1.0.0'
//UIkit依赖两个知名的开源库
implementation 'com.github.bumptech.glide:glide:4.11.0' //图片加载
implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.44' //列表适配器

```
> UIkit也可以直接使用源码模块-可直接修改代码
> #### implementation project(':liveroom-uikit')

3 混淆配置
如果你的项目需要混淆 [qlivesdk混淆配置参考](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/app/proguard-rules.pro
)


## 使用说明
### UIKIT
```kotlin
//初始化
QLive.init(this, QSdkConfig()
    ) { callback ->
     //业务方如何获取token  在token过期和登陆时候会回调该方法
      getLoginToken(callback)
    }

//登陆 登陆成功后才能使用qlive
QLive.auth(object : QLiveCallBack<Void> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: Void?) {
    }
})

//可选 绑定用户资料 第一次绑定后没有跟新个人资料可不用每次都绑定 
//也可以在服务端绑定用户 服务端也可以调用
val ext =  HashMap()
ext.put("vip","1"); // 可选参数，接入用户希望在直播间的在线用户列表/资料卡等UI中显示自定义字段如vip等级等等接入方的业务字段
ext.put("xxx","xxx");//扩展多个字段
//跟新/绑定 业务端的用户信息
QLive.setUser(QUserInfo( "your avatar","your nickname", ext) ,object :  QLiveCallBack<Void>{});

val liveUIKit = QLive.getLiveUIKit()
//跳转到直播列表页面
liveUIKit.launch(context)
```
> 登陆成功后才能使用sdk 

### 自定义UI

![alt ](http://qrnlrydxa.hn-bkt.clouddn.com/qlive5.png)


**直接修改开源代码**
uikit使用源码依赖，直接修改源码
> 优点：快捷
> 缺点：官方更新UI逻辑代码后不方便同步

**无侵入式自定义UI**
uikit使用sdk依赖


拷贝布局文件--只需拷贝需要修改的页面，拷贝至接入的工程并且重新命名
- kit_activity_room_player.xml //[观众直播间布局 ](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_player.xml)
- kit_activity_room_pusher.xm  //[主播直播间布局](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_pusher.xml)
- kit_activity_room_list.xml   //[房间列表页布局](https://github.com/pili-engineering/QNLiveKit_Android/blob/main/liveroom-uikit/src/main/res/layout/kit_activity_room_list.xml)

clear重新编译编译-->androidStudio预览看到如上效果图

- 1修改拷贝文件的布局任意属性，比如边距，文本颜色，样式等等
- 2调用替换布局文件
```kotlin
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
```xml
 
    <!--   找到房间背景图-->
 <com.qlive.uikitcore.QKitImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        android:src="@drawable/kit_dafault_room_bg" />

    <!--   替换-->
 <com.qlive.uikitcore.QKitImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        android:src="@drawable/my_room_bg" />

```
> tip: 所有的安卓自带基础UI都可以修改属性 如边距,父容器排列，文本颜色等等

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
```xml
  
    <!--   原来的UI组件-->
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

    <!--   改成你自己的-->
  <CustomNoticeView
       android:layout_width="238dp"
       android:layout_height="wrap_content"/>
```
> 提示不需要修改activity

#### 删除内置UI组件
直接在拷贝的布局文件或或者源码布局里里删除

案列：去掉pk功能
在xml里直接删除开始pk按钮

```xml
     <!--    去掉开始pk按钮-->
    <com.qlive.uikitpk.StartPKView
      android:layout_width="wrap_content"
      android:layout_height="wrap_content" />

    <!--    去掉pk预览-->
   <com.qlive.uikitpk.PKAnchorPreview
      android:layout_width="match_parent"
      android:layout_height="match_parent" />
```
> UI插件的增删改不需要修改activity

#### 添加UI组件

```kotlin
class CustomView :FrameLayout, QLiveComponent {
   //  实现自己额外的多个UI布局
}
//在拷贝的布局文件里或者源码布局里你想要的位置添加即可

```

#### 添加功能组件

功能组件只关心事件 没有附件到UI上 比如关心自己被踢事件-显示弹窗

自定义组件继承 QLiveComponent 处理自定义关心的事件
```kotlin
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

```xml
   <!--  empty_placeholder_tips   没有数据的占位提示-->
   <!--  empty_placeholder_no_net_icon     没有网络的占位图片-->
   <!--  empty_placeholder_icon    没有数据的占位图片-->
  <com.qlive.uikit.component.RoomListView
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:empty_placeholder_icon="@drawable/kit_pic_empty"
       app:empty_placeholder_no_net_icon="@drawable/kit_pic_empty_network"
       app:empty_placeholder_tips="空空如也" />

```
在你的activivity代码里配置样式
```kotlin
//可选 替换列表item适配
roomListView.setRoomAdapter( CustomAdapter())
//必选 启动 
roomListView.attachKitContext( QUIKitContext(this,supportFragmentManager,this,this))
```

方式2
```kotlin
//使用 qlive数据api 获取房间等数据自定义UI
QLive.getRooms().listRoom(..）
QLive.getRooms().createRoom(..）
```
方式3
修改UIkit源码


### 使用美颜插件（可选）
```
拷贝源码模块uikit-beauty并且添加依赖
implementation project(":uikit-uicomponnets:uikit-beauty")
```
- (必选) 联系七牛商务获取美颜认证lic文件 重命名SenseME.lic放在assets文件下->运行sdk已经带了美颜滤镜贴纸功能
- (可选 -自定义调节UI面板) 修改美颜调节面板UI及拷贝购买的额外的素材文件/或删除原有用不着的素材至uikit-beauty/assets
- (可选 -自定义显示美颜弹窗按钮)uikit中调用 BeautyComponent.showBeautyEffectDialog() 即可显示美颜特效弹窗
- (可选 -自定义显示贴纸弹窗按钮)uikit中调用 BeautyComponent.showBeautyStickDialog()  即可显示美颜贴纸弹窗

### 外接其他美颜
```kotlin
client.setVideoFrameListener(object:QVideoFrameListener{
  //拿到帧回调即可以使用其他美颜sdk处理
})
```

### 无UI
```kotlin
//初始化
QLive.init(
    this, QSdkConfig()
) { callback ->
    //业务方获取token
    getLoginToken(callback)
}

//登陆 登陆成功后才能使用qlive
QLive.auth(object : QLiveCallBack<Void> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: Void?) {
    }
})

//可选 绑定用户资料 第一次绑定后没有跟新个人资料可不用每次都绑定 
//也可以在服务端绑定用户 服务端也可以调用
val ext =  HashMap()
ext.put("vip","1"); // 可选参数，接入用户希望在直播间的在线用户列表/资料卡等UI中显示自定义字段如vip等级等等接入方的业务字段
ext.put("xxx","xxx");//扩展多个字段
//跟新/绑定 业务端的用户信息
QLive.setUser(QUserInfo( "your avatar","your nickname", ext) ,object :  QLiveCallBack<Void>{});

//创建房间
val param =  QCreateRoomParam();
param.setTitle("xxxtitle");
QLive.getRooms().createRoom(param, object :
    QLiveCallBack<QLiveRoomInfo> {
    override fun onError(code: Int, msg: String) {
    }
    override fun onSuccess(data: QLiveRoomInfo) {
    }
})

// 主播推流
//创建推流client
val client = QLive.createPusherClient();
val microphoneParams =  QMicrophoneParam();
microphoneParam.setSampleRate(48000);
//启动麦克风模块
client.enableMicrophone(microphoneParam);
val cameraParam =  QCameraParam()
cameraParam.setFPS(15)
//启动摄像头模块
client.enableCamera(cameraParam,findViewById(R.id.renderView));
//注册房间端监听
client.setLiveStatusListener( object : QLiveStatusListener{})
//加入房间
client.joinRoom(roomId, object :
    QLiveCallBack<QLiveRoomInfo> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: QLiveRoomInfo) {
    }
})
//关闭
client.closeRoom(object : QLiveCallBack<Void> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: Void?) {
    }
})
//销毁
client.destroy();



//用户拉流房间
val client = QLive.createPlayerClient();
//注册房间端监听
client.setLiveStatusListener(object : QLiveStatusListener{})
//加入房间
client.joinRoom(roomId, object :
    QLiveCallBack<QLiveRoomInfo> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: QLiveRoomInfo) {
    }
})
//播放
client.play(findViewById(R.id.renderView));
//离开
client.leaveRoom(object : QLiveCallBack<Void> {
    override fun onError(code: Int, msg: String?) {
    }
    override fun onSuccess(data: Void?) {
    }
})
//销毁
client.destroy();

```

### 使用插件服务

```kotlin
client.getService(QxxxService::class.java) //获取某个业务插件服务
//案列
client.getService(QPKService::class.java)?.start(20 * 1000, receiverRoomID, receiver.userId, null,object : QLiveCallBack<QPKSession> {})
```

### 主动跳转到直播间
```kotlin
   //主动跳转观众直播间
  QLive.getLiveUIKit().getPage(RoomPage::class.java).startPlayerRoomActivity(...)
   //主播主动跳转已经存在主播直播间
  QLive.getLiveUIKit().getPage(RoomPage::class.java).startAnchorRoomActivity(...)
   //自定义开播跳转预览创建直播间页面
  QLive.getLiveUIKit().getPage(RoomPage::class.java).startAnchorRoomWithPreview(...)
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
        object : QLinkMicMixStreamAdapter {
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
    private val mQAudienceMicHandler = object : QAudienceMicHandler.LinkMicHandlerListener {
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

