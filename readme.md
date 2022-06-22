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
implementation project(':app-sdk:depends_sdk_qrtc')  //其他版本下载地址-(https://github.com/pili-engineering/QNDroidIMSDK/tree/main/app/libs)
//七牛播放器  观众拉流端必选 
implementation project(':app-sdk:depends_sdk_piliplayer') //其他版本下载地址-(https://developer.qiniu.com/pili/1210/the-android-client-sdk)

//低代码无ui sdk 必选
implementation project(':app-sdk:qlive-sdk') 

implementation 'com.qiniu:happy-dns:0.2.17' // 七牛dns 必选
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
        //业务请求token
        void getTokenInfo( QLiveCallBack<String> callback){
            GetTokenApi.getToken(callback);
        }
        },new QLiveCallBack<Void>{});

Map ext = new HashMap()
ext.put("vip","1"); //自定义vip等级
ext.put("level","22");//扩展用户等级

//跟新/绑定 业务端的用户信息
QLive.setUser(new QUserInfo( "your avatar","your nickname", ext) ,new QLiveCallBack<Void>{});

QliveUIKit liveUIKit = QLive.getLiveUIKit()
//跳转到直播列表页面
liveUIKit.launch(context);

```

### 自定义UI

### 修改现有的UI组件

方法1 创建自定义UI组件 继承QLiveComponent，调用replace方法无侵入式替换
```kotlin

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
        //设置房间公告文本 公告组件从roomInfo中字段取出
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
```

替换原来内置的UI组件

```kotlin
//获取房间页面
val roomPage =   QLive.getLiveUIKit().getPage(RoomPage::class.java)
//替换公告
roomPage.roomNoticeView.replace(CustomNoticeView::class.java)
//替换底部功能栏
roomPage.bottomFucBar.replace(CustomBottomFucBar::class.java)
//.....每个组件都可以替换

//获取房间列表页面
val roomListPage = QLive.getLiveUIKit().getPage(RoomListPage::class.java)
//替换房间列表页面的创建按钮
roomListPage.createRoomButton.replace(CustomCreateRoomButton::class.java)
//.....每个组件都可以替换

```
删除内置UI组件
```kotlin
val roomPage =   QLive.getLiveUIKit().getPage(RoomPage::class.java)
//删除公告
roomPage.roomNoticeView.isEnable = false
//删除底部功能栏
roomPage.bottomFucBar.isEnable = false
//.....每个组件都可以删除
```

方法2 修改kit组件源码

### 添加UI组件

方法1 无侵入式添加

```kotlin
class CustomView :FrameLayout, QLiveComponent {
   //  实现自己额外的多个UI布局
}

//在房间内置UI上层添加自己的多个额外的UI组件
roomPage.outerCoverView.replace(CustomView::class.java)
//在房间内置UI下层添加自己的多个额外的UI组件
roomPage.innerCoverView.replace(CustomView::class.java)
```

方法2 修改kit开源代码


### 修改布局

方法1 无侵入式修改

拷贝kit布局xml文件 修改属性参数如边距排列方式
```
//自定义房间页面观众房间的布局
roomPage.playerCustomLayoutID = R.layout.customlayout
//自定义房间页面主播房间的布局
roomPage.anchorCustomLayoutID = R.layout.customlayout
//自定义房间列表页面布局
roomListPage.customLayoutID = R.layout.customlayout

```
方法2 直接修改kit开源代码

### 添加功能组件

方法1 自定义组件继承 QLiveComponent 处理自定义关心的事件
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


### 自定义房间列表页面
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
roomListView.attachKitContext(QUIKitContext(this,supportFragmentManager,this,this))
```

方式2
```
//使用 qlive数据api 获取房间等数据自定义UI
QLive.getRooms().listRoom(..）
QLive.getRooms().createRoom(..）
```
方式3
修改UIkit源码

### 无UI
```java
无UI
//初始化
QLive.init(context ,new QTokenGetter(){
   //业务请求token
    void getTokenInfo( QLiveCallBack<String> callback){
        GetTokenApi.getToken(callback);
     }
   
 },new QLiveCallBack<Void>{});

Map extension = new HashMap()
extension.put("vip","1"); //自定义vip等级
extension.put("level","22");//扩展用户等级

//跟新/绑定 业务端的用户信息
QLive.setUser(new QUserInfo( "your avatar","your nickname", extension) ,new QLiveCallBack<Void>{});

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


#接口说明
## 初始化
```java
class QLive {
    static void init(Context context, QTokenGetter tokenGetter, QLiveCallBack<Void> callBack); // 初始化
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



interface QTokenGetter{
    void getTokenInfo( QLiveCallBack<String> callback);
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
    String roomID;
    String title;
    String notice;
    String coverURL;
    Map<String, String> extension; //房间扩展字段
    QLiveUser anchor;
    String roomToken;
    String pkID;
    long onlineCount;
    long startTime;
    long endTime;
    String chatID;
    String pushURL;
    String hlsURL;
    String rtmpURL;
    String flvURL;
    double pv;
    double uv;
    int totalCount;
    int totalMics;
    QLiveStatus liveStatus;
}

class QCreateRoomParam {
    String title;                      //标题
    String notice;                     //公告
    String coverURL;                   //封面 
    HashMap<String,String> extension; //扩展字段
}

```

## 主播/观众 客户端

```java
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

class QPlayerClient {

    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setLiveStatusListener(QLiveStatusListener liveStatusListener);          //房间状态监听
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间
    void leaveRoom( QLiveCallBack<Void> callBack);                               //关闭房间
    void destroy();                                                              //销毁
    void play(QRenderView renderView);                                           //绑定播放器渲染视图

