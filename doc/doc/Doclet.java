//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import com.sun.javadoc.*;
//import com.sun.tools.javac.code.Symbol;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javadoc.ExecutableMemberDocImpl;
//import com.sun.tools.javadoc.MethodDocImpl;
//import com.sun.tools.javadoc.ProgramElementDocImpl;
//
///**
// * 类说明：打印类及其字段、方法的注释<br>
// * 使用javadoc实现<br>
// * 需要在工程中加载jdk中的包$JAVA_HOME/lib/tools.jar
// */
//public class Doclet {
//
//    /**
//     * 测试
//     */
//    public static void main(String[] args) {
//        //java源文件的路径
//        ArrayList<String> sources = new ArrayList<>();
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/qlivesdk/src/main/java/com/qlive/sdk/QLive.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/qlivesdk/src/main/java/com/qlive/sdk/QLiveUIKit.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/qlivesdk/src/main/java/com/qlive/sdk/QUserInfo.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QTokenGetter.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QSdkConfig.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QRooms.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QLiveStatus.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QLiveCallBack.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QClientType.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QClientLifeCycleListener.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QInvitationHandler.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QInvitationHandlerListener.java");
//
//        //java been
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QCreateRoomParam.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QDanmaku.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QExtension.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QInvitation.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QLiveRoomInfo.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QLiveRoomInfo.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QLiveUser.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QMicLinker.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QPKSession.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QPublicChat.java");
//
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QPlayerEventListener.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QBeautySetting.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QBeautySetting.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QConnectionStatusLister.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QPlayerRenderView.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QMicrophoneParam.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QCameraParam.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QMixStreaming.java");
//
//        //推流
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/been/QPublicChat.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-pushclient/src/main/java/com/qlive/pushclient/QPusherClient.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_rtclive/src/main/java/com/qlive/rtclive/QPushRenderView.java");
//
//
//        //拉流端
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-pullclient/src/main/java/com/qlive/playerclient/QPlayerClient.java");
//
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-core/src/main/java/com/qlive/core/QLiveStatusListener.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/linkmicservice/src/main/java/com/qlive/linkmicservice/QLinkMicService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/linkmicservice/src/main/java/com/qlive/linkmicservice/QLinkMicServiceListener.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/linkmicservice/src/main/java/com/qlive/linkmicservice/QAnchorHostMicHandler.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/linkmicservice/src/main/java/com/qlive/linkmicservice/QLinkMicMixStreamAdapter.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/linkmicservice/src/main/java/com/qlive/linkmicservice/QAudienceMicHandler.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QAudioFrameListener.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/liveroom-libs/comp_avparam/src/main/java/com/qlive/avparam/QVideoFrameListener.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/pkservice/src/main/java/com/qlive/pkservice/QPKService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/pkservice/src/main/java/com/qlive/pkservice/QPKServiceListener.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/pkservice/src/main/java/com/qlive/pkservice/QPKMixStreamAdapter.java");
//
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/publicchatservice/src/main/java/com/qlive/pubchatservice/QPublicChatService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/publicchatservice/src/main/java/com/qlive/pubchatservice/QPublicChatServiceLister.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/roomservice/src/main/java/com/qlive/roomservice/QRoomService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/roomservice/src/main/java/com/qlive/roomservice/QRoomServiceListener.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/roomservice/src/main/java/com/qlive/roomservice/QRoomService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/roomservice/src/main/java/com/qlive/roomservice/QRoomService.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/danmakuservice/src/main/java/com/qlive/danmakuservice/QDanmakuService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/danmakuservice/src/main/java/com/qlive/danmakuservice/QDanmakuServiceListener.java");
//
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/chatservice/src/main/java/com/qlive/chatservice/QChatRoomService.java");
//        sources.add("/Users/manjiale/dev/QNLiveKit_Android/service/chatservice/src/main/java/com/qlive/chatservice/QChatRoomServiceListener.java");
//        //打印
//        try {
//            println(sources);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 打印类及其字段、方法的注释
//     *
//     * @param sources java源文件路径
//     */
//    private static HashMap<String, String> links = new HashMap<>();
//
//    private static String checkLinker(String name) {
//        if (name.equals("MicrophoneMergeOption")) {
//            name = "QMixStreaming.MicrophoneMergeOption";
//        }
//        if (name.equals("TranscodingLiveStreamingImage")) {
//            name = "QMixStreaming.TranscodingLiveStreamingImage";
//        }
//        if (name.equals("CameraMergeOption")) {
//            name = "QMixStreaming.CameraMergeOption";
//        }
//        if (name.equals("MergeOption")) {
//            name = "QMixStreaming.MergeOption";
//        }
//
//        if (name.equals("LinkMicHandlerListener")) {
//            name = "QAudienceMicHandler.LinkMicHandlerListener";
//        }
//
//
//        if (links.get(name) == null) {
//            return name;
//        } else {
//            return "{{" + name + "}}";
//        }
//    }
//
//    private static String getClassType(ClassDoc classDoc) {
//        if (classDoc.isEnum()) {
//            return "enum";
//        }
//        if (classDoc.isInterface()) {
//            return "interface";
//        }
//        if (classDoc.isClass()) {
//            return "class";
//        }
//        return "";
//    }
//
//    public static void println(ArrayList<String> sources) throws NoSuchFieldException, IllegalAccessException {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("-doclet");
//        list.add(Doclet.class.getName());
//        list.addAll(sources);
//        com.sun.tools.javadoc.Main.execute(list.toArray(new String[list.size()]));
//
//        ClassDoc[] classes = Doclet.root.classes();
//
//        int DocIndex = 12045;
//        for (ClassDoc classDoc : classes) {
//            if (classDoc.name().equals("QMixStreaming")) {
//                continue;
//            }
//            if (classDoc.name().equals("QMixStreaming.TrackMergeOption")) {
//                continue;
//            }
//            if (classDoc.name().equals("QMicrophoneParam")) {
//                DocIndex = 12073;
//            }
//            if (classDoc.name().equals("QMixStreaming.CameraMergeOption")) {
//                DocIndex = 12078;
//            }
//            if (classDoc.name().equals("QMixStreaming.MicrophoneMergeOption")) {
//                DocIndex = 12080;
//            }
//
//            links.put(classDoc.name(), "https://developer.qiniu.com/lowcode/api/" + String.valueOf(DocIndex++) + "/" + classDoc.name());
//        }
//
//        for (ClassDoc classDoc : classes) {
//            System.out.println(classDoc.name() + "  !");
//            DocFormat format = new DocFormat();
//            format.name = classDoc.name();
//            format.home = false;
//
//            format.describe = new DocFormat.Describe();
//            format.describe.content.add(getClassType(classDoc) + " " + classDoc.qualifiedName());
//            format.describe.content.add(classDoc.commentText());
//
//            format.reflect = links;
//
////            DocFormat.BlockItem classItem = new DocFormat.BlockItem();
////            classItem.name = classDoc.qualifiedName();
////            classItem.desc.add( classDoc.commentText());
//
//            FieldDoc[] fields = classDoc.fields();
//            DocFormat.BlockItem filedItem = new DocFormat.BlockItem();
//
//
//            filedItem.name = "字段";
//            for (FieldDoc field : fields) {
//                DocFormat.ElementItem i = new DocFormat.ElementItem();
//                i.name = field.name();
//                i.desc.add(field.commentText());
//                i.sign = field.modifiers() + " " + checkLinker(field.type().simpleTypeName()) + " " + field.name();
//                filedItem.elements.add(i);
//
//            }
//            if (filedItem.elements.size() > 0) {
//                format.blocks.add(filedItem);
//            }
//
//            DocFormat.BlockItem methodItem = new DocFormat.BlockItem();
//            methodItem.name = "方法";
//
//            if (!classDoc.isEnum()) {
//                MethodDoc[] methods = classDoc.methods();
//                for (MethodDoc method : methods) {
//
//                    DocFormat.ElementItem elementItem = new DocFormat.ElementItem();
//                    elementItem.name = method.name();
//                    elementItem.desc.add(method.commentText());
//                    elementItem.returns = checkLinker(method.returnType().simpleTypeName());
//
//                    //  elementItem.sign = method.toString();
//
//                    Class<ProgramElementDocImpl> clz = ProgramElementDocImpl.class;
//                    Field ageField = clz.getDeclaredField("tree");
//                    ageField.setAccessible(true);
//                    JCTree ageValue = (JCTree) ageField.get(method);
//                    String mn = ageValue.toString();
//                    elementItem.sign = mn;
//                    //  elementItem.sign = method.modifiers() + " " + method.returnType() + " " + method.name() + " " + method.signature();
//                    Parameter[] parameters = method.parameters();
//                    ParamTag[] tags = method.paramTags();
//
//
//                    if (method.name().equals("auth")) {
//                        if (elementItem.note == null) {
//                            elementItem.note = new ArrayList<>();
//                        }
//                        elementItem.note.add("认证成功后才能使用qlive的功能");
//                    }
//
//                    int index = 0;
//                    for (ParamTag tag : tags) {
//                        DocFormat.ParameterItem parameterItem = new DocFormat.ParameterItem();
//                        parameterItem.name = tag.parameterName();
//                        parameterItem.desc = tag.parameterComment();
//                        parameterItem.type = checkLinker(parameters[index].type().simpleTypeName());
//                        elementItem.parameters.add(parameterItem);
//                        index++;
//                    }
//                    methodItem.elements.add(elementItem);
//                }
//
//                if (methods.length > 0) {
//                    format.blocks.add(methodItem);
//                }
//            }
//            if (format.blocks.size() > 0) {
//                System.out.println(links.get(classDoc.name()));
//                System.out.println(format.toJson());
//            }
//
//        }
//
//        System.out.println("api概览");
//        DocFormat format = new DocFormat();
//        format.name = "api概览";
//        format.home = true;
//        format.reflect = links;
//
//        for (ClassDoc classDoc : classes) {
//            DocFormat.BlockItem classItem = new DocFormat.BlockItem();
//            classItem.name = "";
//            classItem.desc.add(checkLinker(classDoc.name()));
//            classItem.desc.add(classDoc.commentText());
//            format.blocks.add(classItem);
//        }
//        System.out.println(format.toJson());
//    }
//
//    /**
//     * 文档根节点
//     */
//    private static RootDoc root;
//
//    /**
//     * javadoc调用入口
//     *
//     * @param root
//     * @return
//     */
//    public static boolean start(RootDoc root) {
//        Doclet.root = root;
//        return true;
//    }
//}
