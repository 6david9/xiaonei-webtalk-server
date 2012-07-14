package com.renren.sns.webtalk.services;

import java.util.List;

import com.renren.sns.webtalk.data.CacheException;
import com.renren.sns.webtalk.models.FacetimeRecorder;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-4
 */
public interface DataManager {
	public int insert(int hostid,FacetimeRecorder recorder) throws CacheException;
	public List<FacetimeRecorder> selectById(int hostid) throws CacheException;
	public void test() throws CacheException;
	public Integer getStatus(int hostid, int keyid)throws CacheException;
	
}
