package com.renren.sns.webtalk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author jinchao.wen
 * @email jinchao.wen@opi-corp.com
 * @date 2011-4-25
 */
public class SMSUtil {

    private static final String HTTP_URL = "http://10.22.198.81:2000/receiver";

    //private static final String HTTP_URL = "http://10.22.198.81:2000/receiver?number=13691384667&message=test";    
    
    
    public synchronized static void sendMessage(String phoneNumber, String content) {

        TalkLogUtil.debug("发送报警短信: " + phoneNumber +"->"+ content);

        sendGet(HTTP_URL, "number=" + phoneNumber + "&message=" + content);

    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    private static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            
            List<String> status = (List<String>)map.get(null);
           for(Iterator<String> iter = status.iterator() ; iter.hasNext(); ){
               TalkLogUtil.error("发送报警短信: " + iter.next());
           }
            /*for (String key : map.keySet()) {
                System.out.println(key + "---&gt;" + map.get(key));
            }
           // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }*/
        } catch (Exception e) {
            TalkLogUtil.error("发送报警短信异常: " + e.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public static void main(String[] args){
        SMSUtil.sendMessage("13691384667", "red5_ping_timeout_please_check_" + "rtmp://webtalk1.renren.com/live");
    }
}
