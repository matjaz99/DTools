package si.matjazcerkvenik.dtools.web.servlets;

import java.io.IOException;
import java.util.ArrayList;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import si.matjazcerkvenik.dtools.context.DMetrics;
import si.matjazcerkvenik.dtools.context.DToolsContext;
import si.matjazcerkvenik.dtools.io.md5.MD5Checksum;
import si.matjazcerkvenik.dtools.web.webhook.Alert;
import si.matjazcerkvenik.dtools.web.webhook.AmAlertMessage;
import si.matjazcerkvenik.dtools.web.webhook.DNotification;
import si.matjazcerkvenik.dtools.web.webhook.HttpMessage;

public class WebhookServlet extends HttpServlet {

	private static final long serialVersionUID = 4274913262329715396L;
	
	public static List<HttpMessage> messages = new LinkedList<HttpMessage>();
	public static List<AmAlertMessage> amMessages = new LinkedList<AmAlertMessage>();
	public static List<DNotification> dNotifs = new LinkedList<DNotification>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// $ curl http://localhost:8080/DTools/api/webhook/blablabla
		// $ curl 'http://localhost:8080/DTools/api/webhook/blablabla?a=1&b=2'

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
		
		HttpMessage m = new HttpMessage();
		m.setTimestamp(System.currentTimeMillis());
		m.setContentLength(req.getContentLength());
		m.setContentType(req.getContentType());
		m.setMethod(req.getMethod());
		m.setPathInfo(req.getPathInfo());
		m.setProtocol(req.getProtocol());
		m.setRemoteHost(req.getRemoteHost());
		m.setRemotePort(req.getRemotePort());
		m.setRequestUri(req.getRequestURI());
		
		m.setBody(req.getPathInfo() + " " + generateParamMap(req));
		m.setHeaderMap(generateHeaderMap(req));
		m.setParameterMap(generateParamMap(req));
		
		messages.add(m);
		
