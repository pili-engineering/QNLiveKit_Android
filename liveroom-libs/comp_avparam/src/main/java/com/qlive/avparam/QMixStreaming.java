package com.qlive.avparam;

public class MixStreaming {
    /**
     * 混流画布参数
     */
    public static class QMixStreamParams {
        private int mixStreamWidth = 0;
        private int mixStringHeight = 0;
        private int mixBitrate = 3420 * 1000;
        private int FPS = 25;
        private QTranscodingLiveStreamingImage backGroundImg = null;

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

        public QTranscodingLiveStreamingImage getBackGroundImg() {
            return backGroundImg;
        }

        public void setBackGroundImg(QTranscodingLiveStreamingImage backGroundImg) {
            this.backGroundImg = backGroundImg;
        }
    }

    /**
     * 背景图片
     */
    public static class QTranscodingLiveStreamingImage {
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

    public static class MicrophoneMergeOption implements TrackMergeOption {
         private boolean isNeed = false;

        public boolean isNeed() {
            return isNeed;
        }

        public void setNeed(boolean need) {
            isNeed = need;
        }
    }

    public static class CameraMergeOption implements TrackMergeOption {
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

    public static class QMergeOption {
         private String uid = "";
         private CameraMergeOption cameraMergeOption = new CameraMergeOption();
         private MicrophoneMergeOption microphoneMergeOption = new MicrophoneMergeOption();

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public CameraMergeOption getCameraMergeOption() {
            return cameraMergeOption;
        }

        public void setCameraMergeOption(CameraMergeOption cameraMergeOption) {
            this.cameraMergeOption = cameraMergeOption;
        }

        public MicrophoneMergeOption getMicrophoneMergeOption() {
            return microphoneMergeOption;
        }

        public void setMicrophoneMergeOption(MicrophoneMergeOption microphoneMergeOption) {
            this.microphoneMergeOption = microphoneMergeOption;
        }
    }
}