```

//低代码直播客户端
QLive{

	//初始化
	//@param-context:安卓上下文	@param-config:sdk配置	@param-tokenGetter:token获取	
	publicstaticvoidinit(Contextcontext,QSdkConfigconfig,QTokenGettertokenGetter);初始化

	//登陆认证成功后才能使用qlive的功能
	//@param-callBack:操作回调	
	publicstaticvoidauth(@NotNull()QLiveCallBack<Void>callBack);登陆认证成功后才能使用qlive的功能

	//跟新用户信息
	//@param-userInfo:用户参数	@param-callBack:回调函数	
	publicstaticvoidsetUser(@NotNull()QUserInfouserInfo,@NotNull()QLiveCallBack<Void>callBack);跟新用户信息

	//获取当前登陆用户资料
	publicstaticQLiveUsergetLoginUser();获取当前登陆用户资料

	//创建推流客户端
	publicstaticQPusherClientcreatePusherClient();创建推流客户端

	//创建拉流客户端
	publicstaticQPlayerClientcreatePlayerClient();创建拉流客户端

	//获取房间管理接口
	publicstaticQRoomsgetRooms();获取房间管理接口

	//获得UIkit
	publicstaticQLiveUIKitgetLiveUIKit();获得UIkit
}

//UIkit客户端
QLiveUIKit{

	//获取内置页面每个页面有相应的UI配置
	//@param-pageClass:页面的类 目前子类为 RoomListPage-> 房间列表页面 RoomPage->直播间页面	
	<TextendsQPage>TgetPage(Class<T>pageClass);获取内置页面每个页面有相应的UI配置

	//跳转到直播列表页面
	//@param-context:安卓上下文	
	voidlaunch(Contextcontext);跳转到直播列表页面
}

//跟新用户资料参数
QUserInfo{
	public String avatar;//[头像]
	public String nick;//[名称]
	public HashMap extension;//[扩展字段]
}

//token获取回调
 当token过期后自动调用getTokenInfo
QTokenGetter{

	//如何获取token
	//@param-callback:业务（同步/异步）获取后把结果通知给sdk	
	voidgetTokenInfo(QLiveCallBack<String>callback);如何获取token
}

//sdk 配置
QSdkConfig{
	public boolean isLogAble;//[打印日志开关]
	public String serverURL;//[服务器地址 默认为低代码demo地址
 如果自己部署可改为自己的服务地址]
}

//房间管理接口
QRooms{

	//创建房间
	//@param-param:创建房间参数	@param-callBack:	
	voidcreateRoom(QCreateRoomParamparam,QLiveCallBack<QLiveRoomInfo>callBack);创建房间

	//删除房间
	//@param-roomID:房间ID	@param-callBack:	
	voiddeleteRoom(StringroomID,QLiveCallBack<Void>callBack);删除房间

	//房间列表
	//@param-pageNumber:	@param-pageSize:	@param-callBack:	
	voidlistRoom(intpageNumber,intpageSize,QLiveCallBack<List<QLiveRoomInfo>>callBack);房间列表

	//根据ID获取房间信息
	//@param-roomID:房间ID	@param-callBack:	
	voidgetRoomInfo(StringroomID,QLiveCallBack<QLiveRoomInfo>callBack);根据ID获取房间信息
}

//直播状态枚举
QLiveStatus{
	public static final QLiveStatus PREPARE;//[房间已创建]
	public static final QLiveStatus ON;//[房间已发布]
	public static final QLiveStatus ANCHOR_ONLINE;//[主播上线]
	public static final QLiveStatus ANCHOR_OFFLINE;//[主播已离线]
	public static final QLiveStatus OFF;//[房间已关闭]

	//
	publicstaticcom.qlive.core.QLiveStatus[]values();

	//
	publicstaticcom.qlive.core.QLiveStatusvalueOf(java.lang.Stringname);
}

//基础回调函数
QLiveCallBack{

	//操作失败
	//@param-code:错误码	@param-msg:消息	
	voidonError(intcode,Stringmsg);操作失败

	//操作成功
	//@param-data:数据	
	voidonSuccess(Tdata);操作成功
}

//客户端类型枚举
QClientType{
	public static final QClientType PUSHER;//[Pusher 推流端]
	public static final QClientType PLAYER;//[Player 拉流观众端]

	//
	publicstaticcom.qlive.core.QClientType[]values();

	//
	publicstaticcom.qlive.core.QClientTypevalueOf(java.lang.Stringname);
}

//ui组件实现的房间生命周期
QClientLifeCycleListener{

	//进入回调
	//@param-user:进入房间的用户	@param-liveId:房间ID	
	voidonEntering(@NotNull()StringliveId,@NotNull()QLiveUseruser);进入回调

	//加入回调
	//@param-roomInfo:房间信息	
	voidonJoined(@NotNull()QLiveRoomInforoomInfo);加入回调

	//用户离开回调
	voidonLeft();用户离开回调

	//销毁
	voidonDestroyed();销毁
}

//邀请处理器
QInvitationHandler{

	//发起邀请/申请
	//@param-expiration:过期时间 单位毫秒 过期后不再响应	@param-receiverRoomID:接收方所在房间ID	@param-receiverUID:接收方用户ID	@param-extension:扩展字段	@param-callBack:回调函数	
	voidapply(longexpiration,StringreceiverRoomID,StringreceiverUID,HashMap<String,String>extension,QLiveCallBack<QInvitation>callBack);发起邀请/申请

	//取消邀请/申请
	//@param-invitationID:邀请ID	@param-callBack:	
	voidcancelApply(intinvitationID,QLiveCallBack<Void>callBack);取消邀请/申请

	//接受对方的邀请/申请
	//@param-invitationID:邀请ID	@param-extension:扩展字段	@param-callBack:	
	voidaccept(intinvitationID,HashMap<String,String>extension,QLiveCallBack<Void>callBack);接受对方的邀请/申请

	//拒绝对方
	//@param-invitationID:邀请ID	@param-extension:扩展字段	@param-callBack:	
	voidreject(intinvitationID,HashMap<String,String>extension,QLiveCallBack<Void>callBack);拒绝对方

	//移除监听
	//@param-listener:	
	voidremoveInvitationHandlerListener(QInvitationHandlerListenerlistener);移除监听

	//添加监听
	//@param-listener:	
	voidaddInvitationHandlerListener(QInvitationHandlerListenerlistener);添加监听
}

//邀请监听
QInvitationHandlerListener{

	//收到申请/邀请
	//@param-invitation:	
	voidonReceivedApply(QInvitationinvitation);收到申请/邀请

	//对方取消申请
	//@param-invitation:	
	voidonApplyCanceled(QInvitationinvitation);对方取消申请

	//申请/邀请超时
	//@param-invitation:	
	voidonApplyTimeOut(QInvitationinvitation);申请/邀请超时

	//被接受
	//@param-invitation:	
	voidonAccept(QInvitationinvitation);被接受

	//被拒绝
	//@param-invitation:	
	voidonReject(QInvitationinvitation);被拒绝
}

//创建房间参数
QCreateRoomParam{
	public String title;//[房间标题]
	public String notice;//[房间公告]
	public String coverURL;//[封面]
	public HashMap extension;//[扩展字段]
}

//弹幕实体
QDanmaku{
	public static String action_danmu;//[]
	public QLiveUser sendUser;//[发送方用户]
	public String content;//[弹幕内容]
	public String senderRoomID;//[发送方所在房间ID]
	public HashMap extension;//[扩展字段]
}

//扩展字段
QExtension{
	public String key;//[]
	public String value;//[]
}

//邀请信息
QInvitation{
	public QLiveUser initiator;//[发起方]
	public QLiveUser receiver;//[接收方]
	public String initiatorRoomID;//[发起方所在房间ID]
	public String receiverRoomID;//[接收方所在房间ID]
	public HashMap extension;//[扩展字段]
	public int invitationID;//[邀请ID]
}

//房间信息
QLiveRoomInfo{
	public String liveID;//[房间ID]
	public String title;//[房间标题]
	public String notice;//[房间公告]
	public String coverURL;//[封面]
	public Map extension;//[扩展字段]
	public QLiveUser anchor;//[主播信息]
	public String roomToken;//[]
	public String pkID;//[当前房间的pk会话信息]
	public long onlineCount;//[在线人数]
	public long startTime;//[开始时间]
	public long endTime;//[结束时间]
	public String chatID;//[聊天室ID]
	public String pushURL;//[推流地址]
	public String hlsURL;//[拉流地址]
	public String rtmpURL;//[拉流地址]
	public String flvURL;//[拉流地址]
	public Double pv;//[pv]
	public Double uv;//[uv]
	public int totalCount;//[总人数]
	public int totalMics;//[连麦者数量]
	public int liveStatus;//[直播间状态]
	public int anchorStatus;//[主播在线状态]
}

//用户
QLiveUser{
	public String userId;//[用户ID]
	public String avatar;//[用户头像]
	public String nick;//[名字]
	public Map extensions;//[扩展字段]
	public String imUid;//[用户im id]
	public String im_username;//[用户Im名称]
}

//连麦用户
QMicLinker{
	public QLiveUser user;//[麦上用户资料]
	public String userRoomID;//[]
	public HashMap extension;//[扩展字段]
	public boolean isOpenMicrophone;//[是否开麦克风]
	public boolean isOpenCamera;//[是否开摄像头]
}

//pk 会话
QPKSession{
	public String sessionID;//[PK场次ID]
	public QLiveUser initiator;//[发起方]
	public QLiveUser receiver;//[接受方]
	public String initiatorRoomID;//[发起方所在房间]
	public String receiverRoomID;//[接受方所在房间]
	public Map extension;//[扩展字段]
	public int status;//[pk 状态 0邀请过程  1pk中 2结束 其他自定义状态比如惩罚时间]
	public long startTimeStamp;//[pk开始时间戳]
}

//公屏类型消息
QPublicChat{
	public static String action_welcome;//[类型 -- 加入房间欢迎]
	public static String action_bye;//[类型 -- 离开房间]
	public static String action_like;//[类型 -- 点赞]
	public static String action_puchat;//[类型 -- 公屏输入]
	public static String action_pubchat_custom;//[]
	public String action;//[消息类型]
	public QLiveUser sendUser;//[发送方]
	public String content;//[消息体]
	public String senderRoomId;//[发送方所在房间ID]
}

//拉流事件回调
QPlayerEventListener{

	//拉流器准备中
	//@param-preparedTime:准备耗时	
	voidonPrepared(intpreparedTime);拉流器准备中

	//拉流器信息回调
	//@param-what:事件 参考七牛霹雳播放器	@param-extra:数据	
	voidonInfo(intwhat,intextra);拉流器信息回调

	//拉流缓冲跟新
	//@param-percent:缓冲比分比	
	voidonBufferingUpdate(intpercent);拉流缓冲跟新

	///视频尺寸变化回调
	//@param-width:变化后的宽	@param-height:变化后高	
	voidonVideoSizeChanged(intwidth,intheight);/视频尺寸变化回调

	//播放出错回调
	//@param-errorCode:错误码 参考七牛霹雳播放器	
	booleanonError(interrorCode);播放出错回调
}

//默认美颜参数（免费）
QBeautySetting{

	//
	publicbooleanisEnabled();

	//设置是否可用
	publicvoidsetEnable(booleanenable);设置是否可用

	//
	publicfloatgetSmoothLevel();

	//磨皮等级
	//@param-smoothLevel:0.0 -1.0	
	publicvoidsetSmoothLevel(floatsmoothLevel);磨皮等级

	//
	publicfloatgetWhiten();

	//设置美白等级
	//@param-whiten:0.0 -1.0	
	publicvoidsetWhiten(floatwhiten);设置美白等级

	//
	publicfloatgetRedden();

	//设置红润等级0.0-1.0
	//@param-redden:	
	publicvoidsetRedden(floatredden);设置红润等级0.0-1.0
}

//rtc推流链接状态监听
QConnectionStatusLister{

	//rtc推流链接状态
	//@param-state:状态枚举	
	voidonConnectionStatusChanged(QRoomConnectionStatestate);rtc推流链接状态
}

//观众播放器预览
 子类 QPlayerTextureRenderView 和 QSurfaceRenderView
QPlayerRenderView{

	//设置预览模式
	//@param-previewMode:预览模式枚举	
	voidsetDisplayAspectRatio(PreviewModepreviewMode);设置预览模式

	//
	voidsetRenderCallback(QRenderCallbackrendCallback);

	//
	ViewgetView();

	//
	SurfacegetSurface();
}

//麦克风参数
QMicrophoneParam{
	public int sampleRate;//[采样率 默认值48000]
	public int bitsPerSample;//[采样位深 默认16]
	public int channelCount;//[通道数 默认 1]
	public int bitrate;//[码率 默认64000]
}

//摄像头参数
QCameraParam{
	public int width;//[分辨率宽 默认值 720]
	public int height;//[分辨高  默认值 1280]
	public int FPS;//[帧率 默认值25]
	public int bitrate;//[码率 默认值1500]
}

//
QMixStreaming{
}

//混流画布参数
QMixStreaming.MixStreamParams{
	public int mixStreamWidth;//[混流画布宽]
	public int mixStringHeight;//[混流画布高]
	public int mixBitrate;//[混流码率]
	public int FPS;//[混流帧率]
	public TranscodingLiveStreamingImage backGroundImg;//[混流背景图片]
}

//背景图片
QMixStreaming.TranscodingLiveStreamingImage{
	public String url;//[背景图网络url]
	public int x;//[x坐标]
	public int y;//[y坐标]
	public int width;//[背景图宽]
	public int height;//[背景图高]
}

//
QMixStreaming.TrackMergeOption{
}

//摄像头混流参数
QMixStreaming.CameraMergeOption{
	public boolean isNeed;//[是否参与混流]
	public int x;//[x坐标]
	public int y;//[y坐标]
	public int z;//[z坐标]
	public int width;//[用户视频宽]
	public int height;//[用户视频高]
}

//某个用户的混流参数
 只需要指定用户ID 和他的摄像头麦克风混流参数
QMixStreaming.MergeOption{
	public String uid;//[用户混流参数的ID]
	public CameraMergeOption cameraMergeOption;//[视频混流参数]
	public MicrophoneMergeOption microphoneMergeOption;//[音频混流参数]
}

//麦克风混流参数
QMixStreaming.MicrophoneMergeOption{
	public boolean isNeed;//[是否参与混流]
}

//推流客户端（主播端）
QPusherClient{

	//获取插件服务实例
	//@param-serviceClass:插件的类	
	@Override()<TextendsQLiveService>TgetService(Class<T>serviceClass);获取插件服务实例

	//设置直播状态回调
	//@param-liveStatusListener:直播事件监听	
	@Override()voidsetLiveStatusListener(QLiveStatusListenerliveStatusListener);设置直播状态回调

	//当前客户端类型QClientType.PUSHER代表推流端QClientType.PLAYER代表拉流端
	@Override()QClientTypegetClientType();当前客户端类型QClientType.PUSHER代表推流端QClientType.PLAYER代表拉流端

	//启动视频采集和预览
	//@param-cameraParam:摄像头参数	@param-renderView:预览窗口	
	voidenableCamera(QCameraParamcameraParam,QPushRenderViewrenderView);启动视频采集和预览

	//启动麦克采集
	//@param-microphoneParam:麦克风参数	
	voidenableMicrophone(QMicrophoneParammicrophoneParam);启动麦克采集

	//加入房间
	//@param-roomID:房间ID	@param-callBack:回调函数	
	voidjoinRoom(StringroomID,QLiveCallBack<QLiveRoomInfo>callBack);加入房间

	//主播关闭房间
	//@param-callBack:	
	voidcloseRoom(QLiveCallBack<Void>callBack);主播关闭房间

	//销毁推流客户端销毁后不能使用
	voiddestroy();销毁推流客户端销毁后不能使用

	//主播设置推流链接状态监听
	//@param-connectionStatusLister:	
	voidsetConnectionStatusLister(QConnectionStatusListerconnectionStatusLister);主播设置推流链接状态监听

	//Switchcamera
	//@param-callBack:切换摄像头回调	
	voidswitchCamera(QLiveCallBack<QCameraFace>callBack);Switchcamera

	//禁/不禁用本地视频流禁用后本地能看到预览观众不能看到主播的画面
	//@param-muted:是否禁用	@param-callBack:	
	voidmuteCamera(booleanmuted,QLiveCallBack<Boolean>callBack);禁/不禁用本地视频流禁用后本地能看到预览观众不能看到主播的画面

	//禁用麦克风推流
	//@param-muted:是否禁用	@param-callBack:	
	voidmuteMicrophone(booleanmuted,QLiveCallBack<Boolean>callBack);禁用麦克风推流

	//设置视频帧回调
	//@param-frameListener:视频帧监听	
	voidsetVideoFrameListener(QVideoFrameListenerframeListener);设置视频帧回调

	//设置本地音频数据监听
	//@param-frameListener:音频帧回调	
	voidsetAudioFrameListener(QAudioFrameListenerframeListener);设置本地音频数据监听

	//暂停
	voidpause();暂停

	//恢复
	voidresume();恢复

	//设置默认免费版美颜参数
	//@param-beautySetting:美颜参数	
	voidsetDefaultBeauty(QBeautySettingbeautySetting);设置默认免费版美颜参数
}

//推流预览窗口
 子类实现 QPushSurfaceView 和 QPushTextureView
QPushRenderView{

	//
	ViewgetView();

	//renview
	QNRenderViewgetQNRender();renview
}

//拉流客户端
QPlayerClient{

	//获取插件服务实例
	//@param-serviceClass:插件的类	
	@Override()<TextendsQLiveService>TgetService(Class<T>serviceClass);获取插件服务实例

	//设置直播状态回调
	//@param-liveStatusListener:直播事件监听	
	@Override()voidsetLiveStatusListener(QLiveStatusListenerliveStatusListener);设置直播状态回调

	//当前客户端类型QClientType.PUSHER代表推流端QClientType.PLAYER代表拉流端
	@Override()QClientTypegetClientType();当前客户端类型QClientType.PUSHER代表推流端QClientType.PLAYER代表拉流端

	//加入房间
	//@param-roomID:房间ID	@param-callBack:回调	
	voidjoinRoom(StringroomID,QLiveCallBack<QLiveRoomInfo>callBack);加入房间

	//离开房间离开后可继续加入其他房间如上下滑动切换房间
	//@param-callBack:回调	
	voidleaveRoom(QLiveCallBack<Void>callBack);离开房间离开后可继续加入其他房间如上下滑动切换房间

	//销毁释放资源离开房间后退出页面不再使用需要释放
	@Override()voiddestroy();销毁释放资源离开房间后退出页面不再使用需要释放

	//设置预览窗口内置QPlayerTextureRenderView(推荐)/QSurfaceRenderView
	//@param-renderView:预览窗口	
	voidplay(@NotNull()QPlayerRenderViewrenderView);设置预览窗口内置QPlayerTextureRenderView(推荐)/QSurfaceRenderView

	//暂停
	voidpause();暂停

	//恢复
	voidresume();恢复

	//添加播放器事件监听
	//@param-playerEventListener:播放器事件监听	
	voidaddPlayerEventListener(QPlayerEventListenerplayerEventListener);添加播放器事件监听

	//移除播放器事件监听
	//@param-playerEventListener:播放器事件监听	
	voidremovePlayerEventListener(QPlayerEventListenerplayerEventListener);移除播放器事件监听
}

//直播状态监听
QLiveStatusListener{

	//直播间状态变化业务状态
	//@param-liveStatus:业务状态	
	voidonLiveStatusChanged(QLiveStatusliveStatus);直播间状态变化业务状态
}

//连麦服务
QLinkMicService{

	//获取当前房间所有连麦用户
	List<QMicLinker>getAllLinker();获取当前房间所有连麦用户

	//设置某人的连麦视频预览麦上用户调用上麦后才会使用切换成rtc连麦下麦后使用拉流预览
	//@param-uID:用户ID	@param-preview:预览窗口	
	voidsetUserPreview(StringuID,QPushRenderViewpreview);设置某人的连麦视频预览麦上用户调用上麦后才会使用切换成rtc连麦下麦后使用拉流预览

	//踢人
	//@param-uID:用户ID	@param-msg:附加消息	@param-callBack:操作回调	
	voidkickOutUser(StringuID,Stringmsg,QLiveCallBack<Void>callBack);踢人

	//跟新扩展字段
	//@param-micLinker:麦位置	@param-QExtension:扩展字段	
	voidupdateExtension(@NotNull()QMicLinkermicLinker,QExtensionQExtension,QLiveCallBack<Void>callBack);跟新扩展字段

	//添加麦位监听
	//@param-listener:麦位监听	
	voidaddMicLinkerListener(QLinkMicServiceListenerlistener);添加麦位监听

	//移除麦位监听
	//@param-listener:麦位监听	
	voidremoveMicLinkerListener(QLinkMicServiceListenerlistener);移除麦位监听

	//获得连麦邀请处理
	QInvitationHandlergetInvitationHandler();获得连麦邀请处理

	//观众向主播连麦处理器
	QAudienceMicHandlergetAudienceMicHandler();观众向主播连麦处理器

	//主播处理自己被连麦处理器
	QAnchorHostMicHandlergetAnchorHostMicHandler();主播处理自己被连麦处理器
}

//麦位监听
QLinkMicServiceListener{

	//有人上麦
	//@param-micLinker:连麦者	
	voidonLinkerJoin(QMicLinkermicLinker);有人上麦

	//有人下麦
	//@param-micLinker:连麦者	
	voidonLinkerLeft(@NotNull()QMicLinkermicLinker);有人下麦

	//有人麦克风变化
	//@param-micLinker:连麦者	
	voidonLinkerMicrophoneStatusChange(@NotNull()QMicLinkermicLinker);有人麦克风变化

	//有人摄像头状态变化
	//@param-micLinker:连麦者	
	voidonLinkerCameraStatusChange(@NotNull()QMicLinkermicLinker);有人摄像头状态变化

	//有人被踢
	//@param-micLinker:连麦者	@param-msg:自定义扩展消息	
	voidonLinkerKicked(@NotNull()QMicLinkermicLinker,Stringmsg);有人被踢

	//有人扩展字段变化
	//@param-micLinker:连麦者	@param-QExtension:扩展信息	
	voidonLinkerExtensionUpdate(@NotNull()QMicLinkermicLinker,QExtensionQExtension);有人扩展字段变化
}

//主播端连麦器
QAnchorHostMicHandler{

	//设置混流适配器
	//@param-QLinkMicMixStreamAdapter:混流适配器	
	publicvoidsetMixStreamAdapter(@NotNull()QLinkMicMixStreamAdapterQLinkMicMixStreamAdapter);设置混流适配器
}

//混流适配器
QLinkMicMixStreamAdapter{

	//连麦开始如果要自定义混流画布和背景返回空则主播推流分辨率有多大就多大默认实现
	QMixStreaming.MixStreamParamsonMixStreamStart();连麦开始如果要自定义混流画布和背景返回空则主播推流分辨率有多大就多大默认实现

	//混流布局适配
	//@param-micLinkers:变化后所有连麦者	@param-target:当前变化的连麦者	@param-isJoin:当前变化的连麦者是新加还是离开	
	List<QMixStreaming.MergeOption>onResetMixParam(List<QMicLinker>micLinkers,QMicLinkertarget,booleanisJoin);混流布局适配
}

//观众连麦器
QAudienceMicHandler{

	//添加连麦监听
	//@param-listener:监听	
	voidaddLinkMicListener(LinkMicHandlerListenerlistener);添加连麦监听

	//移除连麦监听
	//@param-listener:监听	
	voidremoveLinkMicListener(LinkMicHandlerListenerlistener);移除连麦监听

	//开始上麦
	//@param-extension:麦位扩展字段	@param-cameraParams:摄像头参数 空代表不开	@param-microphoneParams:麦克参数  空代表不开	@param-callBack:上麦成功失败回调	
	voidstartLink(HashMap<String,String>extension,QCameraParamcameraParams,QMicrophoneParammicrophoneParams,QLiveCallBack<Void>callBack);开始上麦

	//我是不是麦上用户
	booleanisLinked();我是不是麦上用户

	//结束连麦
	//@param-callBack:操作回调	
	voidstopLink(QLiveCallBack<Void>callBack);结束连麦

	//上麦后可以切换摄像头
	//@param-callBack:	
	voidswitchCamera(QLiveCallBack<QCameraFace>callBack);上麦后可以切换摄像头

	//上麦后可以禁言本地视频流
	//@param-muted:	@param-callBack:	
	voidmuteCamera(booleanmuted,QLiveCallBack<Boolean>callBack);上麦后可以禁言本地视频流

	//上麦后可以禁用本地音频流
	//@param-muted:	@param-callBack:	
	voidmuteMicrophone(booleanmuted,QLiveCallBack<Boolean>callBack);上麦后可以禁用本地音频流

	//上麦后可以设置本地视频帧回调
	//@param-frameListener:	
	voidsetVideoFrameListener(QVideoFrameListenerframeListener);上麦后可以设置本地视频帧回调

	//上麦后可以设置音频帧回调
	//@param-frameListener:	
	voidsetAudioFrameListener(QAudioFrameListenerframeListener);上麦后可以设置音频帧回调

	//上麦后可以设置免费的默认美颜参数
	//@param-beautySetting:	
	voidsetDefaultBeauty(QBeautySettingbeautySetting);上麦后可以设置免费的默认美颜参数
}

//观众连麦处理器监听
 观众需要处理的事件
QAudienceMicHandler.LinkMicHandlerListener{

	//连麦模式连接状态连接成功后连麦器会主动禁用推流器改用rtc
	//@param-state:状态	
	voidonConnectionStateChanged(QRoomConnectionStatestate);连麦模式连接状态连接成功后连麦器会主动禁用推流器改用rtc

	//本地角色变化
	//@param-isLinker:当前角色是不是麦上用户 上麦后true 下麦后false	
	voidonRoleChange(booleanisLinker);本地角色变化
}

//音频帧监听
QAudioFrameListener{

	//音频帧回调
	//@param-srcBuffer:输入pcm数据	@param-size:大小	@param-bitsPerSample:位深	@param-sampleRate:采样率	@param-numberOfChannels:通道数	
	voidonAudioFrameAvailable(ByteBuffersrcBuffer,intsize,intbitsPerSample,intsampleRate,intnumberOfChannels);音频帧回调
}

//视频帧监听
QVideoFrameListener{

	//yuv帧回调
	//@param-data:yuv数据	@param-type:帧类型	@param-width:宽	@param-height:高	@param-rotation:旋转角度	@param-timestampNs:时间戳	
	defaultvoidonYUVFrameAvailable(byte[]data,QVideoFrameTypetype,intwidth,intheight,introtation,longtimestampNs);yuv帧回调

	//纹理ID回调
	//@param-textureID:输入的纹理ID	@param-type:纹理类型	@param-width:宽	@param-height:高	@param-rotation:旋转角度	@param-timestampNs:时间戳	@param-transformMatrix:转化矩阵	
	defaultintonTextureFrameAvailable(inttextureID,QVideoFrameTypetype,intwidth,intheight,introtation,longtimestampNs,float[]transformMatrix);纹理ID回调
}

//pk服务
QPKService{

	//主播设置混流适配器
	//@param-adapter:混流适配	
	voidsetPKMixStreamAdapter(QPKMixStreamAdapteradapter);主播设置混流适配器

	//添加pk监听
	//@param-QPKServiceListener:	
	voidaddServiceListener(QPKServiceListenerQPKServiceListener);添加pk监听

	//移除pk监听
	//@param-QPKServiceListener:	
	voidremoveServiceListener(QPKServiceListenerQPKServiceListener);移除pk监听

	//开始pk
	//@param-timeoutTimestamp:等待对方流超时时间时间戳 毫秒	@param-receiverRoomID:接受方所在房间ID	@param-receiverUID:接收方用户ID	@param-extension:扩展字段	@param-callBack:操作回调函数	
	voidstart(longtimeoutTimestamp,StringreceiverRoomID,StringreceiverUID,HashMap<String,String>extension,QLiveCallBack<QPKSession>callBack);开始pk

	//结束pk
	//@param-callBack:操作回调	
	voidstop(QLiveCallBack<Void>callBack);结束pk

	//主播设置对方的连麦预览
	//@param-view:预览窗口	
	voidsetPeerAnchorPreView(QPushRenderViewview);主播设置对方的连麦预览

	//获得pk邀请处理
	QInvitationHandlergetInvitationHandler();获得pk邀请处理

	//当前正在pk信息没有PK则空
	QPKSessioncurrentPKingSession();当前正在pk信息没有PK则空
}

//pk回调
QPKServiceListener{

	//pk开始回调观众刚进入房间如果房间正在pk也马上会回调
	//@param-pkSession:pk会话	
	voidonStart(@NotNull()QPKSessionpkSession);pk开始回调观众刚进入房间如果房间正在pk也马上会回调

	//pk结束回调
	//@param-pkSession:pk会话	@param-code:-1 异常结束 0主动结束 1对方结束	@param-msg:	
	voidonStop(@NotNull()QPKSessionpkSession,intcode,@NotNull()Stringmsg);pk结束回调

	//主播主动开始后收对方流超时pk没有建立起来
	//@param-pkSession:pk会话	
	voidonStartTimeOut(@NotNull()QPKSessionpkSession);主播主动开始后收对方流超时pk没有建立起来
}

//pk混流适配器
QPKMixStreamAdapter{

	//当pk开始如何混流
	//@param-pkSession:	
	List<QMixStreaming.MergeOption>onPKLinkerJoin(@NotNull()QPKSessionpkSession);当pk开始如何混流

	//pk开始时候混流画布变成多大返回null则原来主播有多大就有多大
	//@param-pkSession:	
	QMixStreaming.MixStreamParamsonPKMixStreamStart(@NotNull()QPKSessionpkSession);pk开始时候混流画布变成多大返回null则原来主播有多大就有多大

	//当pk结束后如果还有其他普通连麦者如何混流如果pk结束后没有其他连麦者则不会回调
	defaultList<QMixStreaming.MergeOption>onPKLinkerLeft();当pk结束后如果还有其他普通连麦者如何混流如果pk结束后没有其他连麦者则不会回调

	//当pk结束后如果还有其他普通连麦者如何混流如果pk结束后没有其他连麦者则不会回调返回空则默认之前的不变化
	defaultQMixStreaming.MixStreamParamsonPKMixStreamStop();当pk结束后如果还有其他普通连麦者如何混流如果pk结束后没有其他连麦者则不会回调返回空则默认之前的不变化
}

//公屏服务
QPublicChatService{

	//发送公聊
	//@param-msg:公屏消息内容	@param-callBack:操作回调	
	publicvoidsendPublicChat(Stringmsg,QLiveCallBack<QPublicChat>callBack);发送公聊

	//发送进入消息
	//@param-msg:消息内容	@param-callBack:操作回调	
	publicvoidsendWelCome(Stringmsg,QLiveCallBack<QPublicChat>callBack);发送进入消息

	//发送拜拜
	//@param-msg:消息内容	@param-callBack:操作回调	
	publicvoidsendByeBye(Stringmsg,QLiveCallBack<QPublicChat>callBack);发送拜拜

	//点赞
	//@param-msg:消息内容
            * @param callBack 操作回调	
	publicvoidsendLike(Stringmsg,QLiveCallBack<QPublicChat>callBack);点赞

	//自定义要显示在公屏上的消息
	//@param-action:消息code 用来区分要做什么响应	@param-msg:消息内容	@param-callBack:回调	
	publicvoidsendCustomPubChat(Stringaction,Stringmsg,QLiveCallBack<QPublicChat>callBack);自定义要显示在公屏上的消息

	//往本地公屏插入消息不发送到远端
	publicvoidpubMsgToLocal(QPublicChatchatModel);往本地公屏插入消息不发送到远端

	//添加监听
	//@param-lister:	
	publicvoidaddServiceLister(QPublicChatServiceListerlister);添加监听

	//移除监听
	//@param-lister:	
	publicvoidremoveServiceLister(QPublicChatServiceListerlister);移除监听
}

//
QPublicChatServiceLister{

	//收到公聊消息pubChat.action可以区分是啥类型的公聊消息
	//@param-pubChat:消息实体	
	voidonReceivePublicChat(QPublicChatpubChat);收到公聊消息pubChat.action可以区分是啥类型的公聊消息
}

//房间服务
QRoomService{

	//添加监听
	//@param-listener:	
	publicvoidaddRoomServiceListener(QRoomServiceListenerlistener);添加监听

	//移除监听
	//@param-listener:	
	publicvoidremoveRoomServiceListener(QRoomServiceListenerlistener);移除监听

	//获取当前房间
	publicQLiveRoomInfogetRoomInfo();获取当前房间

	//刷新房间信息
	publicvoidgetRoomInfo(QLiveCallBack<QLiveRoomInfo>callBack);刷新房间信息

	//跟新直播扩展信息
	//@param-extension:扩展字段	@param-callBack:操作回调	
	publicvoidupdateExtension(QExtensionextension,QLiveCallBack<Void>callBack);跟新直播扩展信息

	//当前房间在线用户
	//@param-pageNum:页号 1开始	@param-pageSize:每页大小	@param-callBack:操作回调	
	publicvoidgetOnlineUser(intpageNum,intpageSize,QLiveCallBack<List<QLiveUser>>callBack);当前房间在线用户

	//某个房间在线用户
	//@param-pageNum:页号 1开始	@param-pageSize:每页大小	@param-callBack:操作回调	@param-roomId:房间ID	
	publicvoidgetOnlineUser(intpageNum,intpageSize,StringroomId,QLiveCallBack<List<QLiveUser>>callBack);某个房间在线用户

	//使用用户ID搜索房间用户
	//@param-uid:用户ID	@param-callBack:操作回调	
	publicvoidsearchUserByUserId(Stringuid,QLiveCallBack<QLiveUser>callBack);使用用户ID搜索房间用户

	//使用用户imuid搜索用户
	//@param-imUid:用户im 用户ID	@param-callBack:操作回调	
	publicvoidsearchUserByIMUid(StringimUid,QLiveCallBack<QLiveUser>callBack);使用用户imuid搜索用户
}

//房间服务监听
QRoomServiceListener{

	//直播间某个属性变化
	//@param-extension:扩展字段	
	voidonRoomExtensionUpdate(QExtensionextension);直播间某个属性变化
}

//弹幕服务
QDanmakuService{

	//添加弹幕监听
	//@param-listener:弹幕消息监听	
	publicvoidaddDanmakuServiceListener(QDanmakuServiceListenerlistener);添加弹幕监听

	//移除弹幕监听
	//@param-listener:弹幕消息监听	
	publicvoidremoveDanmakuServiceListener(QDanmakuServiceListenerlistener);移除弹幕监听

	//发送弹幕消息
	//@param-msg:弹幕内容	@param-extension:扩展字段	@param-callBack:发送回调	
	publicvoidsendDanmaku(Stringmsg,HashMap<String,String>extension,QLiveCallBack<QDanmaku>callBack);发送弹幕消息
}

//弹幕消息监听
QDanmakuServiceListener{

	//收到弹幕消息
	//@param-danmaku:弹幕实体	
	voidonReceiveDanmaku(QDanmakudanmaku);收到弹幕消息
}

//聊天室服务
QChatRoomService{

	//添加聊天室监听
	//@param-chatServiceListener:监听	
	publicvoidaddServiceListener(QChatRoomServiceListenerchatServiceListener);添加聊天室监听

	//移除聊天室监听
	//@param-chatServiceListener:监听	
	publicvoidremoveServiceListener(QChatRoomServiceListenerchatServiceListener);移除聊天室监听

	//发c2c消息
	//@param-msg:消息内容	@param-memberID:成员im ID	@param-callBack:回调	
	voidsendCustomC2CMsg(Stringmsg,StringmemberID,QLiveCallBack<Void>callBack);发c2c消息

	//发群消息
	//@param-msg:消息内容	@param-callBack:回调	
	voidsendCustomGroupMsg(Stringmsg,QLiveCallBack<Void>callBack);发群消息

	//踢人
	//@param-msg:消息内容	@param-memberID:成员im ID	@param-callBack:回调	
	voidkickUser(Stringmsg,StringmemberID,QLiveCallBack<Void>callBack);踢人

	//禁言
	//@param-isMute:是否禁言	@param-msg:消息内容	@param-memberID:成员im ID	@param-duration:禁言时常	@param-callBack:回调	
	voidmuteUser(booleanisMute,Stringmsg,StringmemberID,longduration,QLiveCallBack<Void>callBack);禁言

	//添加管理员
	//@param-memberID:成员im ID	@param-callBack:回调	
	voidaddAdmin(StringmemberID,QLiveCallBack<Void>callBack);添加管理员

	//移除管理员
	//@param-msg:	@param-memberID:成员im ID	@param-callBack:回调	
	voidremoveAdmin(Stringmsg,StringmemberID,QLiveCallBack<Void>callBack);移除管理员
}

//聊天室监听
QChatRoomServiceListener{

	//Onuserjoin.
	//@param-memberID:the member id	
	defaultvoidonUserJoin(@NotNull()StringmemberID);Onuserjoin.

	//Onuserleft.
	//@param-memberID:the member id	
	defaultvoidonUserLeft(@NotNull()StringmemberID);Onuserleft.

	//Onreceivedc2cmsg.
	//@param-msg:the msg	@param-fromID:the from id	@param-toID:the to id	
	defaultvoidonReceivedC2CMsg(@NotNull()Stringmsg,@NotNull()StringfromID,@NotNull()StringtoID);Onreceivedc2cmsg.

	//Onreceivedgroupmsg.
	//@param-msg:the msg	@param-fromID:the from id	@param-toID:the to id	
	defaultvoidonReceivedGroupMsg(@NotNull()Stringmsg,@NotNull()StringfromID,@NotNull()StringtoID);Onreceivedgroupmsg.

	//Onuserkicked.
	//@param-memberID:the member id	
	defaultvoidonUserKicked(@NotNull()StringmemberID);Onuserkicked.

	//Onuserbemuted.
	//@param-isMute:the is mute	@param-memberID:the member id	@param-duration:the duration	
	defaultvoidonUserBeMuted(@NotNull()booleanisMute,@NotNull()StringmemberID,@NotNull()longduration);Onuserbemuted.

	//Onadminadd.
	//@param-memberID:the member id	
	defaultvoidonAdminAdd(@NotNull()StringmemberID);Onadminadd.

	//Onadminremoved.
	//@param-memberID:the member id	@param-reason:the reason	
	defaultvoidonAdminRemoved(@NotNull()StringmemberID,@NotNull()Stringreason);Onadminremoved.
}

//
RoomPage{
	public QLiveFunctionComponent showPKApplyComponent;//[主播收到pk邀请 展示弹窗 事件监听功能组件]
	public QLiveFunctionComponent showLinkMicApplyComponent;//[主播收到连麦申请 展示弹窗 事件监听功能组件]
	public QLiveFunctionComponent playerShowBeInvitedComponent;//[用户收到主播连麦邀请 展示弹窗]
	public QLiveFunctionComponent anchorOfflineMonitorComponent;//[房主离线事件处理]

	//
	publicintgetAnchorCustomLayoutID();

	//自定义布局如果需要替换自定义布局自定义主播端布局如果需要替换自定义布局
	//@param-anchorCustomLayoutID:自定义布局ID	
	publicvoidsetAnchorCustomLayoutID(intanchorCustomLayoutID);自定义布局如果需要替换自定义布局自定义主播端布局如果需要替换自定义布局

	//
	publicintgetPlayerCustomLayoutID();

	//自定义布局如果需要替换自定义布局自定义主播端布局如果需要替换自定义布局
	//@param-playerCustomLayoutID:自定义布局ID	
	publicvoidsetPlayerCustomLayoutID(intplayerCustomLayoutID);自定义布局如果需要替换自定义布局自定义主播端布局如果需要替换自定义布局

	//添加功能组件功能组件
	//@param-component:功能组件	
	publicfinalvoidaddFunctionComponent(@NotNull()QLiveComponentcomponent);添加功能组件功能组件

	//根据房间信息自动跳转主播页直播间或观众直播间
	//@param-context:安卓上下文	@param-roomInfo:房间信息	@param-callBack:回调	
	publicfinalvoidstartRoomActivity(@NotNull()Contextcontext,@NotNull()QLiveRoomInforoomInfo,@Nullable()QLiveCallBack<QLiveRoomInfo>callBack);根据房间信息自动跳转主播页直播间或观众直播间

	//跳转观众直播间
	//@param-context:安卓上下文	@param-liveRoomId:房间ID	@param-callBack:回调	
	publicfinalvoidstartPlayerRoomActivity(@NotNull()Contextcontext,@NotNull()StringliveRoomId,@Nullable()QLiveCallBack<QLiveRoomInfo>callBack);跳转观众直播间

	//跳转已经存在的主播直播间
	//@param-context:安卓上下文	@param-liveRoomId:直播间ID	@param-callBack:回调	
	publicfinalvoidstartAnchorRoomActivity(@NotNull()Contextcontext,@NotNull()StringliveRoomId,@Nullable()QLiveCallBack<QLiveRoomInfo>callBack);跳转已经存在的主播直播间

	//跳转到创建直播间开播页面
	//@param-context:安卓上下文	@param-callBack:回调	
	publicfinalvoidstartAnchorRoomWithPreview(@NotNull()Contextcontext,@Nullable()QLiveCallBack<QLiveRoomInfo>callBack);跳转到创建直播间开播页面
}

//房间列表页面
RoomListPage{

	//
	publicfinalintgetCustomLayoutID();

	//设置房间列表页面的自定义布局
	//@param-layoutID:拷贝kit_activity_room_list.xml 修改后的自定义布局	
	publicfinalvoidsetCustomLayoutID(intlayoutID);设置房间列表页面的自定义布局
}

//功能组件容器
QLiveFunctionComponent{

	//
	publicfinalbooleanisEnable();

	//功能组件是否可用
	//@param-enable:	
	publicfinalvoidsetEnable(booleanenable);功能组件是否可用

	//替换功能组件
	//@param-replaceComponent:	
	publicvoidreplace(@NotNull()QLiveComponentreplaceComponent);替换功能组件
}

//直播间内小组件
 <p>
 父接口：
 QClientLifeCycleListener -> client 生命周期
 LifecycleEventObserver ->  房间客户端生命周期
QLiveComponent{

	//绑定UI组件上下文生命每个组件都拥有组件上下文能获取到业务逻辑能力能和UI能力
	//@param-context:UI组件上下文	
	voidattachKitContext(@NotNull()QLiveUIKitContextcontext);绑定UI组件上下文生命每个组件都拥有组件上下文能获取到业务逻辑能力能和UI能力

	//绑定房间客户端
	//@param-client:推流/拉流客户端	
	voidattachLiveClient(@NotNull()QLiveClientclient);绑定房间客户端
}

//uikit 房间里的UI组件上下文
 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 2能获取房间client 主要资源和关键操作
QLiveUIKitContext{
	val androidContext: Context;//[安卓上下文]
	val fragmentManager: FragmentManager,;//[安卓FragmentManager 用于显示弹窗]
	val currentActivity: Activity;//[当前所在的Activity]
	val lifecycleOwner: LifecycleOwner;//[当前页面的安卓LifecycleOwner]
	 val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit;//[离开房间操作 在任意UI组件中可以操作离开房间]
	val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit;//[创建并且加入房间操作 在任意UI组件中可创建并且加入房间]
	val getPlayerRenderViewCall: () -> QPlayerRenderView?;//[获取当前播放器预览窗口 在任意UI组件中如果要对预览窗口变化可直接获取]
	 val getPusherRenderViewCall: () -> QPushRenderView?;//[获取推流预览窗口  在任意UI组件中如果要对预览窗口变化可直接获取]
}

//商品信息
QItem{
	public String liveID;//[所在房间ID]
	public String itemID;//[商品ID]
	public int order;//[商品号]
	public String title;//[标题]
	public String tags;//[商品标签 多个以,分割]
	public String thumbnail;//[缩略图]
	public String link;//[链接]
	public String currentPrice;//[当前价格]
	public String originPrice;//[原价]
	public int status;//[上架状态
 已下架
 PULLED(0),
 已上架售卖
 ON_SALE(1),
 上架不能购买
 ONLY_DISPLAY(2);]
	public Map extensions;//[商品扩展字段]
}

//商品状态枚举
QItemStatus{
	public static final QItemStatus PULLED;//[已下架]
	public static final QItemStatus ON_SALE;//[已上架售卖]
	public static final QItemStatus ONLY_DISPLAY;//[上架不能购买]

	//
	publicstaticcom.qlive.shoppingservice.QItemStatus[]values();

	//
	publicstaticcom.qlive.shoppingservice.QItemStatusvalueOf(java.lang.Stringname);

	//
	publicintgetValue();
}

//商品顺序参数
QOrderParam{
	public String itemID;//[商品ID]
	public int order;//[调节后的顺序]
}

//单个商品调节顺序
QSingleOrderParam{
	public String itemID;//[商品ID]
	public int from;//[原来的顺序]
	public int to;//[调节后的顺序]
}

//购物服务
QShoppingService{

	//获取直播间所有商品
	//@param-callBack:回调	
	voidgetItemList(QLiveCallBack<List<QItem>>callBack);获取直播间所有商品

	//跟新商品状态
	//@param-itemID:商品ID	@param-status:商品状态	@param-callBack:回调	
	voidupdateItemStatus(StringitemID,QItemStatusstatus,QLiveCallBack<Void>callBack);跟新商品状态

	//
	voidupdateItemStatus(HashMap<String,QItemStatus>newStatus,QLiveCallBack<Void>callBack);

	//跟新商品扩展字段并通知房间所有人
	//@param-item:商品	@param-extension:扩展字段	@param-callBack:回调	
	voidupdateItemExtension(QItemitem,QExtensionextension,QLiveCallBack<Void>callBack);跟新商品扩展字段并通知房间所有人

	//设置讲解中的商品并通知房间所有人
	//@param-item:商品	@param-callBack:回调	
	voidsetExplaining(QItemitem,QLiveCallBack<Void>callBack);设置讲解中的商品并通知房间所有人

	//取消设置讲解中的商品并通知房间所有人
	//@param-callBack:回调	
	voidcancelExplaining(QLiveCallBack<Void>callBack);取消设置讲解中的商品并通知房间所有人

	//获取当前讲解中的
	QItemgetExplaining();获取当前讲解中的

	//跟新单个商品顺序
	//@param-param:调节顺序	@param-callBack:回调	
	voidchangeSingleOrder(QSingleOrderParamparam,QLiveCallBack<Void>callBack);跟新单个商品顺序

	//跟新单个商品顺序
	//@param-params:所有商品 调节后的顺序	@param-callBack:回调	
	voidchangeOrder(List<QOrderParam>params,QLiveCallBack<Void>callBack);跟新单个商品顺序

	//删除商品
	//@param-itemIDS:	@param-callBack:	
	voiddeleteItems(List<String>itemIDS,QLiveCallBack<Void>callBack);删除商品

	//添加购物服务监听
	//@param-listener:监听	
	voidaddServiceListener(QShoppingServiceListenerlistener);添加购物服务监听

	//移除商品监听
	//@param-listener:监听	
	voidremoveServiceListener(QShoppingServiceListenerlistener);移除商品监听
}

//购物车服务监听
QShoppingServiceListener{

	//正在展示的商品切换通知
	//@param-item:商品	
	voidonExplainingUpdate(QItemitem);正在展示的商品切换通知

	//商品扩展字段跟新通知
	//@param-item:商品	@param-extension:扩展字段	
	voidonExtensionUpdate(QItemitem,QExtensionextension);商品扩展字段跟新通知
}

```
