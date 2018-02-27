package ua.yaroslav.auth2.resserver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ResourceServer {

    @GetMapping(value = {"/private"})
    public String getPrivateData(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "token", required = false) String token) throws IOException {
        System.out.println("Auth Header -> " + request.getHeader("Authorization"));
        Enumeration headerNames = request.getHeaderNames();
        StringBuilder builder = new StringBuilder();
        if (request.getHeader("Authorization").length() > 8)
            builder.append("<h3>Access granted!</h3>");
        else builder.append("<h3>Access not granted!</h3>\n");
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if (value.length() < 60)
                builder.append(key + " -> [" + value + "]").append("<br>\n");
            else builder.append(key + " -> [" + value.substring(0, 110) + "...]").append("<br>\n");
        }
        return builder.toString();
    }

}