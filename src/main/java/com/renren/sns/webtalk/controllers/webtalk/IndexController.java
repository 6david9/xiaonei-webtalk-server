package com.renren.sns.webtalk.controllers.webtalk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.renren.sns.webtalk.dao.FacetimeFeedbackDAO;
import com.renren.sns.webtalk.dao.FacetimeRecorderDAO;
import com.renren.sns.webtalk.data.CacheException;
import com.renren.sns.webtalk.data.MemCachedCache;
import com.renren.sns.webtalk.models.FacetimeRecorder;
import com.renren.sns.webtalk.models.UserInfo;
import com.renren.sns.webtalk.util.TalkLogUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.xiaonei.commons.interceptors.access.HostHolder;
import com.xiaonei.commons.interceptors.access.loginrequired.LoginRequired;
import com.xiaonei.platform.component.friends.home.FriendsHome;
import com.xiaonei.platform.core.model.User;
import com.xiaonei.platform.core.model.WUserCache;
import com.xiaonei.sns.platform.core.opt.ice.impl.SnsAdapterFactory;
import com.xiaonei.xce.notify.NotifyAdapter;
import com.xiaonei.xce.notify.NotifyBody;
import com.xiaonei.xce.onlinestatefilter.OnlineStateFilterAdapter;

/**
 * @author inshion(xin.yin@opi-corp.com)
 * @since 2011-3-3
 */
@Path("")
public class IndexController {

	@Autowired
	private HostHolder host;

	@Autowired
	protected FacetimeRecorderDAO facetimeRecorderDAO;

	@Autowired
	protected FacetimeFeedbackDAO facetimeFeedbackDAO;

	private static final String RETURN_XML_BEGIN = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><result>";
	private static final String RETURN_XML_END = "</result>";

	private static final String RETURN = "@";

