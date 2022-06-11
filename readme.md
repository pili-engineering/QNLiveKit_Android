## exmaple:
```java
无UI
//初始化
QLive.init(context ,new QTkenGetter(){
      //业务请求token
     GetTokenApi.getToken(callback);
});

Map ext = new HashMap()
ext.put("vip","1"); //自定义vip等级
ext.put("level","22");//扩展用户等级

//跟新/绑定 业务端的用户信息
QLive.updateUser(new UserInfo( "your avatar","your nickname", ext) ,new QLiveCallBack<Void>{});
 
// 主播推流
//创建推流client
QPusherClient client = QLive.createPusherClient();
//启动麦克风模块
client.enableAudio(MicrophoneParams().apply { mSampleRate = 48000 });
//启动摄像头模块
client.enableVideo(CameraParams().apply {fps=15  });
//本地预览
client.setLocalPreView(findViewById(R.id.QTextureView));
   
//注册房间端监听
client.setClientEventListener(new: QLiveStatusListener{})

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
 
//设置本地预览
client.setPlayer(findViewById(R.id.QPLPlayer));
 
//注册房间端监听
client.setClientEventListener(new: QLiveStatusListener{})

//加入房间
client.joinRoom( roomID, new QLiveCallBack<QLiveRoomInfo> {
    override void onSuccess(QLiveRoomInfo roomInfo){}
    override void onError(int code, String msg) {}
});

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
QLive.init(context ,new QTkenGetter(){
      //业务请求token
     GetTokenApi.getToken(callback);
});

Map ext = new HashMap()
ext.put("vip","1"); //自定义vip等级
ext.put("level","22");//扩展用户等级

//跟新/绑定 业务端的用户信息
QLive.updateUser(new UserInfo( "your avatar","your nickname", ext) ,new QLiveCallBack<Void>{});


QliveUIKit liveUIKit = QLive.createLiveUIKit()
//跳转到直播列表页面
liveUIKit.launch(context);



//配置UI (可选);
RoomPage roomPage = liveUIKit.getRoomPage();
//每个内置UI组件都可以配置自己的替换实现
roomPage.noticeViewComponent.replace(CustomView.Class);
           
//每个内置UI组件都可以禁用
roomPage.noticeViewComponent.isEnable = false;
           
//插入全局覆盖层
roomPage.mOuterCoverComponent.replace(CustomView::clas.java)
   
 
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
    static init(Context context, QTokenGetter tokenGetter, QLiveCallBack<Void> callBack);  // 初始化
    static updateUser(QUserInfo userInfo ,QLiveCallBack<Void> callBack); //绑定用户信息
    static QPusherClient createPusherClient();                                 //创建主播端
    static QPlayerClient createPlayerClient();                                 //创建观众端
    static QLiveUIKit createQLiveUIKit();                               //创建uikit
}

class QLiveUIKit{
    static RoomListPage getRoomListPage();                                  //房间列表页面 -ui组件表
    static RoomPage getRoomPage();                                         //房间页面 - ui组件表
    static void launch(Context context);                                       //启动 跳转直播列表页面
}

interface QTokenGetter{
  void getTokenInfo( QLiveCallBack<String> callback)
}

class QUserInfo{
    String avatar;
    String nick;
    Map<String,String> extensions; //扩展字段
}
```

## 主播/观众 客户端

