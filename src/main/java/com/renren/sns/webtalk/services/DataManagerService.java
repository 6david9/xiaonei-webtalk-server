/*package com.renren.sns.webtalk.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renren.sns.webtalk.dao.FacetimeRecorderDAO;
import com.renren.sns.webtalk.data.Cache;
import com.renren.sns.webtalk.data.MemCachedCache;
import com.renren.sns.webtalk.models.FacetimeRecorder;
import com.renren.sns.webtalk.util.InvokeUtil;
import com.renren.sns.webtalk.util.TalkLogUtil;

*//**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 *//*
@Service
public class DataManagerService implements DataManager, InitializingBean {

	@Autowired
	protected FacetimeRecorderDAO facetimeRecorderDAO;

	private static List<Cache> cachechain = null;

	public void save() {
		System.out.println("save save save save");

	}

	public int insert(int hostid, FacetimeRecorder recorder) {
		doSaveCacheChain(hostid, recorder);
		facetimeRecorderDAO.insertRecorder(hostid, "caller_peer_id_"
				+ recorder.getCaller_id(), recorder.getReciever_id(),
				"reciever_peer_id_" + recorder.getReciever_id(), new Date());
		return 0;
	}

	public List<FacetimeRecorder> selectById(int hostid) {
		List<FacetimeRecorder> ret = null;
		ret = doLoadCacheChain(hostid, hostid);
		facetimeRecorderDAO.selectById(hostid);
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T> T doLoadCacheChain(int hostid, Object... args) {
		T ret = null;
		String method = InvokeUtil.getInvokingMethodName();
		if(cachechain == null){
			TalkLogUtil.debug(" host:" + hostid + " cachechain:"+cachechain);
			return ret;
		}
		for (Cache cache : cachechain) {
			try {
				Class<?>[] clss = null;
				if (args != null && args.length >= 1) {
					clss = new Class<?>[args.length];
					for (int i = 0; i < clss.length; i++) {
						clss[i] = args[i].getClass();
						if (clss[i] == Integer.class) {
							clss[i] = int.class;
						}
					}
				}
				Method m = cache.getClass().getMethod(method, clss);
				m.invoke(cache, args);
				T a = (T) m.invoke(cache, args);
				//Object invret = null;
				T a = null;
				if(invret != null){
					a = (T)invret;
				}
				if (a != null) {
					ret = a;
					TalkLogUtil.debug(" host:" + hostid + " hit:"
							+ cache.getClass().getSimpleName() + " a:" + a);
					break;
				} else {
					TalkLogUtil.debug(" host:" + hostid + " miss:"
							+ cache.getClass().getSimpleName() + " a:" + a);
				}

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private <T> T doSaveCacheChain(int hostid,Object... args) {
		T ret = null;
		String method = InvokeUtil.getInvokingMethodName();
		if(cachechain == null){
			TalkLogUtil.debug(" host:" + hostid + " cachechain:"+cachechain);
			return ret;
		}
		for (Cache cache : cachechain) {
			try {
				Class<?>[] clss = null;
				if (args != null && args.length >= 1) {
					clss = new Class<?>[args.length];
					for (int i = 0; i < clss.length; i++) {
						clss[i] = args[i].getClass();
						if (clss[i] == Integer.class) {
							clss[i] = int.class;
						}
					}
				}
				Method m = cache.getClass().getMethod(method, clss);
				T a = (T) m.invoke(cache, args);
				ret = a;

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return ret;
	}

	public static void main(String[] args) {
		DataManagerService dms = new DataManagerService();
		FacetimeRecorder recorder = new FacetimeRecorder();
		recorder.setCaller_id(1);
		//dms.insert(81120, recorder);
		dms.test();
		dms.getStatus(81120, 12345);
		List<Integer> list = new ArrayList<Integer>();
		list.add(1111);
		list.add(1111);
		list.add(1111);
		System.out.println(list);
	}

	public String getMethodName() {
		String ret = InvokeUtil.getInvokingMethodName();
		return ret;
	}

	@Override
	public void test() {
		doSaveCacheChain(81120);
		System.out.println("DataManagerService::test()");
	}

	@Override
	public Integer getStatus(int hostid, int bid) {
		TalkLogUtil.debug(" host:"+hostid+" bid:"+bid);
		//Integer ret = doLoadCacheChain(hostid, hostid, bid);
		Integer ret = FacetimeRecorder.STATE_USER_BUSY;
		FacetimeRecorder recordC = MemCachedCache.getInstance().loadRecordC(hostid, bid);
		if(recordC == null){
			List<Integer> listR = MemCachedCache.getInstance().loadListR(hostid, bid);
			if(listR == null){
				//free
				ret = FacetimeRecorder.STATE_USER_FREE;
			}
			else{
				if(listR != null && listR.size() <= 2){
					//warn
					
				}
				else{
					int index0 = listR.get(0);
					if(index0 == -1){
						//free
						ret = FacetimeRecorder.STATE_USER_FREE;
					}
					else{
						//busy
						ret = FacetimeRecorder.STATE_USER_BUSY;
					}
				}
			}
		}
		else{
			//busy
			ret = FacetimeRecorder.STATE_USER_BUSY;
		}
		
		if(ret == null)ret = 0;
		TalkLogUtil.debug(" host:"+hostid+" bid:"+bid+" ret:"+ret);
		return ret;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		TalkLogUtil.debug(" afterPropertiesSet() - start");
		cachechain = new ArrayList<Cache>();
		cachechain.add(MemCachedCache.getInstance());
		TalkLogUtil.debug(" afterPropertiesSet() - end");
	}

	*//**
	 * mem_c 写 ；mem_r 写；mem_stat 改caller；
	 * @param hostid
	 * @param bid
	 * @return
	 *//*
	public Integer setConnectedOk(int hostid, int bid) {
		
		TalkLogUtil.debug(" host:"+hostid+" bid:"+bid);
		Integer ret = doLoadCacheChain(hostid, hostid, bid);
		
		//
		if(ret == null)ret = 0;
		FacetimeRecorder recorder = new FacetimeRecorder();
		recorder.setCaller_id(hostid);
		recorder.setCaller_peer_id("");
		recorder.setCaller_time(new Date());
		recorder.setReciever_id(bid);
		recorder.setReciever_peer_id("");
		recorder.setState(FacetimeRecorder.STATE_TALK_CALLING); //正在呼叫
		MemCachedCache.getInstance().saveRecordC(hostid, hostid, recorder);
		
		//
		List<Integer> caller_ids = MemCachedCache.getInstance().loadListR(hostid, bid);
		if(caller_ids == null){
			caller_ids = new ArrayList<Integer>();
		}
		if(caller_ids.size() >= 1){
			if(caller_ids.get(0) == -1){
				caller_ids.add(bid);
				
			}
			else{
				
				TalkLogUtil.warn(" host:"+hostid+" bid:"+bid+" type:"+caller_ids.get(0));
			}
		}
		MemCachedCache.getInstance().saveListR(hostid, bid, caller_ids);
		
		
		TalkLogUtil.debug(" host:"+hostid+" bid:"+bid+" ret:"+ret);
		return 0;
	}
}
*/