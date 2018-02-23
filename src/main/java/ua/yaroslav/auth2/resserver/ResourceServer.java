package ua.yaroslav.auth2.resserver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.yaroslav.auth2.datastore.Database;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ResourceServer{
    private final Database database;

    public ResourceServer(Database database) {
        this.database = database;
    }

    @RequestMapping(value = {"/private"})
    public String getPrivateData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getHeader("Authorization"));
        if(request.getHeader("Authorization") == null) {
            System.out.println("Send redirect ot /oauth");
            response.sendRedirect("/oauth");
        }
        else {
            if(database.isValidToken(request.getHeader("Authorization").substring(6)))
                return "private data for user " + request.getRemoteUser() + "<br>" + request.getHeader("Authorization");
        }
        System.out.println("Header: " + request.getHeader("Authorization"));
        return "Token is invalid";
    }

    @RequestMapping(value = {"/private2"})
    public String getP(){
        return "P2";
    }
}