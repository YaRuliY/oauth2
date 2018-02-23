package ua.yaroslav.auth2.authserver;
import org.springframework.web.bind.annotation.*;
import ua.yaroslav.auth2.datastore.Database;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@RestController
public class AuthServer {
    private final Database database;
    private final String CLIENT_ID = "client";
    private final String CLIENT_SECRET = "secret";

    public AuthServer(Database database) { this.database = database; }

    @PostMapping("/auth")
    public void getCode(HttpServletRequest request,//todo change to requestBody
                          HttpServletResponse response,
                          @RequestParam(value="client_id") String client_id,
                          @RequestParam(value="redirect_uri") String redirect_uri,
                          @RequestParam(value="response_type") String response_type,
                          @RequestParam(value="user_login") String user_login,
                          @RequestParam(value="user_pass") String user_pass) throws IOException, ServletException {
        System.out.println(client_id);
        System.out.println(redirect_uri);
        System.out.println(response_type);
        System.out.println(user_login);
        if (client_id.equals(CLIENT_ID)){
            if (response_type.equals("code")){
                String authCode = Base64.getEncoder().encodeToString(user_login.getBytes());
                database.addAuthCode(authCode);
                response.sendRedirect(redirect_uri + "?authorization_code=" + authCode);
            }
        }
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public String getToken(@RequestParam(value="client_id") String client_id,
                           @RequestParam(value="client_secret") String client_secret,
                           @RequestParam(value="grand_type") String grand_type,
                           @RequestParam(value="code") String code){
        System.out.println(client_id);
        System.out.println(client_secret);
        System.out.println(grand_type);
        System.out.println(code);
        switch (grand_type){
            case "authorization_code" : {
                if (database.isValidAuthCode(code))
                    return "{\n" +
                            "  token_type: \"bearer\",\n" +
                            "  access_token: \"ACCESS_TOKEN\",\n" +
                            "  refresh_token: \"REFRESH_TOKEN\",\n" +
                            "  expires_in: 3600\n" +
                            "}";
            }
            case "refresh_token": {

            }
            default:{
                return "{\"error\": \"invalid_grant_type\"}";
            }
        }
    }

    @RequestMapping("/")
    public String getHome(){ return "<h3>Hell Yeah Server</h3>"; }
}