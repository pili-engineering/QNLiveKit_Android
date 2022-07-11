package com.qlive.avparam;

public class QMixStreaming {
    /**
     * 混流画布参数
     */
    public static class MixStreamParams {
        private int mixStreamWidth = 0;
        private int mixStringHeight = 0;
        private int mixBitrate = 3420 * 1000;
        private int FPS = 25;
        private TranscodingLiveStreamingImage backGroundImg = null;

        public int getMixStreamWidth() {
            return mixStreamWidth;
        }

        public void setMixStreamWidth(int mixStreamWidth) {
            this.mixStreamWidth = mixStreamWidth;
        }

        public int getMixStringHeight() {
            return mixStringHeight;
        }

        public void setMixStringHeight(int mixStringHeight) {
            this.mixStringHeight = mixStringHeight;
        }

        public int getMixBitrate() {
            return mixBitrate;
        }

        public void setMixBitrate(int mixBitrate) {
            this.mixBitrate = mixBitrate;
        }

        public int getFPS() {
            return FPS;
        }

        public void setFPS(int FPS) {
            this.FPS = FPS;
        }

        public TranscodingLiveStreamingImage getBackGroundImg() {
            return backGroundImg;
        }

        /**
         * 设置背景图片
         * @param backGroundImg 背景图片
         */
        public void setBackGroundImg(TranscodingLiveStreamingImage backGroundImg) {
            this.backGroundImg = backGroundImg;
        }
    }

    /**
     * 背景图片
     */
    public static class TranscodingLiveStreamingImage {
        private String url = "";
        private int x = 0;
         private int y = 0;
         private int width = 0;
         private int height = 0;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }


    public static interface TrackMergeOption {
    }

    /**
     * 摄像头混流参数
     */
    public static class CameraMergeOption implements TrackMergeOption {
        /**
         * 是否参与混流
         */
         private boolean isNeed = false;
         private int x = 0;
         private int y = 0;
         private int z = 0;
         private int width = 0;
         private int height = 0;
        // var stretchMode: QNRenderMode? = null

        public boolean isNeed() {
            return isNeed;
        }

        /**
         * 设置 是否参与混流
         * @param need 是否参与混流
         */
        public void setNeed(boolean need) {
            isNeed = need;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    /**
     * 某个用户的混流参数
     * 只需要指定用户ID 和他的摄像头麦克风混流参数
     */
    public static class MergeOption {
         private String uid = "";
         private CameraMergeOption cameraMergeOption = new CameraMergeOption();
         private MicrophoneMergeOption microphoneMergeOption = new MicrophoneMergeOption();

        public String getUid() {
            return uid;
        }

        /**
         * 当前混流参数作用于哪个用户
         * @param uid 用户ID
         */
        public void setUid(String uid) {
            this.uid = uid;
        }

        public CameraMergeOption getCameraMergeOption() {
            return cameraMergeOption;
        }

        /**
         * 设置麦克风混流参数
         * @param cameraMergeOption 麦克风参数
         */
        public void setCameraMergeOption(CameraMergeOption cameraMergeOption) {
            this.cameraMergeOption = cameraMergeOption;
        }

        public MicrophoneMergeOption getMicrophoneMergeOption() {
            return microphoneMergeOption;
        }

        /**
         * 设置摄像头混流参数
         * @param microphoneMergeOption 摄像头参数
         */
        public void setMicrophoneMergeOption(MicrophoneMergeOption microphoneMergeOption) {
            this.microphoneMergeOption = microphoneMergeOption;
        }
    }

    /**
     * 麦克风混流参数
     */
    public static class MicrophoneMergeOption implements TrackMergeOption {
        private boolean isNeed = false;

        public boolean isNeed() {
            return isNeed;
        }
        /**
         * 设置 是否参与混流
         * @param need 是否参与混流
         */
        public void setNeed(boolean need) {
            isNeed = need;
        }
    }
}