package com.renren.sns.webtalk.models;

import java.io.Serializable;
import java.util.Date;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public class FacetimeRecorder implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int STATE_R_FREE = -1;
	public static final int STATE_R_TALKING = 1;
	public static final String STATE_USER_FREE = "FREE";
	public static final String STATE_USER_CALLING = "CALLING";
	public static final String STATE_USER_TALKING = "TALKING";

	public static final int STATE_TALK_CALLING = 0; // 目前正在呼叫别人,用在memcache中
	public static final int STATE_TALK_TALKING = 1; // 目前正在通话中,用在memcache中

	public static final int STATE_TALK_CALL_FAIL = 2; // 呼叫方建立连接失败,入库
	public static final int STATE_TALK_CALL_CANCEL = 3; // 呼叫方取消呼叫(主动),入库
	public static final int STATE_TALK_RECEIVER_FAIL = 4; // 被叫方建立连接失败,入库
	public static final int STATE_TALK_RECEIVER_REJECT = 5; // 被叫方拒绝接听,入库
	public static final int STATE_TALK_HANGUP = 6; // 任何一方挂断通话,入库
	public static final int STATE_TALK_CALL_TIMEOUT = 7; // 呼叫方呼叫超时,入库

	public static final int TALK_TYPE_RTMFP = 0;
	public static final int TALK_TYPE_RTMP = 1;

	int id;
	int caller_id;
	boolean caller_rtmfp_ok;
	boolean caller_rtmp_ok;
	String caller_peer_id;
	int reciever_id;
	boolean receiver_rtmfp_ok;
	boolean receiver_rtmp_ok;
	String receiver_peer_id;

	int caller_rtmfp_stage;
	int caller_rtmp_stage;
	int receiver_rtmfp_stage;
	int receiver_rtmp_stage;

	int state;
	Date caller_time;
	Date reciever_time;
	Date end_time;

	String rtmfpUrl;
	String rtmpUrl;

	int talk_stage;

	public int getCaller_rtmfp_stage() {
		return caller_rtmfp_stage;
	}

	public void setCaller_rtmfp_stage(int callerRtmfpStage) {
		caller_rtmfp_stage = callerRtmfpStage;
	}

	public int getCaller_rtmp_stage() {
		return caller_rtmp_stage;
	}

	public void setCaller_rtmp_stage(int callerRtmpStage) {
		caller_rtmp_stage = callerRtmpStage;
	}

	public int getTalk_stage() {
		return talk_stage;
	}

	public void setTalk_stage(int talkStage) {
		talk_stage = talkStage;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCaller_id() {
		return caller_id;
	}

	public void setCaller_id(int caller_id) {
		this.caller_id = caller_id;
	}

	public String getCaller_peer_id() {
		return caller_peer_id;
	}

	public void setCaller_peer_id(String caller_peer_id) {
		this.caller_peer_id = caller_peer_id;
	}

	public int getReciever_id() {
		return reciever_id;
	}

	public void setReciever_id(int reciever_id) {
		this.reciever_id = reciever_id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCaller_time() {
		return caller_time;
	}

	public void setCaller_time(Date caller_time) {
		this.caller_time = caller_time;
	}

	public Date getReciever_time() {
		return reciever_time;
	}

	public void setReciever_time(Date reciever_time) {
		this.reciever_time = reciever_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public boolean isCaller_rtmfp_ok() {
		return caller_rtmfp_ok;
	}

	public void setCaller_rtmfp_ok(boolean callerRtmfpOk) {
		caller_rtmfp_ok = callerRtmfpOk;
	}

	public boolean isCaller_rtmp_ok() {
		return caller_rtmp_ok;
	}

	public void setCaller_rtmp_ok(boolean callerRtmpOk) {
		caller_rtmp_ok = callerRtmpOk;
	}

	public String getRtmfpUrl() {
		return rtmfpUrl;
	}

	public void setRtmfpUrl(String rtmfpUrl) {
		this.rtmfpUrl = rtmfpUrl;
	}

	public String getRtmpUrl() {
		return rtmpUrl;
	}

	public void setRtmpUrl(String rtmpUrl) {
		this.rtmpUrl = rtmpUrl;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = " ";
		StringBuffer retValue = new StringBuffer();
		retValue.append("FacetimeRecorder ( ").append(super.toString()).append(
				TAB).append("id = ").append(this.id).append(TAB).append(
				"caller_id = ").append(this.caller_id).append(TAB).append(
				"caller_rtmfp_ok = ").append(this.caller_rtmfp_ok).append(TAB)
				.append("caller_rtmp_ok = ").append(this.caller_rtmp_ok)
				.append(TAB).append("caller_peer_id = ").append(
						this.caller_peer_id).append(TAB).append(
						"reciever_id = ").append(this.reciever_id).append(TAB)
				.append("reciever_rtmfp_ok = ").append(this.receiver_rtmfp_ok)
				.append(TAB).append("reciever_rtmp_ok = ").append(
						this.receiver_rtmp_ok).append(TAB)

				.append("stage = ").append(this.caller_rtmfp_stage).append(
						this.caller_rtmp_stage).append(
						this.receiver_rtmfp_stage).append(
						this.receiver_rtmp_stage).append(TAB)

				.append("reciever_peer_id = ").append(this.receiver_peer_id)
				.append(TAB).append("talk_stage = ").append(this.talk_stage)
				.append(TAB).append("state = ").append(this.state).append(TAB)
				.append("caller_time = ").append(this.caller_time).append(TAB)
				.append("reciever_time = ").append(this.reciever_time).append(
						TAB).append("end_time = ").append(this.end_time)
				.append(TAB).append(" )");
		return retValue.toString();
	}

	public boolean isReceiver_rtmfp_ok() {
		return receiver_rtmfp_ok;
	}

	public void setReceiver_rtmfp_ok(boolean receiverRtmfpOk) {
		receiver_rtmfp_ok = receiverRtmfpOk;
	}

	public boolean isReceiver_rtmp_ok() {
		return receiver_rtmp_ok;
	}

	public void setReceiver_rtmp_ok(boolean receiverRtmpOk) {
		receiver_rtmp_ok = receiverRtmpOk;
	}

	public String getReceiver_peer_id() {
		return receiver_peer_id;
	}

	public void setReceiver_peer_id(String receiverPeerId) {
		receiver_peer_id = receiverPeerId;
	}

	public int getReceiver_rtmfp_stage() {
		return receiver_rtmfp_stage;
	}

	public void setReceiver_rtmfp_stage(int receiverRtmfpStage) {
		receiver_rtmfp_stage = receiverRtmfpStage;
	}

	public int getReceiver_rtmp_stage() {
		return receiver_rtmp_stage;
	}

	public void setReceiver_rtmp_stage(int receiverRtmpStage) {
		receiver_rtmp_stage = receiverRtmpStage;
	}

}