    void setPlayerEventListener(QPlayerEventListener playerEventListener);       //设置拉流端事件回调  
}

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

enum QCameraFace{
    FRONT,BACK
};

interface QConnectionStatusLister{
    void onConnectionStatusChanged(QRoomConnectionState state);  //rtc推流链接状态
}

class QMicrophoneParam {
    int sampleRate = 48000;
    int bitsPerSample = 16;
    int channelCount = 1;
    int bitrate = 64000;
}

class QCameraParam {
    int width = 720;
    int height = 1280;
    int FPS = 25;
    int bitrate = 1000;
}

class QLiveUser {
    String userID;
    String imUID;
    String avatar;
    String nick;
    Map<String,String> extension; //用户扩展字段
}

enum QRoomConnectionState{
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    RECONNECTED;
}

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
    void onLinkerJoin(QMicLinker micLinker);                                        //有人上麦回调
    void onLinkerLeft(QMicLinker micLinker);                                             //有人下麦回调
    void onLinkerMicrophoneStatusChange(QMicLinker micLinker);                           //麦上麦克风变化回调
    void onLinkerCameraStatusChange(QMicLinker micLinker);                               //麦上摄像头变化回调
    void onLinkerKicked(QMicLinker micLinker, String msg);                               //踢人事件回调
    void onLinkerExtensionUpdate(QMicLinker micLinker, QExtension extension);                  //麦上扩展字段变化回调 
}

class QAnchorHostMicHandler {
    void setMixStreamAdapter(QMixStreamAdapter mixStreamAdapter);                       //设置混流适配器
}

interface QMixStreamAdapter {
    List<QMergeOption> onResetMixParam(List<QMicLinker> micLinkers, QMicLinker target, boolean isJoin); //混流适配 将变化后的麦位列表视频成混流参数
}

class QAudienceMicHandler{
    void removeListener(QLinkMicListener listener);
    void addListener(QLinkMicListener listener);
    void startLink(HashMap<String,String> extension,CameraParams cameraParams, MicrophoneParams microphoneParams, QLiveCallBack<Void> callBack  );//上麦
    void stopLink();                                                               //下麦  
    void switchCamera(QLiveCallBack<QCameraMode> callBack);                        //切换摄像头
    void muteCamera(boolean muted,QLiveCallBack<Boolean> callBack);                //禁/不禁用本地视频流
    void muteMicrophone(boolean muted,QLiveCallBack<Boolean> callBack);            //禁/不禁用本地麦克风流

    setVideoFrameListener(QVideoFrameListener frameListener);
    setAudioFrameListener(QAudioFrameListener frameListener);

    interface QLinkMicListener{
        onRoleChange(boolean isLinker);                                             //本地角色变化
    }
}
```


```java
interface QPKService extends QLiveService{
    void removeServiceListener(QPKServiceListener pkServiceListener);
    void addServiceListener(QPKServiceListener pkServiceListener);
    void setMixStreamAdapter(QPKMixStreamAdapter mixAdapter);                       //设置pk混流适配器
    void updatePKExtension(QExtension extension, QLiveCallBack<Void> callBack);      //跟新pk扩展字段  
    void start(long timeoutTimestamp ,String receiverRoomID, String receiverUID, HashMap<String, String> extension, QLiveCallBack<QPKSession> callBack);
    void stop(QLiveCallBack<Void> callBack);                                        //结束pk
    void setPeerAnchorPreView(QRenderView view);                                    //设置对方预览
    QInvitationHandler getInvitationHandler();                                      //获取连麦邀请处理器
}

interface QPKServiceListener{
    void onStart(QPKSession pkSession);                                             //pk开始
    void onStop(QPKSession pkSession,int code,String msg);                          //pk结束
    void onStartTimeOut(QPKSession pkSession);                                      //开始超时 与对方主播建立链接超时      
    void onPKExtensionUpdate(QPKSession pkSession,QExtension extension);             //PK扩展字段跟新
}
//pk混流适配
interface QPKMixStreamAdapter{
    QMixStreamParam onPKMixStreamStart(QPKSession pkSession); //pk 开始返回 混流画布参数
    List<QMergeOption> onPKLinkerJoin(QPKSession pkSession);    //PK 开始如何混流
    List<QMergeOption> onPKLinkerLeft();                       //PK结束如果还有其他普通连麦者如何混流 如果没有则不回调自动恢复单路转推
}

