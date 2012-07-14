/**
 * 
 */

package com.renren.sns.webtalk.cluster;

import com.renren.sns.webtalk.models.StreamingServerPair;


/**
 * 服务器集群管理
 * @author jinchao.wen
 * @email  jinchao.wen@opi-corp.com
 * @date   2011-4-12
 */
public interface IClusterManager {
    
    
   final long TIME_OUT = 1000 * 30;
   
   /** 
    * 集群中更新red5服务器
    * @param servername
    */
   public void updateRed5Server(String servername);
   
   /**
    * 集群中删除red5服务器
    * @param servername
    */
   public void delRed5Server(String servername);
   
   /** 
    * 集群中更新red5服务器
    * @param servername
    */
   public void updateCumulusServer(String servername);
   
   /**
    * 集群中删除red5服务器
    * @param servername
    */
   public void delCumulusServer(String servername);
   
    /**
     * 根据modId获取流媒体服务器列表
     * @param modId
     * @return
     */
   public  StreamingServerPair getServerPair( int modId);
   
   /**
    * 检查集群中服务器状态更新时间，如过已经超出更新时限，报警(暂不删除)
    */
   public void checkServerStatus();
}
