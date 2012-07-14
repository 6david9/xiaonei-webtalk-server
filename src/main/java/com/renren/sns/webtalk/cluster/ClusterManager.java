
package com.renren.sns.webtalk.cluster;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.renren.sns.webtalk.models.StreamingServerPair;
import com.renren.sns.webtalk.util.SMSUtil;
import com.renren.sns.webtalk.util.TalkLogUtil;

/**
 * @author jinchao.wen
 * @email jinchao.wen@opi-corp.com
 * @date 2011-4-12
 */
@Service
public class ClusterManager implements IClusterManager {

    //private static final String RTMFP_URL = "rtmfp://webtalk1.renren.com/";
    //private static final String RTMFP_URL = "rtmfp://123.125.44.87/";

    //private static final String RTMP_URL = "rtmp://webtalk2.renren.com/live";
    //private static final String RTMP_URL = "rtmp://123.125.38.134/live";

    //key:服务器url；value：更新时间

    private Map<String, Long> red5Map = new ConcurrentHashMap<String, Long>();

    
    public Map<String, Long> getRed5Map() {
        return red5Map;
    }
    
    public void setRed5Map(Map<String, Long> red5Map) {
        this.red5Map = red5Map;
    }
    
    public Map<String, Long> getCumulusMap() {
        return cumulusMap;
    }
    
    public void setCumulusMap(Map<String, Long> cumulusMap) {
        this.cumulusMap = cumulusMap;
    }

    private Map<String, Long> cumulusMap = new ConcurrentHashMap<String, Long>();

    private Random rand = new Random();

    public ClusterManager() {
		this.updateRed5Server("rtmp://webtalk1.renren.com/live");
		this.updateRed5Server("rtmp://webtalk2.renren.com/live");
		this.updateCumulusServer("rtmfp://webtalk1.renren.com");
		this.updateCumulusServer("rtmfp://webtalk2.renren.com");
    }

    @Override
    public void updateCumulusServer(String cumulus) {
//        TalkLogUtil.debug(cumulus);
        cumulusMap.put(cumulus, System.currentTimeMillis());
    }

    @Override
    public void updateRed5Server(String red5) {
//        TalkLogUtil.debug(red5);
        red5Map.put(red5, System.currentTimeMillis());
    }

    @Override
    public void delCumulusServer(String cumulus) {
        //TalkLogUtil.debug( cumulus);
        if (cumulusMap.containsKey(cumulus)) {
            cumulusMap.remove(cumulus);
        }
    }

    @Override
    public void delRed5Server(String red5) {
        //TalkLogUtil.debug(red5);
        if (red5Map.containsKey(red5)) {
            red5Map.remove(red5);
        }
    }

    @Override
    public StreamingServerPair getServerPair(int modId) {
        //TalkLogUtil.debug("getServerPair:" + modId);

        StreamingServerPair serverPair = new StreamingServerPair();
        serverPair.setModId(modId);
        //serverPair.setRtmfpUrl(RTMFP_URL);
        //serverPair.setRtmpUrl(RTMP_URL);

        if (red5Map.size() > 0) {
            int randIndex = rand.nextInt(red5Map.size());
            int count = 0;
            Set<Map.Entry<String, Long>> entrySet = red5Map.entrySet();
            for (Iterator<Map.Entry<String, Long>> iter = entrySet.iterator(); iter.hasNext(); count++) {
                if (randIndex == count) {
                    Map.Entry<String, Long> entry = iter.next();
                    serverPair.setRtmpUrl(entry.getKey());
                } else {
                    iter.next();
                }
            }
        } else {
            TalkLogUtil.error( "webserver:no_red5_server_found");
            SMSUtil.sendMessage("15801208687", "webserver:no_red5_server_found");
            SMSUtil.sendMessage("15110056634", "webserver:no_red5_server_found");
        }

        if (cumulusMap.size() > 0) {
            int randIndex = rand.nextInt(cumulusMap.size());
            int count = 0;
            Set<Map.Entry<String, Long>> entrySet = cumulusMap.entrySet();
            for (Iterator<Map.Entry<String, Long>> iter = entrySet.iterator(); iter.hasNext(); count++) {
                if (randIndex == count) {
                    Map.Entry<String, Long> entry = iter.next();
                    serverPair.setRtmfpUrl(entry.getKey());
                } else {
                    iter.next();
                }
            }
        } else {
            TalkLogUtil.error( "webserver:no_Cumulus_server_found");
            SMSUtil.sendMessage("15801208687", "webserver:no_Cumulus_server_found");
            SMSUtil.sendMessage("15110056634", "webserver:no_Cumulus_server_found");
        }

        //TalkLogUtil.debug(" return " + serverPair);

        return serverPair;
    }

    @Override
    public void checkServerStatus() {
        long current = System.currentTimeMillis();

        Set<Map.Entry<String, Long>> entrySet = red5Map.entrySet();
        if (entrySet.size() <= 0) {
            TalkLogUtil.error( "webserver:no_red5_server_found");
            SMSUtil.sendMessage("15801208687", "webserver:no_red5_server_found");
            SMSUtil.sendMessage("15110056634", "webserver:no_red5_server_found");
        } else {
            for (Iterator<Map.Entry<String, Long>> iter = entrySet.iterator(); iter.hasNext();) {
                Map.Entry<String, Long> entry = iter.next();
                if (current - entry.getValue() > TIME_OUT) {
                    //red5Map.remove(entry.getKey());
//                    TalkLogUtil.error( "webserver:red5_servers_"+entry.getKey()+"_noping_"+(current - entry.getValue())/1000+"ms");
                }
            }
        }

        entrySet = cumulusMap.entrySet();
        if (entrySet.size() <= 0) {
            TalkLogUtil.error( "webserver:no_Cumulus_server_found");
            SMSUtil.sendMessage("15801208687", "webserver:no_Cumulus_server_found");
            SMSUtil.sendMessage("15110056634", "webserver:no_Cumulus_server_found");
        } else {
            for (Iterator<Map.Entry<String, Long>> iter = entrySet.iterator(); iter.hasNext();) {
                Map.Entry<String, Long> entry = iter.next();
                if (current - entry.getValue() > TIME_OUT) {
                    //cumulusMap.remove(entry.getKey());
//                    TalkLogUtil.error( "webserver:Cumulus_servers_"+entry.getKey()+"_noping_"+(current - entry.getValue())/1000+"ms");
                }
            }
        }
    }

    public static void main(String[] args) {
        long long1 = System.currentTimeMillis();

        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long long2 = System.currentTimeMillis();

        System.out.println(long1);
        System.out.println(long2);
        System.out.println(long2 - long1);

        /*Random rand = new Random();
        while (true) {
            System.out.println(rand.nextInt(10));
        }*/
                
    }

}
