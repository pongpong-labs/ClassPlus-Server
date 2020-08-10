package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.classplus.dto.LectureDetailsDto;
import pnu.classplus.service.LectureService;

@Tag(name="관리자 - 강의 관련 기능")
@RestController
@RequestMapping("/admin2/*")    /* 기능 테스트 위해 인증 안 거치는 path로 설정함. 추후 수정해야 함. */
public class LectureAdminController {

    @Autowired
    private LectureService service;

    @Parameters({
        @Parameter(name="lec_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="prof_mem_idx", description="교수 멤버 코드", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="section", description="분반", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="week", description="주차 수 (몇 주 수업인지)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="16"),
        @Parameter(name="count_per_week", description="주 당 강의 횟수", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="2"),
        @Parameter(name="start_time", description="수업 시작 시간(날짜 의미 없음, 시간만 의미 있음)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true, example="2020-08-01T10:30"),
        @Parameter(name="end_time", description="수업 종료 시간(날짜 의미 없음, 시간만 의미 있음)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true, example="2020-08-10T11:45")
    })
    @Operation(summary="강의 추가", description="강의 개설 작업")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture add successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member code'}\n" +
                        "{'resultCode' : '34', 'resultMessage' : 'the member is not professor'}"))
        })
    })
    @PostMapping("/lecture")
    public ResponseEntity addLectureDetails(@RequestBody LectureDetailsDto lectureDetailsDto) {
        System.out.println(lectureDetailsDto);
        return service.addLectureDetails(lectureDetailsDto);
    }
}
