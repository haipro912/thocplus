package com.stringee;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.StatsObserver;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by luannguyen on 9/27/16.
 */
public class StringeeCall {
    public static final String AUDIO_TRACK_ID = "ARDAMSa0";
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    private static final String TAG = StringeeCall.class.getSimpleName();
    private PeerConnection peerConnection;
    private AudioSource audioSource;
    private AudioTrack localAudioTrack;
    private static VideoSource videoSource;
    private static VideoCapturer videoCapturer;//to switch camera
    private static VideoTrack localVideoTrack;
    private Context mContext;
    public boolean callStarted = false;

    LinkedList<StringeeIceCandidate> queueIceCandidate = new LinkedList<>();

    private LinkedList<StringeeIceServer> iceServers = new LinkedList<>();

    StringeeCallListener listener = null;
    boolean iceConnected = false;

    PeerConnectionFactory peerConnectionFactory;
    final ScheduledExecutorService executor;

    private SessionDescription localDescription;
    private boolean sdpSent;
    private EglBase.Context localContext;
    private EglBase.Context remoteContext;
    private EglBase eglBase;
    private String preferedAudioCodec;
    private String preferedVideoCodec;

    private CallConnectionListener connectionListener;

    public StringeeCall(StringeeCallListener listener) {
        executor = Executors.newSingleThreadScheduledExecutor();
        this.listener = listener;
    }

    public void setConnectionListener(CallConnectionListener listener) {
        this.connectionListener = listener;
    }