	/**
	 * 查询被叫方数据，返回userid,username,tinyurl,mainurl,status
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("info")
	public String info(Invocation inv/* , @Param("uid") int uid */) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		String clientIp = getIpAddr(inv.getRequest()); // 反向代理计算出的 ip
		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " " + inv.toString());

		String status = FacetimeRecorder.STATE_USER_FREE;
		try {
			FacetimeRecorder recordC = MemCachedCache.getInstance()
					.loadRecordC(hostid, guestid);
			if (recordC == null) {
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						hostid, guestid);
				if (listR == null) {
					status = FacetimeRecorder.STATE_USER_FREE;
				} else {
					if (listR != null && listR.size() < 2) {
						TalkLogUtil.warn("listR.size()=" + listR.size());
					} else {
						if (listR.get(0) == FacetimeRecorder.STATE_R_TALKING) {
							status = FacetimeRecorder.STATE_USER_TALKING;
						} else {
							status = FacetimeRecorder.STATE_USER_FREE;
						}
					}
				}
			} else {
				if (recordC.getState() == FacetimeRecorder.STATE_TALK_TALKING) {
					status = FacetimeRecorder.STATE_USER_TALKING;
				} else if (recordC.getState() == FacetimeRecorder.STATE_TALK_CALLING) {
					status = FacetimeRecorder.STATE_USER_CALLING;
				}
			}
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ guestid + " " + inv.toString() + " error:"
					+ e.getMessage());
		}

		User user = SnsAdapterFactory.getUserAdapter().get(guestid);
		UserInfo userInfo = new UserInfo(guestid);
		userInfo.setPeerId("0");
		userInfo.setRtmfpOk(0);
		userInfo.setRtmpOk(0);
		userInfo.setRtmfpUrl("0");
		userInfo.setRtmpUrl("0");
		userInfo.setName(user.getName());
		userInfo.setTinyurl(user.getTinyFullUrl());
		userInfo.setMainurl(user.getMainFullUrl());
		userInfo.setStatus(status);
		userInfo.setPermission(FriendsHome.getInstance().isFriend(hostid,
				guestid));

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<info>").append(userInfo.toString())
				.append("</info>").append(RETURN_XML_END);

		TalkLogUtil.info(" host:" + hostid + " guest:" + guestid + " result: "
				+ status);
		return RETURN + xmlResult.toString();
	}

	/**
	 * 取得客户端 ip
	 * 
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.replaceAll(", ", "-");
	}

	/**
	 * 用户请求与另一个用户<tt>uid</tt>视频
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("call")
	public String call(Invocation inv/*
									 * @Param("recieverid") int
									 * recieverid,@Param("callerpeer") String
									 * callerpeer,
									 * 
									 * @Param("timeout") int
									 * timeout,@Param("rtmfpok") boolean
									 * rtmfpok,
									 * 
									 * @Param("rtmpok") boolean
									 * rtmpok,@Param("rtmfpurl") String
									 * rtmfpurl,
									 * 
									 * @Param("rtmpurl") String
									 * rtmpurl,@Param("rtmfpStage") int
									 * rtmfpStage,
									 * 
									 * @Param("rtmpStage") int
									 * rtmpStage,@Param("talkStage") int
									 * talkStage,
									 */) {
		int hostid = host.getUser().getId();
		int recieverid = Integer.parseInt(inv.getParameter("recieverid"));
		String callerpeer = inv.getParameter("callerpeer");
		int c_timeout = Integer.parseInt(inv.getParameter("c_timeout"));
		int r_timeout = Integer.parseInt(inv.getParameter("r_timeout"));
		boolean rtmfpok = Boolean.parseBoolean(inv.getParameter("rtmfpok"));
		boolean rtmpok = Boolean.parseBoolean(inv.getParameter("rtmpok"));
		String rtmfpurl = inv.getParameter("rtmfpurl");
		String rtmpurl = inv.getParameter("rtmpurl");
		int rtmfpStage = Integer.parseInt(inv.getParameter("rtmfpStage"));
		int rtmpStage = Integer.parseInt(inv.getParameter("rtmpStage"));
		int talkStage = Integer.parseInt(inv.getParameter("talkStage"));
		String clientIp = getIpAddr(inv.getRequest()); // 反向代理计算出的 ip

		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " guest:"+ recieverid 
				+ " c_timeout:" + c_timeout +" r_timeout:" + r_timeout
				+ " rtmfpok:" + rtmfpok
				+ " rtmpok:" + rtmpok + " stage(rtfmp/rtmp):" + " "
				+ rtmfpStage + " " + rtmpStage + " rtmfpurl:" + rtmfpurl
				+ " rtmpurl:" + rtmpurl + " " + inv.toString());

		boolean opResult = true;

		FacetimeRecorder recorder = new FacetimeRecorder();
		recorder.setCaller_id(hostid);
		recorder.setCaller_rtmfp_ok(rtmfpok);
		recorder.setCaller_rtmp_ok(rtmpok);
		recorder.setCaller_peer_id(callerpeer);
		Date date = new Date();
		recorder.setCaller_time(date);
		recorder.setReciever_time(date);
		recorder.setEnd_time(date);
		recorder.setReceiver_rtmfp_ok(false);
		recorder.setReceiver_rtmp_ok(false);
		recorder.setReciever_id(recieverid);
		recorder.setReceiver_peer_id("0");
		recorder.setRtmfpUrl(rtmfpurl);
		recorder.setRtmpUrl(rtmpurl);
		recorder.setTalk_stage(talkStage);
		recorder.setState(FacetimeRecorder.STATE_TALK_CALLING);
		recorder.setCaller_rtmfp_stage(rtmfpStage);
		recorder.setCaller_rtmp_stage(rtmpStage);
		recorder.setReceiver_rtmfp_stage(0);
		recorder.setReceiver_rtmp_stage(0);

		try {
			MemCachedCache.getInstance().saveRecordC(hostid, hostid, recorder,
					c_timeout);

			List<Integer> listR = MemCachedCache.getInstance().loadListR(
					recieverid, recieverid);
			if (listR == null) {
				listR = new ArrayList<Integer>();
				listR.add(0, FacetimeRecorder.STATE_R_FREE);
			}
			if (!listR.contains(hostid)) {
				listR.add(hostid);
			}
			MemCachedCache.getInstance().saveListR(recieverid, recieverid,
					listR, r_timeout);
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"+ recieverid 
					+ " c_timeout:" + c_timeout +" r_timeout:" + r_timeout
					+ " rtmfpok:"+ rtmfpok + " rtmpok:" + rtmpok + " rtmfpurl:" + rtmfpurl
					+ " rtmpurl:" + rtmpurl + " stage(rtfmp/rtmp):" + " "
					+ rtmfpStage + " " + rtmpStage + " " + inv.toString()
					+ " error:" + e.getMessage());
		}

		if (opResult) {
			NotifyBody o = new NotifyBody();
			// 设定基本字段
			o.setSchemaId(179);
			o.setType(179);
			o.setSource(recieverid);
			o.setOwner(hostid);
			o.addToid(recieverid);
			o.setFromId(hostid);
			o.setTime(System.currentTimeMillis());
			// 设定所有其他字段
			o.setValue("from_name", host.getUser().getName());
			// 调用发送接口
			NotifyAdapter.getInstance().dispatch(o);
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<call>").append(String.valueOf(opResult)).append(
				"</call>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"+ recieverid 
				+ " c_timeout:" + c_timeout +" r_timeout:" + r_timeout
				+ " rtmfpok:" + rtmfpok
				+ " rtmpok:" + rtmpok + " rtmfpurl:" + rtmfpurl + " rtmpurl:"
				+ rtmpurl + " " + " stage(rtfmp/rtmp):" + " " + rtmfpStage
				+ " " + rtmpStage + " result: " + String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 接受好友<code>callerid</code>的请求
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("accept")
	public String accept(Invocation inv/*
										 * @Param("callerid") int callerid,
										 * 
										 * @Param("recieverpeer") String
										 * recieverpeer, @Param("timeout") int
										 * timeout,
										 * 
										 * @Param("rtmfpok") boolean
										 * rtmfpok,@Param("rtmpok") boolean
										 * rtmpok,
										 * 
										 * @Param("rtmfpStage") int
										 * rtmfpStage,@Param("rtmpStage") int
										 * rtmpStage,
										 * 
										 * @Param("talkStage") int talkStage
										 */) {
		int hostid = host.getUser().getId();
		int callerid = Integer.parseInt(inv.getParameter("callerid"));
		String recieverpeer = inv.getParameter("recieverpeer");
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		boolean rtmfpok = Boolean.parseBoolean(inv.getParameter("rtmfpok"));
		boolean rtmpok = Boolean.parseBoolean(inv.getParameter("rtmpok"));
		int rtmfpStage = Integer.parseInt(inv.getParameter("rtmfpStage"));
		int rtmpStage = Integer.parseInt(inv.getParameter("rtmpStage"));
		int talkStage = Integer.parseInt(inv.getParameter("talkStage"));
		String clientIp = getIpAddr(inv.getRequest());
		String rtmfpurl = inv.getParameter("rtmfpurl");
		String rtmpurl = inv.getParameter("rtmpurl");
		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " guest:"
				+ callerid + " recieverpeer:" + recieverpeer + " timeout:"
				+ timeout + " rtmfpok:" + rtmfpok + " rtmpok:" + rtmpok
				+ " stage(rtfmp/rtmp):" + " " + rtmfpStage + " " + rtmpStage
				+ " talkStage:" + talkStage + " rtmfpurl:" + rtmfpurl
				+ " rtmpurl:" + rtmpurl + " " + inv.toString());

		boolean opResult = true;

		try {
			List<Integer> listR = MemCachedCache.getInstance().loadListR(
					hostid, hostid);
			if (listR != null) {
				if (listR.get(0).intValue() == FacetimeRecorder.STATE_R_FREE) {

					// 删除请求方
					while (listR.contains(callerid)) {
						listR.remove(new Integer(callerid));
					}

					// 删除第一个状态位
					listR.remove(0);
					// 设置被拒的caller状态
					for (Iterator<Integer> iter = listR.iterator(); iter
							.hasNext();) {
						int otherCallerId = iter.next().intValue();
						FacetimeRecorder recorder = MemCachedCache
								.getInstance().loadRecordC(otherCallerId,
										otherCallerId);
						if (recorder != null) {
							// 当拒绝一个呼叫时,只需要纪录拒绝状态以及时间即可
							recorder
									.setState(FacetimeRecorder.STATE_TALK_RECEIVER_REJECT);
							Date date = new Date();
							recorder.setReciever_time(date);
							recorder.setEnd_time(date);
							facetimeRecorderDAO.insertRecorder(recorder
									.getCaller_id(), recorder
									.getCaller_rtmfp_stage(), recorder
									.getCaller_rtmp_stage(), recorder
									.getCaller_peer_id(), recorder
									.getReciever_id(), recorder
									.getReceiver_rtmfp_stage(), recorder
									.getReceiver_rtmp_stage(), recorder
									.getReceiver_peer_id(), recorder
									.getTalk_stage(), recorder.getState(),
									recorder.getCaller_time(), recorder
											.getReciever_time(), recorder
											.getEnd_time());
						}
					}
					listR.clear();
					// 重写listR
					listR.add(FacetimeRecorder.STATE_R_TALKING);
					listR.add(callerid);
					MemCachedCache.getInstance().saveListR(hostid, hostid,
							listR, timeout);
				}
			}

			// 接受请求
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(callerid, callerid);
			if (recorder != null) {
				recorder.setReciever_time(new Date());
				recorder.setState(FacetimeRecorder.STATE_TALK_TALKING);
				recorder.setReceiver_peer_id(recieverpeer);
				recorder.setReceiver_rtmfp_stage(rtmfpStage);
				recorder.setReceiver_rtmp_stage(rtmpStage);
				recorder.setTalk_stage(talkStage);

				MemCachedCache.getInstance().saveRecordC(callerid, callerid,
						recorder, timeout);
			} else {
				opResult = false;
			}

		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ callerid + " recieverpeer:" + recieverpeer + " timeout:"
					+ timeout + " rtmfpok:" + rtmfpok + " rtmpok:" + rtmpok
					+ " stage(rtfmp/rtmp):" + " " + rtmfpStage + " "
					+ rtmpStage + " talkStage:" + talkStage + " rtmfpurl:"
					+ rtmfpurl + " rtmpurl:" + rtmpurl + " " + inv.toString()
					+ " error: " + e.getMessage());

		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<accept>").append(String.valueOf(opResult)).append(
				"</accept>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ callerid + " recieverpeer:" + recieverpeer + " timeout:"
				+ timeout + " rtmfpok:" + rtmfpok + " rtmpok:" + rtmpok
				+ " stage(rtfmp/rtmp):" + " " + rtmfpStage + " " + rtmpStage
				+ " talkStage:" + talkStage + " rtmfpurl:" + rtmfpurl
				+ " rtmpurl:" + rtmpurl + " result: "
				+ String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 呼叫方主动放弃视频请求
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("cancel")
	public String cancel(Invocation inv/* , @Param("timeout") int timeout */) {
		int hostid = host.getUser().getId();
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		String clientIp = getIpAddr(inv.getRequest());
		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " timeout:"
				+ timeout + " " + inv.toString());

		boolean opResult = true;

		try {
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(hostid, hostid);

			if (recorder != null) {
				// 当取消一个呼叫时,只需要纪录取消状态以及时间即可
				recorder.setState(FacetimeRecorder.STATE_TALK_CALL_CANCEL);
				recorder.setEnd_time(new Date());
				// recorder.setReciever_time( getDefaultDate());
				facetimeRecorderDAO.insertRecorder(recorder.getCaller_id(),
						recorder.getCaller_rtmfp_stage(), recorder
								.getCaller_rtmp_stage(), recorder
								.getCaller_peer_id(),
						recorder.getReciever_id(), recorder
								.getReceiver_rtmfp_stage(), recorder
								.getReceiver_rtmp_stage(), recorder
								.getReceiver_peer_id(), recorder
								.getTalk_stage(), recorder.getState(), recorder
								.getCaller_time(), recorder.getReciever_time(),
						recorder.getEnd_time());
				MemCachedCache.getInstance().removeRecordC(hostid, hostid);

				int uid = recorder.getReciever_id();
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						uid, uid);
				while (listR.contains(hostid)) {
					listR.remove(new Integer(hostid));
				}
				if (listR.size() <= 1) {
					MemCachedCache.getInstance().removeListR(uid, uid);
				} else {
					MemCachedCache.getInstance().saveListR(uid, uid, listR,
							timeout);
				}
			}
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " timeout:"
					+ timeout + " " + inv.toString() + " error: "
					+ e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<cancel>").append(String.valueOf(opResult)).append(
				"</cancel>").append(RETURN_XML_END);

		TalkLogUtil.info("[" + clientIp + "]" + " host:" + hostid + " timeout:"
				+ timeout + " result: " + String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 呼叫方主动放弃视频请求
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("timeout")
	public String timeout(Invocation inv/* , @Param("timeout") int timeout */) {
		int hostid = host.getUser().getId();
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		String clientIp = getIpAddr(inv.getRequest());
		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " timeout:"
				+ timeout + inv.toString());

		boolean opResult = true;

		try {
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(hostid, hostid);

			if (recorder != null) {
				// 呼叫方呼叫超时,入库
				recorder.setState(FacetimeRecorder.STATE_TALK_CALL_TIMEOUT);
				recorder.setEnd_time(new Date());
				// recorder.setReciever_time( getDefaultDate());
				facetimeRecorderDAO.insertRecorder(recorder.getCaller_id(),
						recorder.getCaller_rtmfp_stage(), recorder
								.getCaller_rtmp_stage(), recorder
								.getCaller_peer_id(),
						recorder.getReciever_id(), recorder
								.getReceiver_rtmfp_stage(), recorder
								.getReceiver_rtmp_stage(), recorder
								.getReceiver_peer_id(), recorder
								.getTalk_stage(), recorder.getState(), recorder
								.getCaller_time(), recorder.getReciever_time(),
						recorder.getEnd_time());
				MemCachedCache.getInstance().removeRecordC(hostid, hostid);

				int uid = recorder.getReciever_id();
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						uid, uid);
				while (listR.contains(hostid)) {
					listR.remove(new Integer(hostid));
				}
				if (listR.size() <= 1) {
					MemCachedCache.getInstance().removeListR(uid, uid);
				} else {
					MemCachedCache.getInstance().saveListR(uid, uid, listR,
							timeout);
				}
			}
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " timeout:"
					+ timeout + inv.toString() + " error: " + e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<timeout>").append(String.valueOf(opResult)).append(
				"</timeout>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " timeout:"
				+ timeout + " result: " + String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 拒绝用户视频请求
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("reject")
	public String reject(Invocation inv/*
										 * , @Param("uid") int uid,
										 * 
										 * @Param("timeout") int timeout
										 */) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		String clientIp = getIpAddr(inv.getRequest());
		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " timeout:" + timeout + " " + inv.toString());

		boolean opResult = true;
		try {
			List<Integer> listR = MemCachedCache.getInstance().loadListR(
					hostid, hostid);
			if (listR != null) {
				while (listR.contains(guestid)) {
					listR.remove(new Integer(guestid));
				}
				if (listR.size() <= 1) {
					MemCachedCache.getInstance().removeListR(hostid, hostid);
				} else {
					MemCachedCache.getInstance().saveListR(hostid, hostid,
							listR, timeout);
				}
			}

			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(hostid, guestid);
			if (recorder != null) {
				// 当拒绝一个呼叫时,只需要纪录拒绝状态以及时间即可
				recorder.setState(FacetimeRecorder.STATE_TALK_RECEIVER_REJECT);
				Date date = new Date();
				recorder.setReciever_time(date);
				recorder.setEnd_time(date);
				facetimeRecorderDAO.insertRecorder(recorder.getCaller_id(),
						recorder.getCaller_rtmfp_stage(), recorder
								.getCaller_rtmp_stage(), recorder
								.getCaller_peer_id(),
						recorder.getReciever_id(), recorder
								.getReceiver_rtmfp_stage(), recorder
								.getReceiver_rtmp_stage(), recorder
								.getReceiver_peer_id(), recorder
								.getTalk_stage(), recorder.getState(), recorder
								.getCaller_time(), recorder.getReciever_time(),
						recorder.getEnd_time());
				MemCachedCache.getInstance().removeRecordC(guestid, guestid);
			}
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ guestid + " timeout:" + timeout + " " + inv.toString()
					+ " error:" + e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<reject>").append(String.valueOf(opResult)).append(
				"</reject>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " timeout:" + timeout + " result: "
				+ String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 被挂断方通知服务器：连接已经挂断
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("behangup")
	public String behangup(Invocation inv/*
										 * , @Param("uid") int uid,
										 * 
										 * @Param("hostIsCaller") boolean
										 * hostIsCaller
										 * 
										 * @Param("rtmp1S_C_Kbytes")
										 * 
										 * @Param("rtmp1C_S_Kbytes")
										 * 
										 * @Param("rtmp2S_C_Kbytes")
										 * 
										 * @Param("rtmp2C_S_Kbytes")
										 */) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		boolean hostIsCaller = Boolean.parseBoolean(inv
				.getParameter("hostiscaller"));
		String talktime = inv.getParameter("talktime");
		String clientIp = getIpAddr(inv.getRequest());

		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " hostiscaller:" + hostIsCaller + " talktime:"
				+ talktime + " " + inv.toString());

		boolean opResult = true;

		int callerId;
		int calleeId;
		if (hostIsCaller) {
			callerId = hostid;
			calleeId = guestid;
		} else {
			callerId = guestid;
			calleeId = hostid;
		}
		try {
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(callerId, callerId);
			if (recorder != null) {
				recorder.setState(FacetimeRecorder.STATE_TALK_HANGUP);
				recorder.setEnd_time(new Date());
				facetimeRecorderDAO.insertRecorder(recorder.getCaller_id(),
						recorder.getCaller_rtmfp_stage(), recorder
								.getCaller_rtmp_stage(), recorder
								.getCaller_peer_id(),
						recorder.getReciever_id(), recorder
								.getReceiver_rtmfp_stage(), recorder
								.getReceiver_rtmp_stage(), recorder
								.getReceiver_peer_id(), recorder
								.getTalk_stage(), recorder.getState(), recorder
								.getCaller_time(), recorder.getReciever_time(),
						recorder.getEnd_time());
				MemCachedCache.getInstance().removeRecordC(callerId, callerId);
			}
			MemCachedCache.getInstance().removeListR(calleeId, calleeId);

		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ guestid + " hostiscaller:" + hostIsCaller + " talktime:"
					+ talktime + " " + inv.toString() + " error: "
					+ e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<behangup>").append(String.valueOf(opResult)).append(
				"</behangup>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " hostiscaller:" + hostIsCaller + " talktime:"
				+ talktime + " result: " + String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 客户端提交的连接视频服务器不成功的信息
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("failed")
	public String failed(Invocation inv/*
										 * @Param("userid") int guestid,
										 * 
										 * @Param("hostiscaller")
										 * boolean,hostIsCaller,
										 * 
										 * @Param("rtmfpurl") String
										 * rtmfpurl,@Param("rtmpurl") String
										 * rtmpurl,
										 * 
										 * @Param("rtmfpStage") int
										 * rtmfpStage,@Param("rtmpStage") int
										 * rtmpStage,
										 * 
										 * @Param("talkStage") int
										 * talkStage,@Param("timeout") int
										 * timeout,
										 * @Param("flashVersion")String flashVersion
										 */) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		boolean hostIsCaller = Boolean.parseBoolean(inv
				.getParameter("hostiscaller"));
		String rtmfpurl = inv.getParameter("rtmfpurl");
		String rtmpurl = inv.getParameter("rtmpurl");
		int rtmfpStage = Integer.parseInt(inv.getParameter("rtmfpStage"));
		int rtmpStage = Integer.parseInt(inv.getParameter("rtmpStage"));
		int talkStage = Integer.parseInt(inv.getParameter("talkStage"));
		String clientIp = getIpAddr(inv.getRequest());
		String flashVersion = inv.getParameter("flashVersion");
		FacetimeRecorder recorder = new FacetimeRecorder();

		if (hostIsCaller) {
			recorder.setCaller_id(hostid);
			recorder.setCaller_rtmfp_stage(rtmfpStage);
			recorder.setCaller_rtmp_stage(rtmpStage);
			recorder.setReciever_id(guestid);
			recorder.setReceiver_rtmfp_stage(0);
			recorder.setReceiver_rtmp_stage(0);

			recorder.setCaller_peer_id("0");
			recorder.setReceiver_peer_id("0");

			recorder.setTalk_stage(talkStage);
			recorder.setState(FacetimeRecorder.STATE_TALK_CALL_FAIL);

			recorder.setCaller_time(new Date());
			recorder.setCaller_time(new Date());
			recorder.setCaller_time(new Date());
		} else {
			try {
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						hostid, hostid);
				if (listR != null) {
					while (listR.contains(guestid)) {
						listR.remove(new Integer(guestid));
					}
					if (listR.size() <= 1) {
						MemCachedCache.getInstance()
								.removeListR(hostid, hostid);
					} else {
						MemCachedCache.getInstance().saveListR(hostid, hostid,
								listR, timeout);
					}
				}

				recorder = MemCachedCache.getInstance().loadRecordC(hostid,
						guestid);
				if (recorder != null) {
					// 当拒绝一个呼叫时,只需要纪录拒绝状态以及时间即可
					recorder
							.setState(FacetimeRecorder.STATE_TALK_RECEIVER_FAIL);
					Date date = new Date();
					recorder.setReciever_time(date);
					recorder.setEnd_time(date);
					recorder.setReceiver_rtmfp_stage(rtmfpStage);
					recorder.setReceiver_rtmp_stage(rtmpStage);
					recorder.setTalk_stage(talkStage);

					MemCachedCache.getInstance().saveRecordC(hostid, guestid,
							recorder, timeout);
				}
			} catch (Exception e) {
				recorder = null;
				TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid
						+ " guest:" + guestid + " hostiscaller:" + hostIsCaller
						+ " stage(rtfmp/rtmp):" + " " + rtmfpStage + " "
						+ rtmpStage + " rtmfpurl:" + rtmfpurl + " rtmpurl:"
						+ rtmpurl + " flashVersion:"+flashVersion
						+ " " + inv.toString() + " error:"
						+ e.getMessage());

			}
		}

		if (recorder != null) {
			facetimeRecorderDAO.insertRecorder(recorder.getCaller_id(),
					recorder.getCaller_rtmfp_stage(), recorder
							.getCaller_rtmp_stage(), recorder
							.getCaller_peer_id(), recorder.getReciever_id(),
					recorder.getReceiver_rtmfp_stage(), recorder
							.getReceiver_rtmp_stage(), recorder
							.getReceiver_peer_id(), recorder.getTalk_stage(),
					recorder.getState(), recorder.getCaller_time(), recorder
							.getReciever_time(), recorder.getEnd_time());
		}

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " hostiscaller:" + hostIsCaller
				+ " stage(rtfmp/rtmp):" + " " + rtmfpStage + " " + rtmpStage
				+ " rtmfpurl:" + rtmfpurl + " rtmpurl:" + rtmpurl+ " flashVersion:"+flashVersion);

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<failed>").append("true").append("</failed>").append(
				RETURN_XML_END);
		return RETURN + xmlResult.toString();
	}

	/**
	 * 记录带宽检测结果
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("bwcheck")
	public String bwcheck(Invocation inv/*
										 * 
										 * @Param("action") String
										 * action,@Param("rtmpS_C_Kbytes") int
										 * rtmpS_C_Kbytes,
										 * 
										 * @Param("rtmpC_S_Kbytes") int
										 * rtmpC_S_Kbytes
										 */) {
		String action = inv.getParameter("action");
		int rtmpS_C_Kbytes = Integer.parseInt(inv
				.getParameter("rtmpS_C_Kbytes"));
		int rtmpC_S_Kbytes = Integer.parseInt(inv
				.getParameter("rtmpC_S_Kbytes"));
		String clientIp = getIpAddr(inv.getRequest());

		TalkLogUtil.info(" [" + clientIp + "] " + action + " rtmpS_C_Kbytes:"
				+ rtmpS_C_Kbytes + " rtmpC_S_Kbytes:" + rtmpC_S_Kbytes);

		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<bwCheck>").append("true").append("</bwCheck>")
				.append(RETURN_XML_END);

		return RETURN + xmlResult.toString();
	}

	/**
	 * 客户端提交的调试信息和用户反馈
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("report")
	public String report(Invocation inv) {
		int hostid = host.getUser().getId();
		String clientIp = getIpAddr(inv.getRequest());
		int result = -1;
		try {
			result = facetimeFeedbackDAO.insertFeedback(Integer.parseInt(inv
					.getParameter("talktype")), Integer.parseInt(inv
					.getParameter("type")), inv.getParameter("detail"));
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
					+ inv.toString() + " error: " + e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<report>").append(String.valueOf(result == 1))
				.append("</report>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " result: "
				+ String.valueOf(result == 1));

		return RETURN + xmlResult.toString();
	}

	/**
	 * 查询有哪些人正在呼叫你。 如果检查到MC_C与MC_R数据不一致时，清除脏数据。
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("listing")
	public String listing(Invocation inv) {
		int hostid = host.getUser().getId();
		String clientIp = getIpAddr(inv.getRequest());
		// TalkLogUtil.debug(" host:" + hostid + " " + inv.toString());

		List<Integer> listR = null;

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		try {
			listR = MemCachedCache.getInstance().loadListR(hostid, hostid);
			if (listR != null && listR.size() > 1) {
				if (listR.get(0).intValue() == FacetimeRecorder.STATE_R_FREE) {
					for (int index = 1; index < listR.size(); index++) {
						int callerId = listR.get(index);
						FacetimeRecorder recorder = MemCachedCache
								.getInstance().loadRecordC(callerId, callerId);
						if (recorder != null) {
							UserInfo userInfo = new UserInfo(callerId);
							userInfo.setPeerId(recorder.getCaller_peer_id());
							userInfo
									.setRtmfpOk(recorder.isCaller_rtmfp_ok() ? 1
											: 0);
							userInfo.setRtmpOk(recorder.isCaller_rtmp_ok() ? 1
									: 0);
							userInfo.setRtmfpUrl(recorder.getRtmfpUrl());
							userInfo.setRtmpUrl(recorder.getRtmpUrl());

							User caller = SnsAdapterFactory.getUserAdapter()
									.get(callerId);
							userInfo.setName(caller.getName());
							userInfo.setTinyurl(caller.getTinyFullUrl());
							userInfo.setMainurl(caller.getMainFullUrl());
							userInfo
									.setStatus(FacetimeRecorder.STATE_USER_CALLING);
							userInfo.setPermission(true);

							xmlResult.append("<user>").append(
									userInfo.toString()).append("</user>");

						} else {
							// 当发现MC_C和MC_R不一致时，清除数据
							listR.remove(index);
						}
					}
				}
			}
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
					+ inv.toString() + " error: " + e.getMessage());
		}

		xmlResult.append(RETURN_XML_END);

		// TalkLogUtil.debug(" host:" + hostid + " " + inv.toString()
		// + " result: size_" + userInfoList.size());
		return RETURN + xmlResult.toString();
	}

	/**
	 * 呼叫方在发起呼叫之后，没有自己挂断，也没开始通话时，n秒钟一次的轮询，以便了解自己是否被拒绝
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("holding")
	public String holding(Invocation inv) {
		int hostid = host.getUser().getId();
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		String clientIp = getIpAddr(inv.getRequest());
		// TalkLogUtil.debug(" host:" + hostid + " " + inv.toString());

		String result = "ERR";
		FacetimeRecorder recorder = null;
		try {
			recorder = MemCachedCache.getInstance().loadRecordC(hostid, hostid);
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
					+ inv.toString() + " error:" + e.getMessage());
		}
		if (recorder == null) {
			result = "MISS";
		} else if (recorder.getState() == FacetimeRecorder.STATE_TALK_CALLING) {
			result = "WAIT";
			try {
				MemCachedCache.getInstance().saveRecordC(hostid, hostid,
						recorder, timeout);
			} catch (CacheException e) {
				result = "ERR";
				TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
						+ inv.toString() + " error:" + e.getMessage());
			}
		} else {// if (recorder.getState() ==
			// FacetimeRecorder.STATE_TALK_RECEIVER_FAIL) {
			result = "ERR";
			try {
				MemCachedCache.getInstance().removeRecordC(hostid, hostid);
			} catch (Exception e) {
				TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
						+ inv.toString() + " error:" + e.getMessage());
			}
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<holding>").append(result).append("</holding>")
				.append(RETURN_XML_END);

		TalkLogUtil.debug(" [" + clientIp + "] host:" + hostid + " result:"
				+ result);
		return RETURN + xmlResult.toString();
	}

	/**
	 * 被叫方查询某个呼叫是否还存在
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("ringing")
	public String ringing(Invocation inv) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		String clientIp = getIpAddr(inv.getRequest());
		// TalkLogUtil.debug(" host:" + hostid + " guest:" + guestid + " "
		// + inv.toString());

		String result = "ERR";
		FacetimeRecorder recorder = null;
		try {
			recorder = MemCachedCache.getInstance().loadRecordC(guestid,
					guestid);
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ guestid + " " + inv.toString() + " error:"
					+ e.getMessage());
		}
		if (recorder == null) {
			result = "MISS";
		} else if (recorder.getState() == FacetimeRecorder.STATE_TALK_CALLING) {
			result = "WAIT";
		} else {
			result = "ERR";
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<ringing>").append(result).append("</ringing>")
				.append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " result: " + result);
		return RETURN + xmlResult.toString();
	}

	/**
	 * 更新MemCached中MEM_C及MEM_R
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("talking")
	public String talking(Invocation inv/* , @Param("timeout") int timeout */) {
		int hostid = host.getUser().getId();
		int guestid = Integer.parseInt(inv.getParameter("userid"));
		int timeout = Integer.parseInt(inv.getParameter("timeout"));
		boolean hostIsCaller = Boolean.parseBoolean(inv
				.getParameter("hostiscaller"));
		String quality = inv.getParameter("quality");
		String talktime = inv.getParameter("talktime");
		String clientIp = getIpAddr(inv.getRequest());

		boolean opResult = true;
		try {
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(hostid, hostid);
			if (recorder != null) {
				MemCachedCache.getInstance().saveRecordC(hostid, hostid,
						recorder, timeout);
			}

			List<Integer> listR = MemCachedCache.getInstance().loadListR(
					hostid, hostid);
			if (listR != null) {
				MemCachedCache.getInstance().saveListR(hostid, hostid, listR,
						timeout);
			}
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " guest:"
					+ guestid + " hostIsCaller:" + hostIsCaller + " quality:"
					+ quality + " timeout:" + timeout + " talktime:" + talktime
					+ " " + inv.toString() + " error: " + e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<talking>").append(String.valueOf(opResult)).append(
				"</talking>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " guest:"
				+ guestid + " hostIsCaller:" + hostIsCaller + " quality:"
				+ quality + " timeout:" + timeout + " talktime:" + talktime
				+ " result: " + String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	/**
	 * 查询当前在线好友列表
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("queryonline")
	public String queryonline(Invocation inv) {
		int hostid = host.getUser().getId();
		String clientIp = getIpAddr(inv.getRequest());
		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid);
		List<Integer> fs = SnsAdapterFactory.getBuddyByNameCacheAdapter()
				.getFriendList(hostid);
		int[] fsArr = new int[fs.size()];
		for (int i = 0; i < fs.size(); ++i) {
			fsArr[i] = fs.get(i);
		}
		List<Integer> ofs = OnlineStateFilterAdapter.getInstance()
				.getOnlineIdsFrom(fsArr);
		Map<Integer, WUserCache> ous = SnsAdapterFactory.getUserCacheAdapter()
				.getUserCacheMap(ofs);

		JSONObject online_friends_info = new JSONObject();
		JSONArray friends = new JSONArray();
		for (Integer one : ofs) {
			WUserCache uc = ous.get(one);
			if (uc == null) {
				continue;
			}
			JSONObject person = new JSONObject();
			person.put("userid", uc.getId());
			person.put("username", uc.getName());
			person.put("tinyurl", uc.getTiny_Url());
			friends.put(person);
		}
		online_friends_info.put("friends", friends);
		return RETURN + online_friends_info.toString();
	}

	/**
	 * 查询某在线好友信息
	 * 
	 * @param inv
	 * @return
	 */
	@LoginRequired
	@Post("queryonlineone")
	public String queryonlineone(Invocation inv) {
		int hostid = host.getUser().getId();
		int uid = Integer.parseInt(inv.getParameter("uid"));
		String clientIp = getIpAddr(inv.getRequest());
		TalkLogUtil
				.info(" [" + clientIp + "] host:" + hostid + " guest:" + uid);

		User user = SnsAdapterFactory.getUserAdapter().get(uid);

		JSONObject friend = new JSONObject();
		JSONObject info = new JSONObject();
		info.put("userid", uid);
		info.put("username", user.getName());
		info.put("tinyurl", user.getTinyFullUrl());
		friend.put("friend", info);
		return RETURN + friend.toString();
	}

	/**
	 * 清除某个用户在两个memcache中的所有数据
	 * 
	 * @param inv
	 * @param uid
	 * @return
	 */
	@LoginRequired
	@Get("clear/{uid:\\d+}/{timeout:\\d+}")
	public String clear(Invocation inv, @Param("uid") int uid,
			@Param("timeout") int timeout) {
		int hostid = host.getUser().getId();
		String clientIp = getIpAddr(inv.getRequest());

		boolean opResult = true;
		try {
			// clear caller
			FacetimeRecorder recorder = MemCachedCache.getInstance()
					.loadRecordC(uid, uid);
			if (recorder != null) {
				int calleeId = recorder.getReciever_id();
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						calleeId, calleeId);
				if (listR != null) {
					while (listR.contains(calleeId)) {
						listR.remove(new Integer(calleeId));
					}
					if (listR.size() <= 1) {
						MemCachedCache.getInstance().removeListR(uid, uid);
					} else {
						MemCachedCache.getInstance().saveListR(uid, uid, listR,
								timeout);
					}

					MemCachedCache.getInstance().removeRecordC(uid, uid);
				}
			}

			// callee
			List<Integer> listR = MemCachedCache.getInstance().loadListR(uid,
					uid);
			if (listR != null) {
				if (listR.size() > 1) {
					for (int index = 1; index < listR.size(); index++) {
						int calleeId = listR.get(index);
						MemCachedCache.getInstance().removeRecordC(calleeId,
								calleeId);
					}
				}
				MemCachedCache.getInstance().removeListR(uid, uid);
			}
		} catch (Exception e) {
			opResult = false;
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " "
					+ inv.toString() + " error: " + e.getMessage());
		}

		// to xml
		StringBuilder xmlResult = new StringBuilder(RETURN_XML_BEGIN);
		xmlResult.append("<clear>").append(String.valueOf(opResult)).append(
				"</clear>").append(RETURN_XML_END);

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " result: "
				+ String.valueOf(opResult));
		return RETURN + xmlResult.toString();
	}

	@LoginRequired
	@Get("debug/{test:\\w+}")
	public String debug(Invocation inv, @Param("test") String test) {
		int hostid = host.getUser().getId();
		String clientIp = getIpAddr(inv.getRequest());

		String status = FacetimeRecorder.STATE_USER_FREE;
		try {
			FacetimeRecorder recordC = MemCachedCache.getInstance()
					.loadRecordC(hostid, hostid);
			if (recordC == null) {
				List<Integer> listR = MemCachedCache.getInstance().loadListR(
						hostid, hostid);
				if (listR == null) {
					status = FacetimeRecorder.STATE_USER_FREE;
				} else {
					if (listR != null && listR.size() < 2) {
						TalkLogUtil.warn("listR.size()=" + listR.size());
					} else {
						if (listR.get(0) == FacetimeRecorder.STATE_R_TALKING) {
							status = FacetimeRecorder.STATE_USER_TALKING;
						} else {
							status = FacetimeRecorder.STATE_USER_FREE;
						}
					}
				}
			} else {
				if (recordC.getState() == FacetimeRecorder.STATE_TALK_TALKING) {
					status = FacetimeRecorder.STATE_USER_TALKING;
				} else if (recordC.getState() == FacetimeRecorder.STATE_TALK_CALLING) {
					status = FacetimeRecorder.STATE_USER_CALLING;
				}
			}
		} catch (Exception e) {
			TalkLogUtil.warn(" [" + clientIp + "] host:" + hostid + " test:"
					+ test + " " + inv.toString() + " error " + e.getMessage());
		}

		TalkLogUtil.info(" [" + clientIp + "] host:" + hostid + " status:"
				+ status + " test:" + test);

		return RETURN+hostid+" "+status+" "+test;
		// if (recorder != null) {
		// // to xml
		// String xmlResult = "";
		// XStream xStream = new XStream(new DomDriver());
		// xStream.alias("result", FacetimeRecorder.class);
		// try {
		// Document doc = DocumentHelper
		// .parseText(xStream.toXML(recorder));
		// doc.setXMLEncoding("UTF-8");
		// xmlResult = doc.asXML();
		// } catch (DocumentException e) {
		// TalkLogUtil.error(" host:" + hostid + " action:debug error "
		// + e.getMessage());
		// }
		// return RETURN + xmlResult;
		// } else {
		// return RETURN + RETURN_XML_BEGIN + RETURN_XML_END;
		// }

	}
	
