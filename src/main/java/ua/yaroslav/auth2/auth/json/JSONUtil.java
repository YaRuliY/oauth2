package ua.yaroslav.auth2.auth.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import ua.yaroslav.auth2.auth.dto.AuthRequestDto;
import ua.yaroslav.auth2.auth.entity.AccessToken;
import ua.yaroslav.auth2.auth.entity.AuthCode;
import ua.yaroslav.auth2.auth.entity.RefreshToken;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Component
public class JSONUtil {
    private static ObjectMapper mapper;

    static {
        JSONUtil.mapper = new ObjectMapper();
        JSONUtil.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public JSONUtil() {
    }


    public static AuthCode getCode(AuthRequestDto authRequest) {
        return new AuthCode(authRequest.getClientID(), authRequest.getUsername(), System.currentTimeMillis() + 15*1000*5);
    }

    public static AccessToken getAccessToken(String clientID, String username, String scope) {
        if (StringUtils.isEmpty(scope)) scope = "grant_all";
        return new AccessToken(clientID, username, System.currentTimeMillis() + 60*1000*5, scope, "bearer");
    }

    public static AuthCode readCodeFromB64(String code) throws IOException {
        String s = new String(Base64.getDecoder().decode(code.getBytes()));
        return mapper.readValue(s, AuthCode.class);
    }

    public static AccessToken readTokenFromB64(String token) throws IOException {
        return mapper.readValue(new String(Base64.getDecoder().decode(token.getBytes())), AccessToken.class);
    }

    public static String objectToString(Object code) {
        try {
            return "\n" + mapper.writeValueAsString(code) + "\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeObject(Object code) {
        return Base64.getEncoder().encodeToString(Objects.requireNonNull(objectToString(code)).getBytes());
    }

    public static RefreshToken getRefreshToken(String clientID, String username, String scope) {
        return new RefreshToken(clientID, username, System.currentTimeMillis() + 60000 * 30, scope);
    }

    public static RefreshToken readRefreshTokenFromB64(String token) throws IOException {
        return mapper.readValue(new String(Base64.getDecoder().decode(token.getBytes())), RefreshToken.class);
    }
}