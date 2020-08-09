package pnu.classplus;

import lombok.Getter;

@Getter
public class DataResponse extends ApiResponse {
    private Object data;

    public DataResponse(int resultCode, String resultMessage, Object data) {
        super(resultCode, resultMessage);
        this.data = data;
    }
}