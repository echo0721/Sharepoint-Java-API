package hk.quantr.sharepoint;

import java.util.*;
import com.microsoft.azure.serverless.functions.annotation.*;
import com.microsoft.azure.serverless.functions.*;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
public class AzureFunction {

	@FunctionName("getToken")
	public HttpResponseMessage<String> getToken(@HttpTrigger(name = "req", methods = {"get", "post"}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request, final ExecutionContext context) {
		context.getLogger().info("hk.quantr.sharepoint::AzureFunction::getToken()");
		String domain = getParameter(request, "domain");
		String username = getParameter(request, "username");
		String password = getParameter(request, "password");

		if (domain == null || username == null || password == null) {
			return request.createResponse(400, "You need to pass : domain, username and password");
		} else {
			Pair<String, String> token = SPOnline.login(username, password, domain);
			return request.createResponse(200, token.getLeft() + "\n" + token.getRight());
		}
	}

	String getParameter(HttpRequestMessage<Optional<String>> request, String name) {
		String query = request.getQueryParameters().get(name);
		String para = request.getBody().orElse(query);
		return para;
	}
}
