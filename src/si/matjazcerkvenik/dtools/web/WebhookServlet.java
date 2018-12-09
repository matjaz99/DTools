package si.matjazcerkvenik.dtools.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import si.matjazcerkvenik.dtools.context.DToolsContext;

public class WebhookServlet extends HttpServlet {

	private static final long serialVersionUID = 4274913262329715396L;

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
		
		DToolsContext.getInstance().getLogger().info("doGet(): getRemotePort: " + req.getScheme());
		DToolsContext.getInstance().getLogger().info("doGet(): getRemoteUser: " + req.getServerName());
		DToolsContext.getInstance().getLogger().info("doGet(): getRequestedSessionId: " + req.getServerPort());
		DToolsContext.getInstance().getLogger().info("doGet(): getRequestURI: " + req.getServletPath());
		
		Map<String, String[]> parameterMap = req.getParameterMap();
		String params = "";
		for (Iterator<String> it = parameterMap.keySet().iterator(); it.hasNext();) {
			String s = it.next();
			params += s + "=" + parameterMap.get(s) + ", ";
		}
		DToolsContext.getInstance().getLogger().info("doGet(): parameterMap: " + params);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		String name = req.getParameter("name");

		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getAuthType());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getCharacterEncoding());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getContentLength());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getContentType());
		DToolsContext.getInstance().getLogger().info("doPost(): getAuthType: " + req.getContextPath());
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
		
		String body = "";
		String s = req.getReader().readLine();
		while (s != null) {
			body += s;
			s = req.getReader().readLine();
		}
		
		DToolsContext.getInstance().getLogger().info("doPost(): body: " + body);

	}

}
