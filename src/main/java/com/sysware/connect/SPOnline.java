// License : Apache License Version 2.0  https://www.apache.org/licenses/LICENSE-2.0
package com.sysware.connect;

import com.peterswing.CommonLib;
import com.sysware.entity.vo.LoginInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Peter <mcheung63@hotmail.com>
 */
@SuppressWarnings("Duplicates")
public class SPOnline {

	final static Logger logger = Logger.getLogger(SPOnline.class);

	static {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");
	}
    Map<String,LoginInfo > tokens = new HashMap<>();

	@Value("expireTime")
    Long expireTime;

	public static Pair<String, String> login(String username, String password, String domain) {
		username = StringEscapeUtils.escapeXml11(username);
		password = StringEscapeUtils.escapeXml11(password);
		Pair<String, String> result;
		String token;
		try {
			token = requestToken(domain, username, password);
			if (token == null) {
				return null;
			}
			result = submitToken(domain, token);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String requestToken(String domain, String username, String password) throws XPathExpressionException, SAXException, ParserConfigurationException, IOException {
		String saml = generateSAML(domain, username, password);
		String sts = "https://login.microsoftonline.com/extSTS.srf";
		URL u = new URL(sts);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "text/xml; charset=utf-8");
		OutputStream out = connection.getOutputStream();
		Writer writer = new OutputStreamWriter(out);
		writer.write(saml);

		writer.flush();
		writer.close();

		InputStream in = connection.getInputStream();
		int c;
		StringBuilder sb = new StringBuilder("");
		while ((c = in.read()) != -1) {
			sb.append((char) (c));
		}
		in.close();
		String result = sb.toString();
		String token = extractToken(result);
		if (token == null || token.equals("")) {
			logger.error("Login failed : " + CommonLib.prettyFormatXml(result, 4));
			return null;
		}
		return token;
	}

	private static String generateSAML(String domain, String username, String password) {
		String reqXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n"
				+ "   <s:Header>\n"
				+ "      <a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>\n"
				+ "      <a:ReplyTo>\n"
				+ "         <a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>\n"
				+ "      </a:ReplyTo>\n"
				+ "      <a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To>\n"
				+ "      <o:Security xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" s:mustUnderstand=\"1\">\n"
				+ "         <o:UsernameToken>\n"
				+ "            <o:Username>[[username]]</o:Username>\n"
				+ "            <o:Password>[[password]]</o:Password>\n"
				+ "         </o:UsernameToken>\n"
				+ "      </o:Security>\n"
				+ "   </s:Header>\n"
				+ "   <s:Body>\n"
				+ "      <t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">\n"
				+ "         <wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">\n"
				+ "            <a:EndpointReference>\n"
				+ "               <a:Address>[[endpoint]]</a:Address>\n"
				+ "            </a:EndpointReference>\n"
				+ "         </wsp:AppliesTo>\n"
				+ "         <t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType>\n"
				+ "         <t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType>\n"
				+ "         <t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType>\n"
				+ "      </t:RequestSecurityToken>\n"
				+ "   </s:Body>\n"
				+ "</s:Envelope>";
		String saml = reqXML.replace("[[username]]", username);
		saml = saml.replace("[[password]]", password);
		saml = saml.replace("[[endpoint]]", String.format("https://%s.sharepoint.com/_forms/default.aspx?wa=wsignin1.0", domain));
		return saml;
	}

	private static String extractToken(String result) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new InputSource(new StringReader(result)));
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		String token = xp.evaluate("//BinarySecurityToken/text()", document.getDocumentElement());
		return token;
	}

	private static Pair<String, String> submitToken(String domain, String token) throws IOException {
		String loginContextPath = "/_forms/default.aspx?wa=wsignin1.0";
		String url = String.format("https://%s.sharepoint.com%s", domain, loginContextPath);
		CookieHandler.setDefault(null);
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		HttpURLConnection connection = (HttpURLConnection) uc;
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Accept", "application/x-www-form-urlencoded");
		connection.addRequestProperty("Content-Type", "text/xml; charset=utf-8");
		connection.setInstanceFollowRedirects(false);
		OutputStream out = connection.getOutputStream();
		Writer writer = new OutputStreamWriter(out);
		writer.write(token);
		writer.flush();
		out.flush();
		writer.close();
		out.close();

		String rtFa = null;
		String fedAuth = null;
		Map<String, List<String>> headerFields = connection.getHeaderFields();
		List<String> cookiesHeader = headerFields.get("Set-Cookie");
		if (cookiesHeader != null) {
			for (String cookie : cookiesHeader) {
				if (cookie.startsWith("rtFa=")) {
					rtFa = "rtFa=" + HttpCookie.parse(cookie).get(0).getValue();
				} else if (cookie.startsWith("FedAuth=")) {
					fedAuth = "FedAuth=" + HttpCookie.parse(cookie).get(0).getValue();
				} else {
					//logger.info("waste=" + HttpCookie.parse(cookie).get(0).getValue());
				}
			}
		}
		Pair<String, String> result = ImmutablePair.of(rtFa, fedAuth);
		return result;
	}

	public static String contextinfo(Pair<String, String> token, String domain) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost getRequest = new HttpPost("https://" + domain + ".sharepoint.com/_api/contextinfo");
			getRequest.addHeader("Cookie", token.getLeft() + ";" + token.getRight());
			getRequest.addHeader("accept", "application/json;odata=verbose");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				return IOUtils.toString(response.getEntity().getContent(), "utf-8");
			} else {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException ex) {
				Logger.getLogger(SPOnline.class).error(ex);
			}
		}
		return null;
	}

	public static String get(Pair<String, String> token, String url) {
//		return get(token, url, false);
//	}
//
//	public static String get(Pair<String, String> token, String url, boolean download) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("Cookie", token.getLeft() + ";" + token.getRight());
			getRequest.addHeader("accept", "application/json;odata=verbose");
//			if (download) {
//				getRequest.addHeader("binaryStringResponseBody", "true");
//			}
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 200) {
				return IOUtils.toString(response.getEntity().getContent(), "utf-8");
			} else {
				System.err.println("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + ", " + url);
				return null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException ex) {
				Logger.getLogger(SPOnline.class).error(ex);
			}
		}
		return null;
	}

	public static String get(Pair<String, String> token, String domain, String path) {
		return get(token, "https://" + domain + ".sharepoint.com/" + path);
	}

