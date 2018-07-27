package com.stringee;

import android.content.Context;

import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

/**
 * Created by luannguyen on 9/19/2017.
 */

public class StringeeStream {

    public static class StringeeAudioStats {
        public long audioBytesReceived;
        //public int audioBytesSent;
        public long videoBytesReceived;
        public long audioBytesSent;
        public long videoBytesSent;
        public double timeStamp;

        public StringeeAudioStats() {
        }

        public StringeeAudioStats(String audioSent, String videoSent, String audioRev, String videoRev) {
            try {
                this.audioBytesSent = Long.parseLong(audioSent);
            } catch (Exception e) {
                e.printStackTrace();
                this.audioBytesSent = -1;
            }
            try {
                this.videoBytesSent = Long.parseLong(videoSent);
            } catch (Exception e) {
                e.printStackTrace();
                this.videoBytesSent = -1;
            }
            // rev
            try {
                this.audioBytesReceived = Long.parseLong(audioRev);
            } catch (Exception e) {
                e.printStackTrace();
                this.audioBytesReceived = -1;
            }
            try {
                this.videoBytesReceived = Long.parseLong(videoRev);
            } catch (Exception e) {
                e.printStackTrace();
                this.videoBytesReceived = -1;
            }
        }
    }

    private boolean isLocal;
    private MediaStream mediaStream;
    private SurfaceViewRenderer renderer;
    private VideoRenderer mRenderer;

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public MediaStream getMediaStream() {
        return mediaStream;
    }

    public void setMediaStream(MediaStream mediaStream) {
        this.mediaStream = mediaStream;
    }

    public VideoRenderer getRenderer() {
        return mRenderer;
    }

    public void setRenderer(VideoRenderer mRenderer) {
        this.mRenderer = mRenderer;
    }

    public SurfaceViewRenderer getView(Context context) {
        if (renderer == null) {
            renderer = new SurfaceViewRenderer(context);
            renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        }
        return renderer;
    }

    public SurfaceViewRenderer getSurfaceViewRenderer() {
        return renderer;
    }
}