		DMetrics.dtools_webhook_messages_received_total.labels(m.getRemoteHost(), "get").inc();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getAuthType());
		DToolsContext.getInstance().getLogger().info("doPost(): getCharacterEncoding: " + req.getCharacterEncoding());
		DToolsContext.getInstance().getLogger().info("doPost(): getContentLength: " + req.getContentLength());
		DToolsContext.getInstance().getLogger().info("doPost(): getContentType: " + req.getContentType());
		DToolsContext.getInstance().getLogger().info("doPost(): getContextPath: " + req.getContextPath());
		DToolsContext.getInstance().getLogger().info("doPost(): getLocalAddr: " + req.getLocalAddr());
		DToolsContext.getInstance().getLogger().info("doPost(): getLocalName: " + req.getLocalName());
		DToolsContext.getInstance().getLogger().info("doPost(): getLocalPort: " + req.getLocalPort());
		DToolsContext.getInstance().getLogger().info("doPost(): getMethod: " + req.getMethod());
		DToolsContext.getInstance().getLogger().info("doPost(): getParameter: " + req.getParameter("aaa"));
		DToolsContext.getInstance().getLogger().info("doPost(): getPathInfo: " + req.getPathInfo());
		DToolsContext.getInstance().getLogger().info("doPost(): getPathTranslated: " + req.getPathTranslated());
		DToolsContext.getInstance().getLogger().info("doPost(): getProtocol: " + req.getProtocol());
		DToolsContext.getInstance().getLogger().info("doPost(): getQueryString: " + req.getQueryString());
		DToolsContext.getInstance().getLogger().info("doPost(): getRemoteAddr: " + req.getRemoteAddr());
		DToolsContext.getInstance().getLogger().info("doPost(): getRemoteHost: " + req.getRemoteHost());
		DToolsContext.getInstance().getLogger().info("doPost(): getRemotePort: " + req.getRemotePort());
		DToolsContext.getInstance().getLogger().info("doPost(): getRemoteUser: " + req.getRemoteUser());
		DToolsContext.getInstance().getLogger().info("doPost(): getRequestedSessionId: " + req.getRequestedSessionId());
		DToolsContext.getInstance().getLogger().info("doPost(): getRequestURI: " + req.getRequestURI());
		DToolsContext.getInstance().getLogger().info("doPost(): getScheme: " + req.getScheme());
		DToolsContext.getInstance().getLogger().info("doPost(): getServerName: " + req.getServerName());
		DToolsContext.getInstance().getLogger().info("doPost(): getServerPort: " + req.getServerPort());
		DToolsContext.getInstance().getLogger().info("doPost(): getServletPath: " + req.getServletPath());
		
		String body = getReqBody(req);
		DToolsContext.getInstance().getLogger().info("doGet(): parameterMap: " + getReqParams(req));
		DToolsContext.getInstance().getLogger().info("doPost(): body: " + body);
		DToolsContext.getInstance().getLogger().info("doPost(): headers: " + getReqHeaders(req));
		
		HttpMessage m = new HttpMessage();
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
		
		// TODO how to detect a format of message of the application who send the message so it can be properly parsed?
		
		// headers: host=172.30.19.6:8080, user-agent=Alertmanager/0.15.3, content-length=1889, content-type=application/json, 
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		AmAlertMessage am = gson.fromJson(m.getBody(), AmAlertMessage.class);
		System.out.println(am.toString());
		System.out.println("Number of alerts: " + am.getAlerts().size());
		amMessages.add(am);
		
		dNotifs.addAll(convertToDNotif(m, am));
		
		
		DMetrics.dtools_webhook_messages_received_total.labels(m.getRemoteHost(), "post").inc();
		
		for (Alert a : am.getAlerts()) {
			DMetrics.dtools_am_alerts_received_total.labels(m.getRemoteHost(), a.getLabel("alerttype"), a.getLabel("severity")).inc();
		}

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
	
	private List<DNotification> convertToDNotif(HttpMessage m, AmAlertMessage am) {
		
		List<DNotification> notifs = new ArrayList<DNotification>();
		
		for (Iterator<Alert> it = am.getAlerts().iterator(); it.hasNext();) {
			Alert a = it.next();
			
			DNotification n = new DNotification();
			n.setTimestamp(System.currentTimeMillis());
			n.setSource(m.getRemoteHost());
			n.setAlertname(a.getLabels().get("alertname"));
			
			if (m.getHeaderMap().containsKey("user-agent")) {
				n.setUserAgent(m.getHeaderMap().get("user-agent"));
			} else {
				n.setUserAgent("unknown");
			}

			if (a.getLabels().containsKey("alertdomain")) {
				n.setAlertdomain(a.getLabels().get("alertdomain"));
			} else {
				n.setAlertdomain("unknown");
			}
			
			if (a.getLabels().containsKey("alerttype")) {
				n.setAlerttype(a.getLabels().get("alerttype"));
			} else {
				n.setAlerttype("unknown");
			}
			
			if (a.getLabels().containsKey("instance")) {
				n.setInstance(a.getLabels().get("instance"));
			} else {
				n.setInstance("unknown");
			}
			
			if (a.getLabels().containsKey("severity")) {
				n.setSeverity(a.getLabels().get("severity"));
			} else {
				n.setSeverity("indeterminate");
			}
			
			if (a.getStatus().equalsIgnoreCase("resolved")) {
				n.setSeverity("clear");
			}
			
			if (a.getAnnotations().containsKey("summary")) {
				n.setSummary(a.getAnnotations().get("summary"));
			} else {
				n.setSummary("-");
			}
			
			if (a.getAnnotations().containsKey("description")) {
				n.setDescription(a.getAnnotations().get("description"));
			} else {
				n.setDescription("-");
			}
			
			n.setStatus(a.getStatus());
			n.setUid(MD5Checksum.getMd5Checksum(n.getAlertname() + n.getAlertdomain() 
				+ n.getAlerttype() + n.getInstance() + n.getSummary() + n.getDescription()));
			
//			DNotification found = null;
//			for (Iterator<DNotification> it1 = dNotifs.iterator(); it1.hasNext();) {
//				DNotification dn = it1.next();
//				if (dn.getUid().equalsIgnoreCase(n.getUid())) {
//					found = dn;
//					if (n.getSeverity().equalsIgnoreCase("clear")) {
//						
//					}
//					break;
//				}
//			}
//			if (found == null) {
//				notifs.add(n);
//			}
			notifs.add(n);
			
		}
		
		return notifs;
		
	}

}
