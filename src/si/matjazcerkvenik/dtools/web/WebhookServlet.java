package si.matjazcerkvenik.dtools.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import si.matjazcerkvenik.dtools.context.DToolsContext;

public class WebhookServlet extends HttpServlet {

	private static final long serialVersionUID = 4274913262329715396L;
	
	public static List<WebhookMessage> messages = new LinkedList<WebhookMessage>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		DToolsContext.getInstance().getLogger().info("doGet(): getAuthType: " + req.getAuthType());
		DToolsContext.getInstance().getLogger().info("doGet(): getCharacterEncoding: " + req.getCharacterEncoding());
		DToolsContext.getInstance().getLogger().info("doGet(): getContentLength: " + req.getContentLength());
		DToolsContext.getInstance().getLogger().info("doGet(): getContentType: " + req.getContentType());
		DToolsContext.getInstance().getLogger().info("doGet(): getContextPath: " + req.getContextPath());
		DToolsContext.getInstance().getLogger().info("doGet(): getLocalAddr: " + req.getLocalAddr());
		DToolsContext.getInstance().getLogger().info("doGet(): getLocalName: " + req.getLocalName());
		DToolsContext.getInstance().getLogger().info("doGet(): getLocalPort: " + req.getLocalPort());
		DToolsContext.getInstance().getLogger().info("doGet(): getMethod: " + req.getMethod());
		DToolsContext.getInstance().getLogger().info("doGet(): getParameter: " + req.getParameter("aaa"));
		DToolsContext.getInstance().getLogger().info("doGet(): getPathInfo: " + req.getPathInfo());
		DToolsContext.getInstance().getLogger().info("doGet(): getPathTranslated: " + req.getPathTranslated());
		DToolsContext.getInstance().getLogger().info("doGet(): getProtocol: " + req.getProtocol());
		DToolsContext.getInstance().getLogger().info("doGet(): getQueryString: " + req.getQueryString());
		DToolsContext.getInstance().getLogger().info("doGet(): getRemoteAddr: " + req.getRemoteAddr());
		DToolsContext.getInstance().getLogger().info("doGet(): getRemoteHost: " + req.getRemoteHost());
		DToolsContext.getInstance().getLogger().info("doGet(): getRemotePort: " + req.getRemotePort());
		DToolsContext.getInstance().getLogger().info("doGet(): getRemoteUser: " + req.getRemoteUser());
		DToolsContext.getInstance().getLogger().info("doGet(): getRequestedSessionId: " + req.getRequestedSessionId());
		DToolsContext.getInstance().getLogger().info("doGet(): getRequestURI: " + req.getRequestURI());
		DToolsContext.getInstance().getLogger().info("doGet(): getScheme: " + req.getScheme());
		DToolsContext.getInstance().getLogger().info("doGet(): getServerName: " + req.getServerName());
		DToolsContext.getInstance().getLogger().info("doGet(): getServerPort: " + req.getServerPort());
		DToolsContext.getInstance().getLogger().info("doGet(): getServletPath: " + req.getServletPath());
		
		DToolsContext.getInstance().getLogger().info("doGet(): parameterMap: " + getReqParams(req));
		DToolsContext.getInstance().getLogger().info("doGet(): headers: " + getReqHeaders(req));

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getAuthType());
		DToolsContext.getInstance().getLogger().info("doPost(): getCharacterEncoding: " + req.getCharacterEncoding());
		DToolsContext.getInstance().getLogger().info("doPost(): getContentLength: " + req.getContentLength());
		DToolsContext.getInstance().getLogger().info("doPost(): getContentType: " + req.getContentType());
		DToolsContext.getInstance().getLogger().info("doPost(): getContextPath: " + req.getContextPath());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getLocalAddr());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getLocalName());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getLocalPort());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getMethod());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getParameter("aaa"));
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getPathInfo());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getPathTranslated());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getProtocol());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getQueryString());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRemoteAddr());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRemoteHost());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRemotePort());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRemoteUser());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRequestedSessionId());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getRequestURI());
		DToolsContext.getInstance().getLogger().info("doPost(): getScheme: " + req.getScheme());
		DToolsContext.getInstance().getLogger().info("doPost(): getServerName: " + req.getServerName());
		DToolsContext.getInstance().getLogger().info("doPost(): getServerPort: " + req.getServerPort());
		DToolsContext.getInstance().getLogger().info("doPost(): getServletPath: " + req.getServletPath());
		
		String body = getReqBody(req);
		DToolsContext.getInstance().getLogger().info("doGet(): parameterMap: " + getReqParams(req));
		DToolsContext.getInstance().getLogger().info("doPost(): body: " + body);
		DToolsContext.getInstance().getLogger().info("doPost(): headers: " + getReqHeaders(req));
		
		WebhookMessage m = new WebhookMessage();
		m.setTimestamp(System.currentTimeMillis());
		m.setContentLength(req.getContentLength());
		m.setContentType(req.getContentType());
		m.setMethod(req.getMethod());
		m.setPathInfo(req.getPathInfo());
		m.setProtocol(req.getProtocol());
		m.setRemoteHost(req.getRemoteHost());
		m.setRemotePort(req.getRemotePort());
		m.setRequestUri(req.getRequestURI());
		m.setBody(body);
		m.setHeaderMap(generateHeaderMap(req));
		m.setParameterMap(generateParamMap(req));
		
		messages.add(m);

	}
	
	
	private Map<String, String> generateHeaderMap(HttpServletRequest req) {
		
		Map<String, String> m = new HashMap<String, String>();
		
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String val = req.getHeader(key);
			m.put(key, val);
		}
		return m;
	}
	
	private Map<String, String> generateParamMap(HttpServletRequest req) {
		
		Map<String, String> m = new HashMap<String, String>();
		Map<String, String[]> parameterMap = req.getParameterMap();
		
		for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();) {
			String s = it.next();
			m.put(s, parameterMap.get(s)[0]);
		}
		return m;
	}
	
	
	private String getReqHeaders(HttpServletRequest req) {
		
		String headers = "";
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			//headerNames.toString();
			String val = req.getHeader(key);
			headers += key + "=" + val + ", ";
		}
		return headers;
		
	}
	
	private String getReqParams(HttpServletRequest req) {
		Map<String, String[]> parameterMap = req.getParameterMap();
		String params = "";
		for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();) {
			String s = it.next();
			params += s + "=" + parameterMap.get(s)[0] + ", ";
		}
		return params;
	}
	
	private String getReqBody(HttpServletRequest req) throws IOException {
		
		String body = "";
		String s = req.getReader().readLine();
		while (s != null) {
			body += s;
			s = req.getReader().readLine();
		}
		
		return body;
		
	}

}
