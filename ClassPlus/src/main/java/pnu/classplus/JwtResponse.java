package pnu.classplus;

import lombok.Getter;

@Getter
public class JwtResponse extends ApiResponse {
    private String token;
    private String type = "Bearer";

    public JwtResponse(int resultCode, String resultMessage, String accessToken) {
        super(resultCode, resultMessage);
        this.token = accessToken;
    }
}