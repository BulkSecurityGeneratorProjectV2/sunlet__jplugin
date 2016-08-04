package net.jplugin.ext.webasic.api;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jplugin.common.kits.MD5Kit;
import net.jplugin.common.kits.StringKit;
import net.jplugin.core.ctx.api.JsonResult;
import net.jplugin.core.ctx.api.RuleResult;

public class AbstractExController {
	private static final String WA_CK = "wa_ck";
	private static final String WA_MD5 = "wa_md5";
	private static final String WA_CH = "wa_ch";
	public static final String JSP_BASE = "/WEB-INF/classes/";

	private HttpServletRequest req;
	private HttpServletResponse res;

	public final void _init(HttpServletRequest req, HttpServletResponse res) {
		this.req = req;
		this.res = res;
		Enumeration nms = req.getParameterNames();
		while (nms.hasMoreElements()) {
			String name = (String) nms.nextElement();
			req.setAttribute(name, req.getParameter(name));
		}
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public String getParam(String nm) {
		return req.getParameter(nm);
	}

	public Object getAttr(String nm) {
		return req.getAttribute(nm);
	}

	public Set<String> getAttrNames() {
		HashSet<String> ret = new HashSet<String>();
		Enumeration<String> nms = req.getAttributeNames();

		while (nms.hasMoreElements()) {
			String key = nms.nextElement();
			ret.add(key);
		}
		return ret;
	}

	public String getStringAttr(String nm) {
		return (String) req.getAttribute(nm);
	}

	public void setAttr(String nm, Object o) {
		req.setAttribute(nm, o);
	}

	public void forward(String path) {
		try {
			req.getRequestDispatcher(path).forward(req, res);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	public void renderJsp(String jsp) {
		if (jsp.startsWith("/")){
			this.forward(jsp);
		}else{
			String name = this.getClass().getName();
			String jsppath;
			if (name.indexOf('.') > 0) {
				name = name.substring(0, name.lastIndexOf('.'));
				name = StringKit.replaceStr(name, ".", "/");
				jsppath = JSP_BASE + name + "/" + jsp;
			} else {
				jsppath = JSP_BASE + jsp;
			}
			this.forward(jsppath);
		}
	}

	@Deprecated
	public void renderJson(String json) {
		getRes().setContentType("text/html; charset=utf-8");
		try {
			getRes().getWriter().print(json);
		} catch (IOException e) {
			throw new RuntimeException("render json error" + json, e);
		}
	}
	
	@Deprecated
	public void renderJson(JsonResult jr){
		String wa_ck = (String) req.getAttribute(WA_CK);
		if ("true".equals(wa_ck)) {
			// Ϊ�˼���һ�����л�,�ȼٶ�wa_ch=true,��ȡMD5,Ȼ�����滻֮
			jr._setProp(WA_CH, "true");
			jr._setProp(WA_MD5, "%{MD5KEY}%");
			String jsonToWrite = jr.toJson();

			String oldMd5 = (String) req.getAttribute(WA_MD5);
			String newMd5 = MD5Kit.MD5(jsonToWrite);
			if (newMd5.equals(oldMd5)) {
				// ������ޱ仯
				JsonResult newrr = JsonResult.create();
				newrr._setProp(WA_CH, "false");
				jsonToWrite = newrr.toJson();
			} else {
				// �������MD5��Ϣ�������ݣ�MD5ֵ�滻��ȥ
				jsonToWrite = StringKit.replaceStr(jsonToWrite, "%{MD5KEY}%",
						newMd5);
			}
			renderJson(jsonToWrite);
		} else {
			// ֱ�����
			renderJson(jr.toJson());
		}
	}
	
	@Deprecated
	public void renderJson(RuleResult rr) {
		String wa_ck = (String) req.getAttribute(WA_CK);
		if ("true".equals(wa_ck)) {
			// Ϊ�˼���һ�����л�,�ȼٶ�wa_ch=true,��ȡMD5,Ȼ�����滻֮
			rr.setContent(WA_CH, "true");
			rr.setContent(WA_MD5, "%{MD5KEY}%");
			String jsonToWrite = rr.getJson();

			String oldMd5 = (String) req.getAttribute(WA_MD5);
			String newMd5 = MD5Kit.MD5(jsonToWrite);
			if (newMd5.equals(oldMd5)) {
				// ������ޱ仯
				RuleResult newrr = RuleResult.create();
				newrr.setContent(WA_CH, "false");
				jsonToWrite = newrr.getJson();
			} else {
				// �������MD5��Ϣ�������ݣ�MD5ֵ�滻��ȥ
				jsonToWrite = StringKit.replaceStr(jsonToWrite, "%{MD5KEY}%",
						newMd5);
			}
			renderJson(jsonToWrite);
		} else {
			// ֱ�����
			renderJson(rr.getJson());
		}
	}

	public void invalidSession() {
		req.getSession().invalidate();
	}

	public void setSessAttr(String k, Object v) {
		req.getSession().setAttribute(k, v);
	}

	public void sendRedirect(String path) {
		try {
			res.sendRedirect(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
