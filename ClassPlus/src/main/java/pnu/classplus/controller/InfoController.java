package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'response successful'}"))
        }),
    })
    @GetMapping("/univ")
    public ResponseEntity getUniversityInfo() {
        return service.getUniversityInfo();
    }


    @Parameter(name="univ_idx", description="대학교 코드", schema=@Schema(type="integer"))
    @Operation(summary="학과 정보 불러오기", description="학과 코드, 이름을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
                @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'response successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '31', 'resultMessage' : 'invalid univ_idx parameter'}"))
        }),
    })
    @GetMapping("/dept")
    public ResponseEntity getDepartmentInfo(@RequestParam("univ_idx") final String univ_idx) {
        final long univCode = Long.parseLong(univ_idx);
        return service.getDepartmentInfo(univCode);
    }
}