class QPKSession {
    String sessionID;
    QLiveUser initiator;
    QLiveUser receiver;
    String initiatorRoomID;
    String receiverRoomID;
    Map<String, String> extension;
    int status;
    long startTimeStamp;
}
```

```java
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
    QLiveUser initiator;
    QLiveUser receiver;
    String initiatorRoomID;
    String receiverRoomID;
    HashMap<String,String> extension;
    int linkType;

    @JSONField(serialize = false)
    int invitationID;
}
```
```java
class QMergeOption {
    String uID;
    CameraMergeOption  cameraMergeOption;
    MicrophoneMergeOption microphoneMergeOption;

    class CameraMergeOption  {
        boolean isNeed = true;
        int x = 0;
        int y = 0;
        int z = 0;
        int width = 0;
        int height = 0;
    }

    class MicrophoneMergeOption  {
        boolean isNeed = true;
    }
}

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

interface QChatRoomServiceListener{
    void onUserJoin(String memberID);
    void onUserLeft(String memberID);
    void onReceivedC2CMsg(String msg, String fromID, String toID);
    void onReceivedGroupMsg(String msg, String fromID, String toID);
    void onUserKicked(String memberID);
    void onUserBeMuted(boolean isMute, String memberID, long duration);
    void onAdminAdd(String memberID);
    void onAdminRemoved(String memberID, String reason);
}
```

```java
interface QRoomService {
    void removeServiceListener(QRoomServiceListener listener);
    void addServiceListener(QRoomServiceListener listener);
    QLiveRoomInfo getRoomInfo();
    void getRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);                             //刷新房间信息
    void updateExtension(QExtension extension, QLiveCallBack<void> callBack);    //跟新房间扩展字段
    void getOnlineUser(QLiveCallBack<List<QLiveUser>> callBack);                         //当前房间现在用户

}

interface QRoomServiceListener{
    void onRoomExtensionUpdate(QExtension extension);  //房间扩展字段跟
}
```

```java
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

class QPublicChat {
    String action;
    QLiveUser sendUser;
    String content;
    String senderRoomID;
}

```
```java
interface QDanmakuService extends QLiveService {
    void addServiceLister(QDanmakuServiceListener listener);
    void removeServiceLister(QDanmakuServiceListener listener);
    void sendDanmaku(String msg, HashMap<String,String> extension, QLiveCallBack<QDanmaku> callBack);
}
interface QNDanmakuServiceListener {
    void onReceiveDanmaku(QDanmaku danmaku);
}
class QDanmaku {
    QLiveUser sendUser;
    String content;
    String senderRoomID;
    HashMap<String, String> extension;
}

```

## UIKIT

```java
//主播列表
class RoomListPage extends QPage {
    setCustomLayoutId(int layoutID); //替换整体布局
    AppBarView appbar;  //页面toolbar      
    RoomListViewView roomListView; //房间列表
    CreateRoomButtonView createRoomButton; //创建房间按钮 
}

//房间列表页面
class RoomPage {

    setPlayerCustomLayoutId(int layoutID); //替换整体布局 替换观众端
    setAnchorCustomLayoutId(int layoutID); //替换整体布局 替换主播端

    LivePrepareView livePreView ;//开播准备
    RoomBackGroundView roomBackGroundView;//房间背景

    //顶部 
    RoomHostView roomHostView; //左上角房主
    OnlineUserView onlineUserView ;//右上角在线用户槽位
    RoomMemberCountView RoomMemberCountView;//右上角房间
    RoomIDView roomIDView ;//右上角房间
    RoomTimerView roomTimerView;//右上角房间计时器
    DanmakuTrackView danmakuTrackView;//弹幕


    //中部
    PublicChatView publicChatView ;//公屏聊天
    RoomNoticeView roomNoticeView ;//公告

    PKPreView pkPreview;//pk主播两个小窗口
    PKCoverView pkCoverview;//pk覆盖层自定义UI

    LinkersView linkersView;//连麦中的用户 


    //底部

    ShowInputView showInputView;//房间底部 输入框
    StartPKView startPKView;//主播开始pk按钮
    BottomFucBarView bottomFucBar ;//右下角功能栏目 --连麦弹幕关闭按钮等功能栏


    OuterCoverView outerCoverView;// 全局上层覆盖自定义 空槽位
    InnerCoverView innerCoverView ;//全局底层覆盖自定义 空槽位

    ShowPKApplyFunctionComponent showPKApplyComponent ;//主播收到pk邀请 展示弹窗 事件监听功能组件 
    ShowLinkMicApplyFunctionComponent showLinkMicApplyComponent ;//主播收到连麦申请 展示弹窗 事件监听功能组件

    addFunctionComponent(QRoomComponent component); //注册自定义功能组件
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
        val androidContext: Context,
        val fm: FragmentManager,
        val currentActivity: Activity,
        val lifecycleOwner: LifecycleOwner,
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


