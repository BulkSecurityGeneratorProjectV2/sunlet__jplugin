package net.jplugin.ext.staticweb.impl;

import java.util.HashMap;
import java.util.Map;

import net.jplugin.common.kits.FileKit;
import net.jplugin.core.kernel.api.PluginEnvirement;
import net.jplugin.ext.staticweb.api.IContentManager;

public class ConentManagerImpl implements IContentManager {

	private static final int MAX_POST_CHECK = 15;

	/**
	 * Ϊ�˼���ESF���Ϲ��򣬿��Դ�.do��Ҳ���Բ����������
	 * ���β��û�е㣬������ .do������false��
	 * ���򷵻�true
	 */
	@Override
	public boolean accept(String uri) {
		String post = getPost(uri);
		if (post==null || ".do".equals(post)) 
			return false; //β��û�е㣬Ŀǰ�ж�15λ
		else{
			return true;
		}
	}

	private String getContentTypeForUri(String uri) {
		String post = getPost(uri);
		return ContentTypeMaps.get(post);
	}

	/**
	 * ��ȡ���MAX_POST_CHECK λ�������.XXX�Ļ�������.XXX
	 * @param uri
	 * @return
	 */
	private String getPost(String uri) {
		int maxlen = MAX_POST_CHECK;
		int lastIndex = uri.lastIndexOf('.');
		if (lastIndex < 0)
			return null;
		if (uri.length() - lastIndex > maxlen)
			return null;
		String post = uri.substring(lastIndex);
		return post;
	}

	public String getContentTypeForPostPrefix(String post) {
		return ContentTypeMaps.get(post);
	}

	@Override
	public Response handleRequest(Request req) {
//		Response res = Response.create(h, c)
		Map<String,String> headers = new HashMap<>();
		String uri = req.getUri();
		String contentType = getContentTypeForUri(uri);
		if (contentType==null) contentType = "text/html";
		
		if ("text/html".equals(contentType)) 
			contentType = "text/html; charset=utf-8";
		headers.put("ContentType", contentType);
		byte[] content = loadContent(req);
		
		Response response = Response.create(headers, content);
		return response;
		
	}

	private byte[] loadContent(Request req) {
		String uri = req.getUri();
		String path = PluginEnvirement.INSTANCE.getWebRootPath()+uri;
//		String path = "."+uri;
		if (FileKit.existsAndIsFile(path)){
			byte[] content = FileKit.file2Bytes(path);
			return content;
		}else{
			return ("File not found,file="+uri).getBytes();
		}
	}
	
	public static void main(String[] args) {
		
		ConentManagerImpl o = new ConentManagerImpl();
		Response res = o.handleRequest(Request.create("/src/net/jplugin/ext/staticweb/Plugin.java"));
		
		System.out.println(new String(res.getContentBytes()));
		System.out.println("headers = "+res.getHeaders());
		res = o.handleRequest(Request.create("/src/net/jplugin/ext/staticweb/Plugin22.aa"));
		System.out.println(new String(res.getContentBytes()));
		System.out.println("headers = "+res.getHeaders());
	}

}
