/**
 * 
 */

package com.renren.sns.webtalk.models;

/**
 * POJO文件，用来binding到xml
 * 
 * @author jinchao.wen
 * @email jinchao.wen@opi-corp.com
 * @date 2011-3-14
 */
public class UserInfo {

    private int userid;

    private String username;

    private String peer;

    private String tinyurl;

    private String mainurl;

    private int rtmfpok;

    private int rtmpok;

    private String rtmfpurl;

    private String rtmpurl;

    private String status;
    
    private Boolean permission;

	public UserInfo(int userId) {
        this.userid = userId;
    }
    
	public int getRtmfpok() {
        return rtmfpok;
    }

    public void setRtmfpok(int rtmfpok) {
        this.rtmfpok = rtmfpok;
    }

    public int getRtmpok() {
        return rtmpok;
    }

    public void setRtmpok(int rtmpok) {
        this.rtmpok = rtmpok;
    }

    public String getRtmfpUrl() {
        return rtmfpurl;
    }

    public void setRtmfpUrl(String rtmfpUrl) {
        this.rtmfpurl = rtmfpUrl;
    }

    public String getRtmpUrl() {
        return rtmpurl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpurl = rtmpUrl;
    }

    public int getUserId() {
        return userid;
    }

    public String getPeerId() {
        return peer;
    }

    public void setPeerId(String peerId) {
        this.peer = peerId;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getTinyurl() {
        return tinyurl;
    }

    public void setTinyurl(String tinyurl) {
        this.tinyurl = tinyurl;
    }

    public String getMainurl() {
        return mainurl;
    }

    public void setMainurl(String mainurl) {
        this.mainurl = mainurl;
    }

    public int getRtmfpOk() {
        return rtmfpok;
    }

    public void setRtmfpOk(int rtmfpOk) {
        this.rtmfpok = rtmfpOk;
    }

    public int getRtmpOk() {
        return rtmpok;
    }

    public void setRtmpOk(int rtmpOk) {
        this.rtmpok = rtmpOk;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public Boolean getPermission() {
		return permission;
	}

	public void setPermission(Boolean permission) {
		this.permission = permission;
	}
	
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("<userid>").append(this.userid).append("</userid>")
			.append("<username>").append(this.username).append("</username>")
			.append("<peer>").append(this.peer).append("</peer>")
			.append("<tinyurl>").append(this.tinyurl).append("</tinyurl>")
			.append("<mainurl>").append(this.mainurl).append("</mainurl>")
			.append("<rtmfpok>").append(this.rtmfpok).append("</rtmfpok>")
			.append("<rtmpok>").append(this.rtmpok).append("</rtmpok>")
			.append("<rtmfpurl>").append(this.rtmfpurl).append("</rtmfpurl>")
			.append("<rtmpurl>").append(this.rtmpurl).append("</rtmpurl>")
			.append("<status>").append(this.status).append("</status>")
			.append("<permission>").append(this.permission).append("</permission>");
		return res.toString();
	}

}
