package net.jplugin.core.kernel.api.ctx;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.JsonKit;

/**
 *
 * @author: LiuHang
 * @version ����ʱ�䣺2015-2-5 ����10:07:28
 **/

public class RequesterInfo {
	public static final String CLIENT_BROWSER="b";
	public static final String CLIENT_MBROWSER="mb";

	String requestId;
	String clientType;
	String operatorToken;
	String operatorId;
	String currentTenantId;
	String clientAppToken;
	String clientAppCode;
	String callerIpAddress;
	String requestUrl;
	Content content=new Content();
	Cookies cookies=new Cookies();
	
	//following for request content

	
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String ct) {
		this.clientType = ct;
	}
	public String getOperatorToken() {
		return operatorToken;
	}
	public void setOperatorToken(String token) {
		this.operatorToken = token;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getCurrentTenantId() {
		return currentTenantId;
	}
	public void setCurrentTenantId(String mtid) {
		this.currentTenantId = mtid;
	}
	public String getClientAppToken() {
		return clientAppToken;
	}
	public void setClientAppToken(String clientAppToken) {
		this.clientAppToken = clientAppToken;
	}
	public String getClientAppCode() {
		return clientAppCode;
	}
	public void setClientAppCode(String clientAppCode) {
		this.clientAppCode = clientAppCode;
	}
	
	public String getCallerIpAddress() {
		return callerIpAddress;
	}
	public void setCallerIpAddress(String callerIpAddress) {
		this.callerIpAddress = callerIpAddress;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public Content getContent() {
		return content;
	}
	public Cookies getCookies(){
		return cookies;
	}

	/**
	 * following for request content
	 */
	public static class Content{
		String contentType;
		Map<String,String> paramContent;
		String jsonContent;
		Map mapForJsonContent;
		
		
		public String getContentType() {
			return contentType;
		}
		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		public Map<String, String> getParamContent() {
			return paramContent;
		}
		public void setParamContent(Map<String, String> paramContent) {
			this.paramContent = paramContent;
		}
		public String getJsonContent() {
			return jsonContent;
		}
		public void setJsonContent(String jsonContent) {
			this.jsonContent = jsonContent;
			parseAndCacheJsonContent();
		}
		private void parseAndCacheJsonContent() {
			 this.mapForJsonContent = JsonKit.json2Map(jsonContent);
			 //������һ���ݴ�
			 if (this.mapForJsonContent==null) 
				 this.mapForJsonContent = new HashMap<String,String>(0);
		}
		public Map getMapForJsonContent() {
			return mapForJsonContent;
		}
	}
}
