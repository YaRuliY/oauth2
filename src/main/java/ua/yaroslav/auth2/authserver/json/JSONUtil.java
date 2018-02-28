package ua.yaroslav.auth2.authserver.json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;
import ua.yaroslav.auth2.authserver.dto.AuthRequest;
import ua.yaroslav.auth2.authserver.json.entity.AuthCode;
import ua.yaroslav.auth2.authserver.json.entity.TokenAccess;
import ua.yaroslav.auth2.authserver.json.entity.TokenRefresh;
import ua.yaroslav.auth2.store.InMemoryStore;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Component
public class JSONUtil {
    private final ObjectMapper mapper;
    private final InMemoryStore store;

    public JSONUtil(InMemoryStore store){
        this.store = store;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public AuthCode getCode(AuthRequest authRequest){
        AuthCode code = new AuthCode(authRequest.getClientID(), authRequest.getUsername(),new Date().getTime() + 15000);
        return code;
    }

    public TokenAccess getAccessToken(String clientID, String username , String scope){
        if (scope == "") scope = "grant_all";
        TokenAccess access = new TokenAccess(clientID, username, new Date().getTime() + 60000,
                scope, "bearer", store.getTokens().size());
        access.setTokenID(store.getTokens().size());
        return access;
    }

    public AuthCode readCodeFromB64(String code) throws IOException {
        String s = new String(Base64.getDecoder().decode(code.getBytes()));
        return mapper.readValue(s, AuthCode.class);
    }

    public TokenAccess readTokenFromB64(String token) throws IOException {
        return mapper.readValue(new String(Base64.getDecoder().decode(token.getBytes())), TokenAccess.class);
    }

    public String objectToString(Object code){
        try {
            return mapper.writeValueAsString(code);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encodeObject(Object code){
        String s = objectToString(code);
        return Base64.getEncoder().encodeToString(s.getBytes());
    }

    public TokenRefresh getRefreshToken(String clientID, String username, int ath){
        TokenRefresh refresh = new TokenRefresh(clientID, username, new Date().getTime() + 60000 * 30);
        refresh.setAccessTokenID(ath);
        return refresh;
    }

    public TokenRefresh readRefreshTokenFromB64(String token) throws IOException {
        return mapper.readValue(new String(Base64.getDecoder().decode(token.getBytes())), TokenRefresh.class);
    }
}