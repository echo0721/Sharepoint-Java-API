package hk.quantr.sharepoint;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Peter <peter@quantr.hk>
 */
@Controller
@EnableAutoConfiguration
public class SpringController {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringController.class, args);
	}

	@RequestMapping("/")
	@ResponseBody
	String getToken() {
		try {
			Properties prop = new Properties();
			prop.load(SPOnline.class.getClassLoader().getResourceAsStream("messages_en_US.properties"));
			return "version " + prop.getProperty("version") + ", build date " + prop.getProperty("build.date");
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(SPOnline.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	@RequestMapping("/getToken")
	@ResponseBody
	String getToken(String domain, String username, String password) {
		if (domain == null || username == null || password == null) {
			return "You need to pass : domain, username and password";
		} else {
			Pair<String, String> token = SPOnline.login(username, password, domain);
			if (token == null) {
				return "Authentication failed " + domain + ", " + username + ", " + password;
			} else {
				return token.getLeft() + "\n" + token.getRight();
			}
		}
	}

}
