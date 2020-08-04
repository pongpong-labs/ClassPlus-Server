package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pnu.classplus.service.InfoService;

@Tag(name="기본 데이터 관련 기능")
@RestController
@RequestMapping("/info/*")
public class InfoController {

    @Autowired
    private InfoService service;

    @Operation(summary="대학교 정보 불러오기", description="대학교 코드, 이름을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'Join Successful!'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples={
                @ExampleObject(value="{'resultCode' : '11', 'resultMessage' : 'User ID is already registered'}\n" +
                    "{'resultCode' : '12', 'resultMessage' : 'Email is already registered'}\n" +
                    "{'resultCode' : '13', 'resultMessage' : 'Invalid University Code'}\n" +
                    "{'resultCode' : '14', 'resultMessage' : 'Invalid Department Code'}")
            })
        })
    })
    @GetMapping(value="/getUnivInfo", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUniversityInfo() {
        return service.getUniversityInfo();
    }
}
