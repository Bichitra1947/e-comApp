package e_com.bichitra.e_com02092024.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private List<String> roles;
    private ResponseCookie responseCookie;

    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(Long id, String username, List<String> roles, ResponseCookie responseCookie) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.responseCookie = responseCookie;
    }

    public UserInfoResponse(Long id, String jwtToken, String username, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }
}
