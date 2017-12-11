package hk.quantr.sharepoint;

import java.util.*;
import com.microsoft.azure.serverless.functions.annotation.*;
import com.microsoft.azure.serverless.functions.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class AzureFunction {

	@FunctionName("getToken")
	public HttpResponseMessage<String> getToken(@HttpTrigger(name = "req", methods = {"get"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request, final ExecutionContext context) {
		context.getLogger().info("hk.quantr.sharepoint.AzureFunction::getToken()");
		String domain = getParameter(request, "domain");
		String username = getParameter(request, "username");
		String password = getParameter(request, "password");

		if (domain == null || username == null || password == null) {
			return request.createResponse(400, "You need to pass : domain, username and password");
		} else {
			Pair<String, String> token = SPOnline.login(username, password, domain);
			if (token == null) {
				return request.createResponse(403, "Authentication failed " + domain + ", " + username + ", " + password);
			} else {
				return request.createResponse(200, token.getLeft() + "\n" + token.getRight());
			}
		}
	}

	@FunctionName("version")
	public HttpResponseMessage<String> version(@HttpTrigger(name = "req", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request, final ExecutionContext context) {
		String version = null;
		try {
			InputStream input = SPOnline.class.getClassLoader().getResourceAsStream("messages_en_US.properties");
			Properties prop = new Properties();
			prop.load(input);
			version = prop.getProperty("version");
			input.close();
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(SPOnline.class.getName()).log(Level.SEVERE, null, ex);
		}
		return request.createResponse(200, version);
	}

	String getParameter(HttpRequestMessage<Optional<String>> request, String name) {
		String para = request.getQueryParameters().get(name);
//		String para = request.getBody().orElse(query);
		return para;

//		if (query == null) {
//			return request.getHeaders().get(name);
//		} else {
//			return query;
//		}
	}
}
