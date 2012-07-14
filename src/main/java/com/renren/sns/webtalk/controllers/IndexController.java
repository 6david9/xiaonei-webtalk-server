package com.renren.sns.webtalk.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;

import com.renren.sns.webtalk.cluster.IClusterManager;
import com.renren.sns.webtalk.models.StreamingServerPair;
import com.renren.sns.webtalk.util.TalkLogUtil;
import com.xiaonei.commons.interceptors.access.HostHolder;
import com.xiaonei.commons.interceptors.access.loginrequired.LoginRequired;

@Path("")
public class IndexController {

	@Autowired
	private HostHolder host;

	@Autowired
	protected IClusterManager clusterManager;

	private static final String PAGE_CLIENT = "webtalk/client/webtalk-client.jsp";

	private static final String PAGE_DEV = "webtalk/client/webtalk-dev.jsp";

	/**
	 * 主入口
	 * 
	 * @param inv
	 * @param uid
	 * @return
	 */
	@LoginRequired
	@Get("/webtalk")
	public String index(Invocation inv) {
		int uid = -1;
		String uidstr = inv.getParameter("uid");
		if (uidstr != null) {
			uid = Integer.parseInt(uidstr);
		}
		// if( inv.getRequest().getMethod().equalsIgnoreCase("POST") ){
		// }
		int hostid = host.getUser().getId();
		TalkLogUtil.info(" hostid:" + hostid + " uid:" + uid + " "
				+ inv.toString());

		StreamingServerPair serverPair = clusterManager.getServerPair(hostid);

		String ret = PAGE_CLIENT;

		inv.addModel("local_id", hostid);
		inv.addModel("local_name", host.getUser().getName());
		inv.addModel("local_tiny_url", host.getUser().getTinyFullUrl());
		inv.addModel("local_main_url", host.getUser().getMainFullUrl());
		inv.addModel("local_rtmfp_url", serverPair.getRtmfpUrl());
		inv.addModel("local_rtmp_url", serverPair.getRtmpUrl());
		inv.addModel("remote_id", uid);

		return ret;
	}

	/**
	 * 开发测试入口
	 * 
	 * @param inv
	 * @param uid
	 * @return
	 */
	@LoginRequired
	@Get("/webtalk/dev")
	public String dev(Invocation inv) {
		int uid = -1;
		String uidstr = inv.getParameter("uid");
		if (uidstr != null) {
			uid = Integer.parseInt(uidstr);
		}
		// if( inv.getRequest().getMethod().equalsIgnoreCase("POST") ){
		// }
		int hostid = host.getUser().getId();
		TalkLogUtil.info(" hostid:" + hostid + " uid:" + uid + " "
				+ inv.toString());

		StreamingServerPair serverPair = clusterManager.getServerPair(hostid);

		String ret = PAGE_DEV;

		inv.addModel("local_id", hostid);
		inv.addModel("local_name", host.getUser().getName());
		inv.addModel("local_tiny_url", host.getUser().getTinyFullUrl());
		inv.addModel("local_main_url", host.getUser().getMainFullUrl());
		inv.addModel("local_rtmfp_url", serverPair.getRtmfpUrl());
		inv.addModel("local_rtmp_url", serverPair.getRtmpUrl());
		inv.addModel("remote_id", uid);

		return ret;
	}

}
