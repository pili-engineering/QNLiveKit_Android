## exmaple:
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

//关闭
client.destroy(); 
```



```java
//UIKIT
//初始化
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


//配置UI (可选);
RoomPage roomPage = liveUIKit.getRoomPage();

//每个内置UI组件都可以配置自己的替换实现
roomPage.noticeViewComponent.replace(CustomView.Class);
           
//每个内置UI组件都可以禁用
roomPage.noticeViewComponent.isEnable = false;
           
//插入全局覆盖层
roomPage.mOuterCoverComponent.replace(CustomView.Class)
        
//可选 配置直播列表样式
RoomListPage roomListPage =  liveUIKit.getRoomListPage();

//如果需要将直播列表
View view = roomListPage.roomListView.create(context);
//添加到自己想要的地方
addView(view);

```



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
    static RoomListPage getRoomListPage();      //房间列表页面 -ui组件表
    static RoomPage getRoomPage();              //房间页面 - ui组件表
    static void launch(Context context);        //启动 跳转直播列表页面
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
    String chatId;
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
       QRenderMode stretchMode; //混流缩放模式
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
class RoomListPage  {
    setCustomLayoutId(int layoutID); //替换整体布局
    AppBarView appbar;  //页面toolbar      
    RoomListViewView roomListView; //房间列表
    CreateRoomButtonView createRoomButton; //创建房间按钮 
}

//房间列表页面
class RoomPage {

    setCustomLayoutId(int layoutID); //替换整体布局
    
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
    void attachKitContext(QLiveKitUIContext context);
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
class QLiveKitUIContext(
        val androidContext: Context, //安卓上下文
        val fm: FragmentManager,     //显示弹窗上下文
        val currentActivity: Activity, //当前activity
        val lifecycleOwner: LifecycleOwner, //页面生命周期
        val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit, //离开房间操作
        val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit,//创建并加入操作
        val getPlayerRenderViewCall: () -> QPlayerRenderView?, //获取当前播放器预览窗口
        val getPusherRenderViewCall: () -> QPushRenderView?,  //获取推流预览窗口
        )


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
```


```
client 基类
QLiveClient{
     <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
     void setLiveStatusListener(QLiveStatusListener liveStatusListener);          //房间状态监听
    
    //获得当前client实际类型
     ClientType getClientType()
     
     protected void join( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);           //加入房间
     protected void left( QLiveCallBack<Void> callBack);                                   //离开房间- 主播close 观众leave 对应父类生命周期统一叫left
     protected void destroy();                                                              //销毁
   
}
   
enum QClientType{
   PUSHER,PLAYER
}

```
