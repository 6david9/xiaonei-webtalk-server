/**
 * 
 */

package com.renren.sns.webtalk.models;

/**
 * @author jinchao.wen
 * @email jinchao.wen@opi-corp.com
 * @date 2011-4-12
 */
public class StreamingServerPair {

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

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("modId: ").append(modId).append("; rtmfpUrl: ").append(rtmfpUrl).append(
                "; rtmpUrl ").append(rtmpUrl);
        return sb.toString();
    }

    private int modId;

    private String rtmfpUrl;

    private String rtmpUrl;
}