```java
class QPusherClient {
    void registerService(List<Class<? extends QNLiveService>> serviceClass);     //注册用户需要的服务
    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setLiveStatusListener(QLiveStatusListener liveStatusListener);          //房间事件监听
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);         //加入房间
    void closeRoom( QLiveCallBack<Void> callBack);                                //关闭房间
    void destroy();                                                               //销毁

    void setConnectionStatusLister(QConnectionStatusLister connectionStatusLister);//媒体事件监听
    void enableCamera(QCameraParams cameraParams,QRenderView renderview);          //启动视频采集 和预览
    void enableMicrophone(QMicrophoneParams microphoneParams);                     //启动麦克参数
    void switchCamera(QLiveCallBack<QCameraMode> callBack);                        //切换摄像头
  
    void muteCamera(boolean muted,QLiveCallBack<Boolean> callBack);                //禁/不禁用本地视频流
    void muteMicrophone(boolean muted,QLiveCallBack<Boolean> callBack);             //禁/不禁用本地麦克风流
    void setVideoFrameListener(QVideoFrameListener frameListener);                 //设置本地摄像头数据监听
    void setAudioFrameListener(QAudioFrameListener frameListener);                 //设置本地音频数据监听
}

class QPlayerClient {
    void registerService(List<Class<? extends QNLiveService>> serviceClass);     //注册用户需要的服务
    <T extends QLiveService> T getService(Class<T> serviceClass);                //获得插件服务
    void setLiveStatusListener(QLiveStatusListener liveStatusListener);              //房间事件监听
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);                //加入房间
    void leaveRoom( QLiveCallBack<Void> callBack);                                //关闭房间
    void destroy();                                                               //销毁 
     
    void player(QPlayer player);                                          //绑定播放器   
}


interface QLiveStatusListener {
    void onLiveStatusChanged(QLiveStatus liveStatus);                             //直播间状态变化 业务状态
}

//直播状态枚举  低代码服务端定义的业务状态
enum QLiveStatus {
    LiveStatusPrepare, //直播准中 （已经创建）
    LiveStatusOn,      // 直播间已发布  （已经发布，可以开播和拉流
    LiveStatusAnchorOnline, //主播上线
    LiveStatusAnchorOffline, //主播离线
    LiveStatusOff            //直播间关闭
};

enum QCameraMode{
  Front,Back
};

interface QConnectionStatusLister{
    void onConnectionStateChanged(QRoomConnectionState state);//rtc推流链接状态
}

class QMicrophoneParams {
    int sampleRate = 48000;
    int bitsPerSample = 16;
    int channelCount = 1;
    int bitrate = 64000;
}
 
class QCameraParams {
    int width = 720;
    int height = 1280;
    int fps = 25;
    int bitrate = 1000;
}

class QLiveRoomInfo {
    String roomID;
    String title;
    String notice;
    String coverUrl;
    Map<String, String> extension; //房间扩展字段
    QLiveUser anchorInfo;
    String roomToken;
    String pkId;
    long onlineCount;
    long startTime;
    long endTime;
    String chatId;
    String pushUrl;
    String hlsUrl;
    String rtmpUrl;
    String flvUrl;
    double pv;
    double uv;
    int totalCount;
    int totalMics;
    int liveStatus;
    int anchorStatus;
}

class QLiveUser {
    String userID;
    String imUID;
    String avatar;
    String nick;
    Map<String,String> extensions; //用户扩展字段
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
    void updateExtension(QMicLinker micLinker, Extension extension,QLiveCallBack<Void> callBack); //跟新麦位扩展字段
    QAudienceMicLinker getAudienceMicLinker();                                        // 用户连麦器 
    QAnchorHostMicLinker getAnchorHostMicLinker();                                    //主播连麦器
    QInvitationHandler getInvitationHandler();                                        //邀请处理器   
}
 
interface QLinkMicServiceListener{
    void onUserJoinLink(QMicLinker micLinker);                                        //有人上麦回调
    void onUserLeft(MicLinker micLinker);                                             //有人下麦回调
    void onUserMicrophoneStatusChange(MicLinker micLinker);                           //麦上麦克风变化回调
    void onUserCameraStatusChange(MicLinker micLinker);                               //麦上摄像头变化回调
    void onUserBeKick(MicLinker micLinker, String msg);                               //踢人事件回调
    void onUserExtension(MicLinker micLinker, Extension extension);                   //麦上扩展字段变化回调 
}

class QNAnchorHostMicLinker {
    void setMixStreamAdapter(MixStreamAdapter mixStreamAdapter);                       //设置混流适配器
}

interface MixStreamAdapter {
     List<QNMergeOption> onResetMixParam(List<QNMicLinker> micLinkers, QNMicLinker target, boolean isJoin); //混流适配 将变化后的麦位列表视频成混流参数
}

class QAudienceMicLinker{
     void removeListener(QLinkMicListener listener);               
     void addListener(QLinkMicListener listener);
     void startLink(HashMap<String,String> extensions,CameraParams cameraParams, MicrophoneParams microphoneParams, QLiveCallBack<Void> callBack  );//上麦
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
    void updatePKExtension(Extension extension, QLiveCallBack<Void> callBack);      //跟新pk扩展字段  
    void start(long timeoutTimestamp ,String receiverRoomID, String receiverUID, HashMap<String, String> extensions, QLiveCallBack<QPKSession> callBack);
    void stop(QLiveCallBack<Void> callBack);                                        //结束pk
    void setPeerAnchorPreView(QRenderView view);                                    //设置对方预览
    QInvitationHandler getInvitationHandler();                                      //获取连麦邀请处理器
}

interface QPKServiceListener{
    void onStart(QPKSession pkSession);                                             //pk开始
    void onStop(QPKSession pkSession,int code,String msg);                          //pk结束
    void onStartTimeOut(QPKSession pkSession);                                      //开始超时 与对方主播建立链接超时      
    void onPKExtensionUpdate(QPKSession pkSession,Extension extension);             //PK扩展字段跟新
}
//pk混流适配
interface QPKMixStreamAdapter{
     MixStreamParams onPKMixStreamStart(QPKSession pkSession); //pk 开始返回 混流画布参数
     List<QMergeOption> onPKLinkerJoin(PKSession pkSession);    //PK 开始如何混流
     List<QMergeOption> onPKLinkerLeft();                       //PK结束如果还有其他普通连麦者如何混流 如果没有则不回调自动恢复单路转推
}

```