//    private static 	ConcurrentHashMap<String,String> peerUserIDsMap = new ConcurrentHashMap<String,String>();
//    private static 	ConcurrentHashMap<String,Date> peerUserIDsTimeMap = new ConcurrentHashMap<String,Date>();
//
//    public Map<String,String> getPeerUserIDsMap()
//    {
//    	return peerUserIDsMap;
//    }
//	@LoginRequired
//	@Post("recordCurrPeerUserIDs")
//	public void recordCurrPeerUserIDs(Invocation inv) {
//		
//		int hostid = host.getUser().getId();
//		String peerID = inv.getParameter("peerID");
//		String userID = inv.getParameter("userID");
//		
//		boolean isAdd = Boolean.parseBoolean(inv
//				.getParameter("isAdd"));
//		try {
//			Date now = new Date();
//			Date preHour = new Date();
//			preHour.setHours(now.getHours()-1); 
//			
//			if(isAdd)
//			{
//			   peerUserIDsMap.put(userID, peerID);
//			   peerUserIDsTimeMap.put(userID, now);
//			   
//			}else{
//				peerUserIDsMap.remove(userID);
//				peerUserIDsTimeMap.remove(userID);
//			}
//			
//			//删除 map 中当前时间一个小时前的 对象
//			removeUnnecessaryObj(now,preHour);
//			
//		} catch (Exception e) {
//			TalkLogUtil.warn("host:" + hostid + " peerID:"+peerID
//					  +" userID:"+userID+" isAdd:"+isAdd+ inv.toString() + " error:"
//					  + e.getMessage());
//		}
//
//
//		TalkLogUtil.info("host:" + hostid + " peerID:"+peerID
//				         +" userID:"+userID+" isAdd:"+isAdd);
//	}
	/**
	 * 删除 map 中当前时间一个小时前的 对象
	 * @param now
	 * @param preHour
	 */
