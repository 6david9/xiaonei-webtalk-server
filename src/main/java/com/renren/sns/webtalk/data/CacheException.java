/**
 * 
 */

package com.renren.sns.webtalk.data;


/**
 * 修改MemCached时所发生的异常
 * 
 * @author jinchao.wen
 * @email  jinchao.wen@opi-corp.com
 * @date   2011-3-14
 */
public class CacheException extends Exception {

    private static final long serialVersionUID = -4693042808340902807L;
    
    private Exception e;
    private String info;

    public CacheException (String info){
        this.info = info;
    }

    public CacheException (Exception e){
        this.e = e;
    }

    public CacheException(Exception e, String info){
        this.e = e;
        this.info = info;
    }

    
    public Exception getE() {
        return e;
    }

    
    public void setE(Exception e) {
        this.e = e;
    }

    
    public String getInfo() {
        return info;
    }

    
    public void setInfo(String info) {
        this.info = info;
    }
}