```java
class QInvitationHandler{
    void apply(long expiration, String receiverRoomID,String receiverUID, HashMap<String,String>extensions, QLiveCallBack<QInvitation> callBack);//发起邀请或者申请
    void cancelApply(int invitationId,QLiveCallBack<Void> callBack);                                  //取消申请
    void accept(int invitationId, HashMap<String,String> extensions,QLiveCallBack<Void> callBack);    //接受
    void reject(int invitationId, HashMap<String,String> extensions,  QLiveCallBack<Void> callBack ); //拒绝
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
     HashMap<String,String> extensions;
     int linkType;
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
       boolean isNeed ;
    }
}
```

```java
interface QChatRoomService extends QLiveService {
     void removeServiceListener(QChatRoomServiceListener chatServiceListener);
     void addServiceListener(QChatRoomServiceListener chatServiceListener);
     void sendCustomC2CMsg(String msg, String memberId, QLiveCallBack<Void> callBack);  //发c2c
     void sendCustomGroupMsg(String msg, QLiveCallBack<Void> callBack);                 //发群消息
     void kickUser(String msg, String memberId, QLiveCallBack<Void> callBack);          //踢出聊天室
     void muteUser(boolean isMute ,String msg, String memberId, long duration ,QLiveCallBack<Void> callBack); //禁言
     void addAdmin( String memberId, QLiveCallBack<Void> callBack);                      //添加管理员
     void removeAdmin(String msg, String memberId, QLiveCallBack<Void> callBack);        //移除管理员
}

interface QChatRoomServiceListener{
     void onUserJoin(String memberId);
     void onUserLeft(String memberId);
     void onReceivedC2CMsg(String msg, String fromId, String toId);
     void onReceivedGroupMsg(String msg, String fromId, String toId);
     void onUserBeKicked(String memberId);
     void onUserBeMuted(boolean isMute, String memberId, long duration);
     void onAdminAdd(String memberId);
     void onAdminRemoved(String memberId, String reason);
}
```

```java
interface QRoomService {
    void removeServiceListener(QRoomServiceListener listener);
    void addServiceListener(QRoomServiceListener listener);
    QLiveRoomInfo getRoomInfo();
    void refreshRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);                    //刷新房间信息
    void updateRoomExtension(Extension extension, QLiveCallBack<void> callBack);    //跟新房间扩展字段
    void getOnlineUser(QLiveCallBack<List<LiveUser>> callBack);                     //当前房间现在用户
    static void searchUserByUserId(String uID, QLiveCallBack<LiveUser> callBack);   //根据ID搜索用户信息
    static void searchUserByIMUID(String imUID, QLiveCallBack<LiveUser> callBack);
    static void createRoom(QCreateRoomParam param, QLiveCallBack<QLiveRoomInfo> callBack);   //创建房间
    static void deleteRoom(String roomID, QLiveCallBack<void> callBack);                    //删除房间
    static void listRoom(QLiveRoomStatus status, int pageNumber, int pageSize, QLiveCallBack<List<QLiveRoomInfo>> callBack);
    static void getRoomInfo(String roomID, QLiveCallBack<List<QLiveRoomInfo>> callBack);
}

interface QRoomServiceListener{
     void onRoomExtensions(Extension extension);  //房间扩展字段跟
}
```