//	private void removeUnnecessaryObj(Date now,Date preHour)
//	{
//		Iterator it = peerUserIDsTimeMap.entrySet().iterator();
//		while(it.hasNext())
//		{
//			Map.Entry entry = (Map.Entry)it.next();
//			String userid = (String)entry.getValue();
//			Date time = (Date)entry.getValue();
//		
//			if(time.before(preHour))
//			{
//				peerUserIDsTimeMap.remove(userid);
//				peerUserIDsTimeMap.remove(userid);
//			}
//		}
//	}
	
	public static void main(String[] args) {

		// test for xml binding
		/*
		 * UserInfo user = new UserInfo(); user.setName("name");
		 * user.setPeerId("peerId"); user.setTinyurl("tinyurl");
		 * user.setUserId(100101);
		 * 
		 * UserInfo user1 = new UserInfo(); user1.setName("name");
		 * user1.setPeerId("peerId"); user1.setTinyurl("tinyurl");
		 * user1.setUserId(100101);
		 * 
		 * List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		 * userInfoList.add(user); userInfoList.add(user1);
		 * 
		 * XStream xStream = new XStream(new DomDriver());
		 * xStream.alias("callers", List.class); xStream.alias("user",
		 * UserInfo.class);
		 * 
		 * Document doc = null; try { doc =
		 * DocumentHelper.parseText(xStream.toXML(userInfoList)); } catch
		 * (DocumentException e) { e.printStackTrace(); }
		 * doc.setXMLEncoding("UTF-8");
		 * 
		 * System.out.println(doc.asXML());
		 */

		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		System.out.println(list.contains(1));
		System.out.println(list.contains(new Integer(1)));

		DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = df.parse("0000-00-00 00:00:00");
			System.out.println(date);
			System.out.println(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(Boolean.getBoolean("true"));
		System.out.println(Boolean.parseBoolean("true"));
	}
}
