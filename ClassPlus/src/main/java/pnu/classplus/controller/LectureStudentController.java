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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pnu.classplus.dto.LectureScriptDto;
import pnu.classplus.service.LectureService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Tag(name="학생 - 강의 관련 기능")
@PreAuthorize("hasRole('STUDENT')")
@RestController
@RequestMapping("/student/*")
public class LectureStudentController {

    @Autowired
    private LectureService service;

    @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true)
    @Operation(summary="강의 등록")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture register successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '34', 'resultMessage' : 'the member is not student'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student already registered this lecture'}"))
        })
    })
    @PostMapping("/lecture")
    public ResponseEntity registerLecture(HttpServletRequest request, @Parameter(hidden=true) @RequestBody Map<String, Object> param) {
        Long lec_details_idx = ((Number) param.get("lec_details_idx")).longValue();
        return service.registerLecture(request, lec_details_idx);
    }


    @Parameters({
        @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="week", description="주차 수 (몇 번째 주 수업인지)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="1 (1주 2회차 수업일 때)"),
        @Parameter(name="num_per_week", description="강의 회차 (1주 1회차, 1주 2회차, 2주 1회차...)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="2 (1주 2회차 수업일 때)"),
        @Parameter(name="summary", description="강의 개요 (400byte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="script", description="강의 스크립트 (64Kbyte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="feedback", description="피드백 (5000byte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    })
    @Operation(summary="강의 스크립트 추가")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture register successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '34', 'resultMessage' : 'the member is not student'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student already registered this lecture'}"))
        })
    })
    @PostMapping("/script")
    public ResponseEntity addLectureScript(HttpServletRequest request, @Parameter(hidden=true) @RequestBody LectureScriptDto lectureScriptDto) {
        return service.addLectureScript(request, lectureScriptDto);
    }
}