```java
interface QPublicChatService extends QLiveService{
     void addServiceLister(QPublicChatServiceLister lister);
     void removeServiceLister(QPublicChatServiceLister lister);
     void sendPublicChat(String msg, QLiveCallBack<PubChatModel> callBack);         //发送公屏聊天消息
     void sendWelCome(String msg, QLiveCallBack<PubChatModel> callBack);            //发送进入房间消息
     void sendByeBye(String msg, QLiveCallBack<PubChatModel> callBack);            //发送离开房间消息
     void sendLike(String msg, QLiveCallBack<PubChatModel> callBack);               //发送点赞消息
     void sendCustomPubChat(String action, String msg,  QLiveCallBack<PubChatModel> callBack); //发送自定义要显示在公屏上的消息
     void pubMsgToLocal(PubChatModel chatModel);                                    //往本地公屏插入消息不发送到对端
}

interface QPublicChatServiceLister {
     void onReceivePublicChat(PubChatModel model); //收到公屏消息
}

class PubChatModel {
     String action;
     QLiveUser sendUser;
     String content;
     String senderRoomID;
     String getMsgAction();
}

```

## UIKIT

```java
//主播列表
class RoomListPage  {
     setCustomLayoutId(int layoutID); //替换整体布局
     AppBarComponent appbar;  //页面toolbar      
     RoomListViewComponent roomListView; //房间列表
     CreateRoomButtonComponent createRoomButton; //创建房间按钮 
}

//房间列表页面
class RoomPage {
    setCustomLayoutId(int layoutID); //替换整体布局
    RoomBackGroundComponent roomBackGroundView;//房间背景
    RoomHostComponent roomHostView; //左上角房主
    LivePrepareComponent livePreView ;//开播准备
    OnlineUserComponent onlineUserView ;//右上角在线用户槽位
    RoomMemberCountComponent RoomMemberCountView;//右上角房间
    RoomIDComponent roomIDView ;//右上角房间
    RoomTimerComponent roomTimerView;//右上角房间计时器
    DanmakuTrackComponent danmakuTrackView;//弹幕
    PublicChatComponent publicChatView ;//公屏聊天
    RoomNoticeComponent roomNoticeView ;//公告
    StartPKComponent startPKView;//主播开始pk按钮
    PKCoverComponent pkCoverView;  //PK覆盖层 空槽位
    PKAnchorPreviewComponent pkAnchorPreview;//pk主播两个小窗口
    LinkersComponent linkersView;//连麦中的用户 
    ShowInputComponent showInputView;//房间底部 输入框
    BottomFucBarComponent bottomFucBar ;//右下角功能栏目 --连麦弹幕关闭按钮等功能栏
    OuterCoverComponent outerCoverView;// 全局上层覆盖自定义 空槽位
    InnerCoverComponent mInnerCoverView ;//全局底层覆盖自定义 空槽位
    
    ShowPKApplyFucComponent showReceivedPKApply ;//主播收到连麦申请弹窗
    ShowLinkMicApplyFucComponent showLinkMicApply ;//主播收到pk邀请弹窗
}
```


```Java
//kit基础类说明 非dom
//房间内的UI组件
interface QRoomUIComponent {
    void attach( KitContext context,QLiveClient  client );
    void onRoomEntering(String roomID,QLiveUser user);                         //正在加入房间
    void onRoomJoined(QRoomInfo roomInfo);                                     //加入了某个房间  
    void onRoomLeft();                                                          //离开了某个房间 
    void onRoomClosed();                                                        //关闭了直播 
    void onDestroyed();                                                         //client销毁
}
//UI组件
interface QUIComponent {
    void attach( KitContext context);
}
//UI组件的上下文
interface KitContext {
     Context getAndroidContext();                                               //安卓上下文
     FragmentManager getAndroidFragmentManager();                               //安卓fm
     FragmentActivity getCurrentActivity();                                     //所在的Activity
}
//用户自定义房间内组件基类
class BaseRoomUIComponentView extends FrameLayout implements LifecycleEventObserver, QRoomUIComponent{
   
}
///用户自定义组件基类
class BaseUIComponentView extends FrameLayout implements LifecycleEventObserver, QUIComponent{

}

//内置UI型号组件
class BaseRoomUIComponent{
    <T extends BaseRoomUIComponentView> void setReplaceView(Class<T> serviceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);                                    
}
//内置UI型号组件
class BaseUIComponent{
    <T extends BaseUIComponentView> void setReplaceView(Class<T> serviceClass); //替换成你的UI
    void setIsEnable(boolean isEnable);
}

//房间内功能型组件 处理事件
class BaseRoomFuncComponentHandler implements LifecycleEventObserver, QComponent{
}

//内置功能型号组件
class BaseFucComponent{
    <T extends BaseRoomFuncComponentHandler> void setReplaceHandler(Class<T> serviceClass); //替换成你的处理器
    void setIsEnable(boolean isEnable);
}
```