    public CallConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void init(final Context context) {
        Log.e("Stringee", "init begin");
        mContext = context;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("Stringee", "init begin execute");
                // Create peer connection factory.
                eglBase = EglBase.create();
                localContext = eglBase.getEglBaseContext();
                remoteContext = eglBase.getEglBaseContext();
                PeerConnectionFactory.initializeAndroidGlobals(context.getApplicationContext(), false);
                peerConnectionFactory = new PeerConnectionFactory(null);
                peerConnectionFactory.setVideoHwAccelerationOptions(localContext, remoteContext);
                Log.e("Stringee", "init end execute");
            }
        });
    }

    public void startCall(final boolean isCaller, final boolean isCallOut, final boolean hasVideo, final int quality,
                          final JSONObject sdpObject) {
        //Logging.enableLogToDebugOutput(Logging.Severity.LS_VERBOSE);
        Log.e("Stringee", "startCall begin");
        callStarted = true;

        final StringeeCreateSdpObserver createSdpObserver = new StringeeCreateSdpObserver(true, this);

        final PCObserver pcObserver = new PCObserver(this);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("Stringee", "startCall begin execute");
                // Create peer connection constraints.
                MediaConstraints pcConstraints = new MediaConstraints();
                pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));

                LinkedList<PeerConnection.IceServer> iceServers = createIceServers();
                PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
                /*if (isCallOut) {
                    rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXCOMPAT;
                } else {
                    rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.BALANCED;
                }*/
                //TODO old version
                /*rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.BALANCED;
                rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;*/
                if (isCallOut) {
                    rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
                    //rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE;
                    rtcConfig.iceTransportsType = PeerConnection.IceTransportsType.NOHOST;
                    rtcConfig.pruneTurnPorts = true;
                } else {
                    rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.BALANCED;
                    //rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
                }
                rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
                rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
                rtcConfig.iceRegatherOnFailedNetworksInterval = Integer.valueOf(20000);
                //rtcConfig.iceRegatherIntervalRange = new PeerConnection.IntervalRange(5000, 30000);//TODO test
                //disable TCP
                rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
                Log.e("Stringee", "startCall begin createPeer");
                peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, pcConstraints, pcObserver);
                Log.e("Stringee", "startCall end createPeer");
                MediaStream mediaStream = peerConnectionFactory.createLocalMediaStream("ARDAMS");

                Log.e("Stringee", "VIETTEL_DEBUG: 01");

                MediaConstraints audioConstraints = new MediaConstraints();
                audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
                Log.e("Stringee", "VIETTEL_DEBUG: 02");
                localAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_TRACK_ID, audioSource);
                Log.e("Stringee", "VIETTEL_DEBUG: 03");
                localAudioTrack.setEnabled(true);
                mediaStream.addTrack(localAudioTrack);
                Log.e("Stringee", "VIETTEL_DEBUG: 04");

                if (!isCallOut) {
                    int minWidth = StringeeConstant.VIDEO_NORMAL_MIN_WIDTH;
                    int minHeight = StringeeConstant.VIDEO_NORMAL_MIN_HEIGHT;
                    switch (quality) {
                        case StringeeConstant.VIDEO_QUALITY_NORMAL:
                            minWidth = StringeeConstant.VIDEO_NORMAL_MIN_WIDTH;
                            minHeight = StringeeConstant.VIDEO_NORMAL_MIN_HEIGHT;
                            break;
                        case StringeeConstant.VIDEO_QUALITY_HD:
                            minWidth = StringeeConstant.VIDEO_HD_MIN_WIDTH;
                            minHeight = StringeeConstant.VIDEO_HD_MIN_HEIGHT;
                            break;
                        case StringeeConstant.VIDEO_QUALITY_FULL_HD:
                            minWidth = StringeeConstant.VIDEO_FULL_HD_MIN_WIDTH;
                            minHeight = StringeeConstant.VIDEO_FULL_HD_MIN_HEIGHT;
                            break;
                    }
                    if (isCameraAvailable(mContext)) {
                        videoCapturer = createVideoCapturer();
                        Log.e("Stringee", "VIETTEL_DEBUG: 05");
                        videoSource = peerConnectionFactory.createVideoSource(videoCapturer);
                        videoCapturer.startCapture(minWidth, minHeight, 30);
                        localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_TRACK_ID, videoSource);
                        localVideoTrack.setEnabled(hasVideo);
                        mediaStream.addTrack(localVideoTrack);
                        Log.e("Stringee", "VIETTEL_DEBUG: 06");
                    } else {
                        Log.e("Stringee", "VIETTEL_DEBUG: no camera");
                    }
                }

                peerConnection.addStream(mediaStream);
                Log.e("Stringee", "VIETTEL_DEBUG: 07");
                if (listener != null) {
                    StringeeStream stringeeStream = new StringeeStream();
                    stringeeStream.setLocal(true);
                    stringeeStream.setMediaStream(mediaStream);
                    listener.onLocalStreamCreated(stringeeStream);
                    Log.e("Stringee", "VIETTEL_DEBUG: 08");
                }

                if (isCaller) {
                    // Create SDP constraints.
                    MediaConstraints sdpMediaConstraints = new MediaConstraints();
                    sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
                    if (!isCallOut) {
                        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo",
                                "true"));
                    } else {
                        sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo",
                                "false"));
                    }
                    Log.e("Stringee", "VIETTEL_DEBUG: 09");
                    peerConnection.createOffer(createSdpObserver, sdpMediaConstraints);
                    Log.e("Stringee", "VIETTEL_DEBUG: 10");
                } else {
                    try {
                        String sdpType = sdpObject.getString("type");
                        String sdp = sdpObject.getString("sdp");

                        StringeeSessionDescription.Type type = StringeeSessionDescription.Type.OFFER;
                        if (sdpType.equals("offer") || sdpType.equals("0")) {
                            type = StringeeSessionDescription.Type.OFFER;
                            Log.e("Stringee", "+++++++++++++++++++++++ sdp OFFER received");
                        } else if (sdpType.equals("answer") || sdpType.equals("2")) {
                            Log.e("Stringee", "+++++++++++++++++++++++ sdp ANSWER received");
                            type = StringeeSessionDescription.Type.ANSWER;
                        } else if (sdpType.equals("pranswer") || sdpType.equals("1")) {
                            type = StringeeSessionDescription.Type.PRANSWER;
                        }
                        StringeeSessionDescription sessionDescription = new StringeeSessionDescription(type, sdp);
                        setRemoteDescription(sessionDescription);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Log.e("Stringee", "startCall end execute");
            }
        });
    }

    public void stopCall() {
        if (!callStarted) {
            return;
        }
        callStarted = false;

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (peerConnection != null) {
                    peerConnection.dispose();
                    peerConnection = null;
                }
                if (audioSource != null) {
                    audioSource.dispose();
                    audioSource = null;
                }

                if (videoCapturer != null) {
                    try {
                        videoCapturer.stopCapture();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    videoCapturer.dispose();
                    videoCapturer = null;
                }

                if (videoSource != null) {
                    videoSource.dispose();
                    videoSource = null;
                }
                //Closing peer connection factory.
                if (peerConnectionFactory != null) {
                    peerConnectionFactory.dispose();
                    peerConnectionFactory = null;
                }
                if (eglBase != null) {
                    eglBase.release();
                    eglBase = null;
                }
            }
        });
    }

    //TODO addlog
    public void setRemoteDescription(final StringeeSessionDescription sessionDescription) {
        boolean setOffer = true;
        final SessionDescription.Type type;
        if (sessionDescription.type == StringeeSessionDescription.Type.OFFER) {
            type = SessionDescription.Type.OFFER;
            setOffer = true;
        } else if (sessionDescription.type == StringeeSessionDescription.Type.PRANSWER) {
            type = SessionDescription.Type.PRANSWER;
            setOffer = false;
        } else if (sessionDescription.type == StringeeSessionDescription.Type.ANSWER) {
            type = SessionDescription.Type.ANSWER;
            setOffer = false;
        } else {
            type = SessionDescription.Type.OFFER;
        }

        final StringeeSetSdpObserver setSdpObserver = new StringeeSetSdpObserver(false, setOffer, this);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                SessionDescription webrtcSdp = new SessionDescription(type, sessionDescription.description);
                Log.e("Stringee", "...................................... setRemoteDescription");
                peerConnection.setRemoteDescription(setSdpObserver, webrtcSdp);
            }
        });
    }


    //TODO addlog
    public void addIceCandidate(final StringeeIceCandidate iceCandidate, final boolean isCallout) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //xem la relay hay host
                String[] sdpA = iceCandidate.sdp.split(" ");
                String transport = sdpA[2];
                String candType = sdpA[7];

                IceCandidate webrtcIceCandidate = new IceCandidate(iceCandidate.sdpMid, iceCandidate.sdpMLineIndex,
                        iceCandidate.sdp);
                if (isCallout) {
                    peerConnection.addIceCandidate(webrtcIceCandidate);
                } else {
                    //peerConnection.addIceCandidate(webrtcIceCandidate);
                    if (transport.equals("udp") && candType.equals("relay")) {
                        Log.e("Stringee", "add luon ice candidate RELAY +++++++++++++++++++++++++: " + iceCandidate
                                .sdp);
                        peerConnection.addIceCandidate(webrtcIceCandidate);
                    }/* else if (iceConnected) {
                        Log.e("Stringee", "add luon vi da connected +++++++++++++++++++++++++: " + iceCandidate.sdp);
                        peerConnection.addIceCandidate(webrtcIceCandidate);
                    } else {
                        Log.e("Stringee", "CHUA add ice candidate RELAY, cho iceconnected +++++++++++++++++++++++++:
                        " + iceCandidate.sdp);
                        queueIceCandidate.add(iceCandidate);
                    }*/
                }
            }
        });
    }

    private LinkedList<PeerConnection.IceServer> createIceServers() {
        LinkedList<PeerConnection.IceServer> list = new LinkedList<>();

        for (StringeeIceServer ice : iceServers) {
            PeerConnection.IceServer iceServer1 = new PeerConnection.IceServer(ice.uri, ice.username, ice.password);
            list.add(iceServer1);
        }

        return list;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public List<StringeeIceServer> getIceServers() {
        return iceServers;
    }

    public void setIceServers(LinkedList<StringeeIceServer> iceServers) {
        this.iceServers = iceServers;
    }

    public void setListener(StringeeCallListener listener) {
        this.listener = listener;
    }

    public boolean isCallStarted() {
        return callStarted;
    }

    public void setCallStarted(boolean callStarted) {
        this.callStarted = callStarted;
    }

    public void setMicrophoneMute(final boolean isMute) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (localAudioTrack != null) {
                    localAudioTrack.setEnabled(!isMute);
                }
            }
        });
    }

    public void getStats(final AudioStatsListener statsListener) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peerConnection.getStats(new StatsObserver() {
                    @Override
                    public void onComplete(StatsReport[] reports) {
                        processReportStats(reports, statsListener);
                    }
                }, null);

                /*peerConnection.getStats(new RTCStatsCollectorCallback() {
                    @Override
                    public void onStatsDelivered(RTCStatsReport rtcStatsReport) {
                        Map<String, RTCStats> map = rtcStatsReport.getStatsMap();
                        RTCStats rtcStats = map.get("RTCTransport_audio_1");
                        Map<String, Object> members = rtcStats.getMembers();
                        StringeeStream.StringeeAudioStats audioStats = new StringeeStream.StringeeAudioStats();
                        BigInteger bytesSent = (BigInteger) members.get("bytesSent");
                        audioStats.audioBytesSent = bytesSent.intValue();
                        BigInteger bytesReceived = (BigInteger) members.get("bytesReceived");
                        audioStats.audioBytesReceived = bytesReceived.intValue();
                        audioStats.timeStamp = System.currentTimeMillis();
                        statsListener.onAudioStats(audioStats);
                    }
                });*/
            }
        });
    }

    private void processReportStats(StatsReport[] reports, AudioStatsListener statsListener) {
        String audioReceived = "0";
        String videoReceived = "0";
        String audioSend = "0";
        String videoSend = "0";
        for (StatsReport report : reports) {
            if (report.type.equals("ssrc") && report.id.contains("ssrc") && report.id.contains("send")) {// send
                Map<String, String> reportMap = getReportMap(report);
                String audioInputLevel = reportMap.get("audioInputLevel");
                String frameWidthSent = reportMap.get("googFrameWidthSent");
                if (audioInputLevel != null) {
                    audioSend = reportMap.get("bytesSent");
                } else if (frameWidthSent != null) {
                    videoSend = reportMap.get("bytesSent");
                }
            }//rec
            if (report.type.equals("ssrc") && report.id.contains("ssrc") && report.id.contains("recv")) {
                Map<String, String> reportMap = getReportMap(report);
                String audioOutputLevel = reportMap.get("audioOutputLevel");
                String frameWidthReceived = reportMap.get("googFrameWidthReceived");
                if (audioOutputLevel != null) {
                    audioReceived = reportMap.get("bytesReceived");
                } else if (frameWidthReceived != null) {
                    videoReceived = reportMap.get("bytesReceived");
                }
            }
        }
        StringeeStream.StringeeAudioStats audioStats = new StringeeStream.StringeeAudioStats(audioSend, videoSend,
                audioReceived, videoReceived);
        audioStats.timeStamp = System.currentTimeMillis();
        statsListener.onAudioStats(audioStats);
    }

    private Map<String, String> getReportMap(StatsReport report) {
        Map<String, String> reportMap = new HashMap<>();
        for (StatsReport.Value value : report.values) {
            reportMap.put(value.name, value.value);
        }
        return reportMap;
    }

    public void enableVideo(final boolean isEnable) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (localVideoTrack != null) {
                    localVideoTrack.setEnabled(isEnable);
                }
            }
        });
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(mContext);
    }

    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer = null;
        if (useCamera2()) {
            videoCapturer = createCameraCapturer(new Camera2Enumerator(mContext));
        } else {
            videoCapturer = createCameraCapturer(new Camera1Enumerator(true));
        }
        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    public SessionDescription getLocalDescription() {
        return localDescription;
    }

    public void setLocalDescription(SessionDescription localDescription) {
        this.localDescription = localDescription;
    }

    public boolean isSdpSent() {
        return sdpSent;
    }

    public void setSdpSent(boolean sdpSent) {
        this.sdpSent = sdpSent;
    }

    public String getPreferedAudioCodec() {
        return preferedAudioCodec;
    }

    public void setPreferedAudioCodec(String preferedAudioCodec) {
        this.preferedAudioCodec = preferedAudioCodec;
    }

    public String getPreferedVideoCodec() {
        return preferedVideoCodec;
    }

    public void setPreferedVideoCodec(String preferedVideoCodec) {
        this.preferedVideoCodec = preferedVideoCodec;
    }

    public void switchCamera(CameraVideoCapturer.CameraSwitchHandler handler) {
        if (videoCapturer instanceof CameraVideoCapturer) {
            CameraVideoCapturer cameraVideoCapturer = (CameraVideoCapturer) videoCapturer;
            cameraVideoCapturer.switchCamera(handler);
        }
    }

    public void setLocalDescription(final SdpObserver observer, final SessionDescription sessionDescription) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peerConnection.setLocalDescription(observer, sessionDescription);
            }
        });
    }

    public void setRemoteDescription(final SdpObserver observer, final SessionDescription sessionDescription) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peerConnection.setRemoteDescription(observer, sessionDescription);
            }
        });
    }

    public void createAnswer(final SdpObserver observer, final MediaConstraints mediaConstraints) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                peerConnection.createAnswer(observer, mediaConstraints);
            }
        });
    }

    public void renderStream(final StringeeStream stream, boolean zOverlay) {
        if (stream == null) {
            return;
        }
        SurfaceViewRenderer renderer = stream.getSurfaceViewRenderer();
        MediaStream mediaStream = stream.getMediaStream();
        if (mediaStream != null && mediaStream.videoTracks.size() > 0) {
            VideoTrack videoTrack = mediaStream.videoTracks.get(0);
            VideoRenderer videoRenderer = stream.getRenderer();
            if (videoRenderer != null) {
                videoTrack.removeRenderer(videoRenderer);
                renderer.release();
            }
            if (!stream.isLocal()) {
                renderer.setMirror(false);
                renderer.init(remoteContext, null);
            } else {
                renderer.init(localContext, null);
                renderer.setMirror(true);
            }
            renderer.setZOrderMediaOverlay(zOverlay);
            VideoRenderer mRenderer = new VideoRenderer(renderer);
            mediaStream.videoTracks.get(0).addRenderer(mRenderer);
            stream.setRenderer(mRenderer);
        }
    }

    public boolean isCameraAvailable(Context context) {
        PackageManager pm = context.getPackageManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                return true;
            }
        } else {
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                return true;
            }
        }
        return false;
    }
}