//	public static String download(Pair<String, String> token, String domain, String path) {
//		return get(token, "https://" + domain + ".sharepoint.com/" + path, true);
//	}
	public static String post(Pair<String, String> token, String domain, String path, String data, String formDigestValue) {
		return post(token, domain, path, data, formDigestValue, false);
	}

	public static String post(Pair<String, String> token, String domain, String path, String data, String formDigestValue, boolean isXHttpMerge) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost postRequest = new HttpPost("https://" + domain + ".sharepoint.com/" + path);
			postRequest.addHeader("Cookie", token.getLeft() + ";" + token.getRight());
			System.out.println(token.getLeft() + ";" + token.getRight());
			postRequest.addHeader("accept", "application/json;odata=verbose");
			postRequest.addHeader("content-type", "application/json;odata=verbose");
			postRequest.addHeader("X-RequestDigest", formDigestValue);
			postRequest.addHeader("IF-MATCH", "*");
			if (isXHttpMerge) {
				postRequest.addHeader("X-HTTP-Method", "MERGE");
			}

			List<NameValuePair> nvps = new ArrayList<>();
			if (data != null) {
				StringEntity input = new StringEntity(data, "UTF-8");
				input.setContentType("application/json");
				postRequest.setEntity(input);
			}

			HttpResponse response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
				logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + ", " + path);
			}
			if (response.getEntity() == null || response.getEntity().getContent() == null) {
				return null;
			} else {
				return IOUtils.toString(response.getEntity().getContent(), "utf-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException ex) {
				Logger.getLogger(SPOnline.class).error(ex);
			}
		}
		return null;
	}

	/**
	 *  上传文件
	 * @param token   SharePoint 登陆令牌
	 * @param domain  域名
	 * @param path	   请求路径
	 * @param file	   上传的附件
	 * @param formDigestValue  SharePoint请求验证
	 * @return  SharePoint 请求返回数据
	 */
	public static String uploadFile(Pair<String, String> token, String domain, String path, File file, String formDigestValue) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost postRequest = new HttpPost("https://" + domain + ".sharepoint.com/" + path);
			postRequest.addHeader("Cookie", token.getLeft() + ";" + token.getRight());
			System.out.println(token.getLeft() + ";" + token.getRight());
			postRequest.addHeader("accept", "application/json;odata=verbose");
		//	postRequest.addHeader("content-type", "application/json;odata=verbose");
			postRequest.addHeader("X-RequestDigest", formDigestValue);

			List<NameValuePair> nvps = new ArrayList<>();
			if (file != null) {
				HttpEntity httpEntity = new FileEntity(file);
				postRequest.setEntity(httpEntity);
			}

			HttpResponse response = httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
				logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode() + ", " + path);
			}
			if (response.getEntity() == null || response.getEntity().getContent() == null) {
				return null;
			} else {
				return IOUtils.toString(response.getEntity().getContent(), "utf-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException ex) {
				Logger.getLogger(SPOnline.class).error(ex);
			}
		}
		return null;
	}

	public static String delete(Pair<String, String> token, String domain, String path, String formDigestValue) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpDelete deleteRequest = new HttpDelete("https://" + domain + ".sharepoint.com/" + path);
			deleteRequest.addHeader("Cookie", token.getLeft() + ";" + token.getRight());
			deleteRequest.addHeader("accept", "application/json;odata=verbose");
			deleteRequest.addHeader("content-type", "application/json;odata=verbose");
			deleteRequest.addHeader("X-RequestDigest", formDigestValue);
			deleteRequest.addHeader("IF-MATCH", "*");
			HttpResponse response = httpClient.execute(deleteRequest);

			if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 204) {
				logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
			if (response.getEntity() == null || response.getEntity().getContent() == null) {
				return null;
			} else {
				return IOUtils.toString(response.getEntity().getContent(), "utf-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException ex) {
				Logger.getLogger(SPOnline.class).error(ex);
			}
		}
		return null;
	}

	public static String escapeSharePointUrl(String path) {
		return path.replaceAll(" ", "%20");
	}

    /**
     * 获取登陆令牌
     * @param username  用户名
     * @param password  密码
     * @param domain   域
     * @return
     */
	public Pair<String, String> getToken(String username,String password,String domain){
        Pair<String, String> token = null;
        long currentTime = System.currentTimeMillis();
        LoginInfo loginInfo = tokens.get(username);

        boolean logout = true;
        if(loginInfo != null){
            Long loginTime = loginInfo.getLoginTime();

            Long time = currentTime - loginTime ;

            if(time < expireTime){
                token = loginInfo.getToken();
                logout = false;
            }

        }

        token = this.login(username, password, domain);

        if(logout){
            LoginInfo info = new LoginInfo();
            info.setLoginTime(currentTime);
            info.setToken(token);
            tokens.put(username,loginInfo);
        }
        return token;
    }




}
