package com.renren.sns.webtalk.data;

import java.util.ArrayList;
import java.util.List;

import com.renren.sns.webtalk.models.FacetimeRecorder;
import com.renren.sns.webtalk.util.TalkLogUtil;
import com.renren.xcache.XCache;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public class MemCachedCache implements Cache {
	
	private static final MemCachedCache instance = new MemCachedCache();
	MemCachedCache(){}
	public static MemCachedCache getInstance(){return instance;}

	private static final String MEM_CACHE_C_KEYPREFIX = "_C_";
	private static final String MEM_CACHE_R_KEYPREFIX = "_R_";
	private static final String MEM_CACHE_S_KEYPREFIX = "_S_";

	private static final XCache<FacetimeRecorder> fcCache = XCache.getCache("facetime.caller", FacetimeRecorder.class);
	@SuppressWarnings("unchecked")
	private static final XCache<List> frCache = XCache.getCache("facetime.reciever", List.class);
	private static final XCache<Integer> fsCache = XCache.getCache("facetime.status", Integer.class);

	private static final int EXP_TIME = XCache.EXPIRE_DAY * 1;


	public void saveIntegerS(int host, int keyid, Integer status) throws CacheException{
		if (host == 0 || keyid == 0 || status == null ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" status:"+status);
			return;
		}
		try {
			String realkey = MEM_CACHE_S_KEYPREFIX + keyid;
			fsCache.set(realkey, status, EXP_TIME);
			//TalkLogUtil.debug(" host:" + host + "keyid:" + keyid + " saved_realkey:" + realkey+" v:"+status);
		} catch (Exception e) {
			throw new CacheException(e, "saveIntegerS: " +" host:" + host + " keyid:" + keyid+" status:"+status);
		}
	}

	public Integer loadIntegerS(int host, int keyid)  throws CacheException{
		Integer ret = null;
		if (host == 0 || keyid == 0 ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid);
			return ret;
		}
		try {
			String realkey = MEM_CACHE_S_KEYPREFIX + keyid;
			ret = fsCache.get(realkey);
			//TalkLogUtil.debug(" host:" + host + " keyid:" + keyid + " get_ret:" + ret);
		} catch (Exception e) {
			throw new CacheException(e, "saveIntegerS: " +"  host:" + host + " keyid:" + keyid);
		}
		return ret;
	}
	
	public void removeIntegerS(int host, int keyid, Integer status) throws CacheException{
		if (host == 0 || keyid == 0 || status == null ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" status:"+status);
			return;
		}
		try {
			String realkey = MEM_CACHE_S_KEYPREFIX + keyid;
			fsCache.delete(realkey);
			//TalkLogUtil.debug(" host:" + host +" userid:" + keyid + " deleted realkey:" + realkey);
		} catch (Exception e) {
		    throw new CacheException(e, "removeIntegerS: " +"  host:" + host + " keyid:" + keyid);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> loadListR(int host, int keyid) throws CacheException{
		List<Integer> ret = null;
		if (host == 0 || keyid == 0  ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid);
			return ret;
		}
		
		try {
			String realkey = MEM_CACHE_R_KEYPREFIX + keyid;
			ret = frCache.get(realkey);
			//TalkLogUtil.debug(" host:" + keyid + " get_ret:" + ret );
		} catch (Exception e) {
		    throw new CacheException(e, "loadListR: " +"  host:" + host + " keyid:" + keyid);
		}
		return ret;
	}
	
	public boolean removeListR(int host,int keyid) throws CacheException{
	    boolean ret = false;
		if (host == 0 || keyid == 0 ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid);
			return ret;
		}
		try {
			String realkey = MEM_CACHE_R_KEYPREFIX + keyid;
			ret = frCache.delete(realkey);
			//TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " del_realkey:" + realkey +" get_ret:" + ret );
		} catch (Exception e) {
		    throw new CacheException(e, "removeListR: " +"  host:" + host + " keyid:" + keyid);
		}
		return ret;
	}

	public boolean saveListR(int host, int keyid, List<Integer> caller_ids, int timeout) throws CacheException{
	    boolean ret = false;
		if (host == 0 || keyid == 0 || caller_ids == null ){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" caller_ids:"+caller_ids);
			return ret;
		}

		try {
			String realkey = MEM_CACHE_R_KEYPREFIX + keyid;
			ret = frCache.set(realkey, caller_ids, timeout);
			//TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " saved_realkey:" + realkey+" v:"+caller_ids +" get_ret:" + ret );
		} catch (Exception e) {
		    throw new CacheException(e, "saveListR: " +"  host:" + host + " keyid:" + keyid);
		}	
		return ret;
	}
	
	public boolean saveOrUpdatateListR(int host, int keyid, int callerid) throws CacheException{
	    boolean ret = false;
	    if (host == 0 || keyid == 0 || callerid == 0 ){
            TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" callerid:"+ callerid);
            return ret;
        }

        try {
            String realkey = MEM_CACHE_R_KEYPREFIX + keyid;
            List<Integer> caller_ids = getInstance().loadListR(host, keyid);
            if( null == caller_ids ){
                caller_ids = new ArrayList<Integer>();
            }
            caller_ids.add(callerid);
            ret = frCache.set(realkey, caller_ids, EXP_TIME);
            //TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " saved_realkey:" + realkey+" v:"+caller_ids +" get_ret:" + ret );
        } catch (Exception e) {
            throw new CacheException(e, "saveOrUpdatateListR: " +"  host:" + host + " keyid:" + keyid);
        }
        return ret;
    }

	public boolean saveRecordC(int host, int keyid, FacetimeRecorder recorder) throws CacheException{
	    boolean ret = false;
	    if (host == 0 || keyid == 0 || recorder == null ){
	        TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" recorder:"+recorder );
	        return ret;
	    }

	    try {
	        String realkey = MEM_CACHE_C_KEYPREFIX + keyid;
	        ret = fcCache.set(realkey, recorder, EXP_TIME);
	        //TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " saved_realkey:" + realkey+" v:"+recorder +" get_ret:" + ret );
	    } catch (Exception e) {
	        throw new CacheException(e, "saveRecordC: " +"  host:" + host + " keyid:" + keyid);
	    }
	    return ret;
	}
	
	public boolean saveRecordC(int host, int keyid, FacetimeRecorder recorder, int timeout) throws CacheException{
	    boolean ret = false;
	    if (host == 0 || keyid == 0 || recorder == null ){
            TalkLogUtil.warn(" host:" + host + " keyid:" + keyid+" recorder:"+recorder + " timeout:" + timeout);
            return ret;
        }

        try {
            String realkey = MEM_CACHE_C_KEYPREFIX + keyid;
            if( timeout <= 0){
                timeout = EXP_TIME;
            }
            ret = fcCache.set(realkey, recorder, timeout);
            //TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " saved_realkey:" + realkey+" v:"+recorder +" get_ret:" + ret );
        } catch (Exception e) {
            throw new CacheException(e, "saveRecordC: " +"  host:" + host + " keyid:" + keyid);
        }
        return ret;
    }
	
	public FacetimeRecorder loadRecordC(int host, int keyid) throws CacheException{
		FacetimeRecorder ret = null;
		if (host == 0 || keyid == 0){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid);
			return ret;
		}

		try {
			String realkey = MEM_CACHE_C_KEYPREFIX + keyid;
			ret = fcCache.get(realkey);
			//TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " ret get:" + ret);
		} catch (Exception e) {
		    throw new CacheException(e, "loadRecordC: " +"  host:" + host + " keyid:" + keyid);
		}
		return ret;
	}

	public boolean removeRecordC(int host, int keyid) throws CacheException{
	    boolean ret = false;
	    if (host == 0 || keyid == 0){
			TalkLogUtil.warn(" host:" + host + " keyid:" + keyid);
			return ret;
		}
		try {
			String realkey = MEM_CACHE_C_KEYPREFIX + keyid;
			ret = fcCache.delete(realkey);
			//TalkLogUtil.debug(" host:" +host+" keyid:" + keyid + " deleted realkey:" + realkey +" get_ret:" + ret );
		} catch (Exception e) {
		    throw new CacheException(e, "removeRecordC: " +"  host:" + host + " keyid:" + keyid);
		}
		return ret;
	}

	@Override
	public int insert(int hostid, FacetimeRecorder recorder) throws CacheException{
		saveRecordC(hostid, hostid, recorder, 0);
		return 0;
	}

	@Override
	public void test() throws CacheException{
		System.out.println("MemCachedCache::test()");
		if (false) {
			saveListR(0, 0, null, 10);
			loadListR(0, 0);
			loadIntegerS(0, 0);
			removeIntegerS(0, 0, null);
			saveIntegerS(0, 0, null);
			System.out.println(MEM_CACHE_R_KEYPREFIX + MEM_CACHE_S_KEYPREFIX);
		}

	}

	@Override
	public List<FacetimeRecorder> selectById(int hostid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getStatus(int hostid, int keyid) throws CacheException{
		Integer ret = loadIntegerS(hostid, keyid);
		return ret;
	}


}