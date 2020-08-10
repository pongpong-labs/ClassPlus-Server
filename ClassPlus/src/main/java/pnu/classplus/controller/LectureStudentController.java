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
                        "{'resultCode' : '36', 'resultMessage' : 'student already registered this lecture'}\n" +
                        "{'resultCode' : '39', 'resultMessage' : 'student already deleted this lecture'}"))
        })
    })
    @PostMapping("/lecture")
    public ResponseEntity registerLecture(HttpServletRequest request, @Parameter(hidden=true) @RequestBody Map<String, Object> param) {
        Long lec_details_idx = ((Number) param.get("lec_details_idx")).longValue();
        return service.registerLecture(request, lec_details_idx);
    }


    @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true)
    @Operation(summary="강의 삭제")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture delete successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '34', 'resultMessage' : 'the member is not student'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'cannot delete lecture, student not registered this lecture'}\n" +
                        "{'resultCode' : '39', 'resultMessage' : 'student already deleted this lecture'}"))
        })
    })
    @DeleteMapping("/lecture")
    public ResponseEntity deleteLecture(HttpServletRequest request, @Parameter(hidden=true) @RequestBody Map<String, Object> param) {
        Long lec_details_idx = ((Number) param.get("lec_details_idx")).longValue();
        return service.deleteLecture(request, lec_details_idx);
    }


    @Parameters({
        @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="week", description="주차 수 (몇 번째 주 수업인지)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="1 (1주 2회차 수업일 때)"),
        @Parameter(name="num_per_week", description="강의 회차 (1주 1회차, 1주 2회차, 2주 1회차...)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="2 (1주 2회차 수업일 때)"),
        @Parameter(name="summary", description="강의 개요 (400byte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="script", description="강의 스크립트 (64Kbyte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="feedback", description="피드백 (5000byte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    })
    @Operation(summary="강의 스크립트 추가", description = "week, num_per_week 중복 데이터 예외 처리 안 되 있으므로 유의하세요. (에러 코드 38 미처리)")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture register successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student not registered this lecture'}\n" +
                        "{'resultCode' : '38', 'resultMessage' : 'invalid week or num_per_week parameter (duplicated in database)'}"))
        })
    })
    @PostMapping("/script")
    public ResponseEntity addLectureScript(HttpServletRequest request, @Parameter(hidden=true) @RequestBody LectureScriptDto lectureScriptDto) {
        return service.addLectureScript(request, lectureScriptDto);
    }


    @Operation(summary="수강 중인 강의 정보 리스트 불러오기")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{\n 'resultCode' : '0',\n 'resultMessage' : 'registered lecture lists response successful'\n" +
                    " 'data' : [\n" +
                    "  {\n" +
                    "   'lectureDetails' : {\n" +
                    "    'idx' : 4,\n" +
                    "    'section' : 57,\n" +
                    "    'week' : 16,\n" +
                    "    'count_per_week' : 2\n" +
                    "    'start_time' : '2020-08-01T10:30:00'\n" +
                    "    'end_time' : '2020-08-01T11:45:00'\n" +
                    "    'professor' : {\n" +
                    "     'idx' : 10\n" +
                    "     'uid' : 'pf_hschoi'\n" +
                    "     'name' : '채흥석'\n" +
                    "     'email' : 'hschoi@pusan.ac.kr'\n" +
                    "     'department' : {\n" +
                    "      'idx' : 22,\n" +
                    "      'name' : '정보컴퓨터공학부'\n" +
                    "     }\n" +
                    "    }\n" +
                    "    'lecture' : {\n" +
                    "     'idx' : 1,\n" +
                    "     'name' : '운영체제',\n" +
                    "     'department' : {\n" +
                    "      'idx' : 22,\n" +
                    "      'name' : '정보컴퓨터공학부'\n" +
                    "     }\n" +
                    "    }\n" +
                    "   }\n" +
                    "   'take_count' : 1,\n" +
                    "  },\n" +
                    "  {\n" +
                    "   'lectureDetails' : {\n" +
                    "    'idx' : 3,\n" +
                    "    'section' : 57,\n" +
                    "    'week' : 16,\n" +
                    "    'count_per_week' : 2\n" +
                    "    'start_time' : '2020-08-01T16:00:00'\n" +
                    "    'end_time' : '2020-08-01T17:30:00'\n" +
                    "    'professor' : {\n" +
                    "     'idx' : 9\n" +
                    "     'uid' : 'pf_johk'\n" +
                    "     'name' : '조환규'\n" +
                    "     'email' : 'johk@pusan.ac.kr'\n" +
                    "     'department' : {\n" +
                    "      'idx' : 22,\n" +
                    "      'name' : '정보컴퓨터공학부'\n" +
                    "     }\n" +
                    "    }\n" +
                    "    'lecture' : {\n" +
                    "     'idx' : 4,\n" +
                    "     'name' : 'C프로그래밍',\n" +
                    "     'department' : {\n" +
                    "      'idx' : 22,\n" +
                    "      'name' : '정보컴퓨터공학부'\n" +
                    "     }\n" +
                    "    }\n" +
                    "   }\n" +
                    "   'take_count' : 0,\n" +
                    "  },\n" +
                    " ]\n" +
                    "}\n" +
                    "{\n 'resultCode' : '41',\n 'resultMessage' : 'the student didn't registered to any lecture',\n 'data' : [ ]\n}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '34', 'resultMessage' : 'the member is not student'}"))
        })
    })
    @GetMapping("/lecture/all")
    public ResponseEntity getLectureRegisteredList(HttpServletRequest request) {
        return service.getLectureRegisteredList(request);
    }


    @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true)
    @Operation(summary="수강 중인 특정 강의의 스크립트가 저장된 데이터의 개요 리스트만 반환")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{\n 'resultCode' : '0',\n 'resultMessage' : 'lecture brief lists response successful'\n" +
                    " 'data' : [\n" +
                    "  {\n" +
                    "   'idx' : 3,\n" +
                    "   'week' : 1,\n" +
                    "   'num_per_week' : 1\n" +
                    "   'created' : '2020-08-01T16:00:00.905'\n" +
                    "   'modified' : '2020-08-01T17:30:00.905'\n" +
                    "   'summary' : '강의 개요 내용 테스트 111111'\n" +
                    "  },\n" +
                    "  {\n" +
                    "   'idx' : 4,\n" +
                    "   'week' : 1,\n" +
                    "   'num_per_week' : 2\n" +
                    "   'created' : '2020-08-01T16:00:00.905'\n" +
                    "   'modified' : '2020-08-01T17:30:00.905'\n" +
                    "   'summary' : '강의 개요 내용 테스트 222222'\n" +
                    "  },\n" +
                    "  {\n" +
                    "   'idx' : 5,\n" +
                    "   'week' : 2,\n" +
                    "   'num_per_week' : 1\n" +
                    "   'created' : '2020-08-01T16:00:00.905'\n" +
                    "   'modified' : '2020-08-01T17:30:00.905'\n" +
                    "   'summary' : '강의 개요 내용 테스트 333333'\n" +
                    "  },\n" +
                    " ]\n" +
                    "}\n" +
                    "{\n 'resultCode' : '37',\n 'resultMessage' : 'student didn't save any scripts (empty)',\n 'data' : [ ]\n}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student not registered this lecture'}"))
        })
    })
    @GetMapping("/lecture/summary")
    public ResponseEntity getLectureRegisteredBriefList(HttpServletRequest request, @RequestParam("lec_details_idx") final String param_lec_details_idx) {
        final long lec_details_idx = Long.parseLong(param_lec_details_idx);
        return service.getLectureRegisteredBriefList(request, lec_details_idx);
    }


    @Parameters({
        @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="week", description="주차 수 (몇 번째 주 수업인지)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="1 (1주 2회차 수업일 때)"),
        @Parameter(name="num_per_week", description="강의 회차 (1주 1회차, 1주 2회차, 2주 1회차...)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="2 (1주 2회차 수업일 때)"),
    })
    @Operation(summary="특정 주차 강의 스크립트 반환")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{\n 'resultCode' : '0',\n 'resultMessage' : 'lecture script get successful'\n" +
                    " 'data' : {\n" +
                    "   'week' : 1,\n" +
                    "   'num_per_week' : 1\n" +
                    "   'lec_details_idx' : 4\n" +
                    "   'summary' : '강의 개요 내용 테스트 111111'\n" +
                    "   'script' : '강의 자막이 저장되는 곳입니다.'\n" +
                    " }\n" +
                    "}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student not registered to this lecture'}\n" +
                        "{'resultCode' : '38', 'resultMessage' : 'invalid week or num_per_week parameter (script not saved in database)'}"))
        })
    })
    @GetMapping("/lecture/script")
    public ResponseEntity getLectureRegisteredScript(HttpServletRequest request, @RequestParam("lec_details_idx") final String param_lec_details_idx,
                                                     @RequestParam("week") final String param_week, @RequestParam("num_per_week") final String param_num_per_week) {
        final long lec_details_idx = Long.parseLong(param_lec_details_idx);
        final int week = Integer.parseInt(param_week);
        final int num_per_week = Integer.parseInt(param_num_per_week);
        return service.getLectureRegisteredScript(request, lec_details_idx, week, num_per_week);
    }


    @Parameters({
        @Parameter(name="lec_details_idx", description="강의 코드", in= ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="week", description="주차 수 (몇 번째 주 수업인지)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="1 (1주 2회차 수업일 때)"),
        @Parameter(name="num_per_week", description="강의 회차 (1주 1회차, 1주 2회차, 2주 1회차...)", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true, example="2 (1주 2회차 수업일 때)"),
        @Parameter(name="summary", description="강의 개요 (400byte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="script", description="강의 스크립트 (64Kbyte)", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
    })
    @Operation(summary="특정 주차 강의 스크립트 수정")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'lecture script put successful'}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '32', 'resultMessage' : 'invalid lecture_details code'}\n" +
                        "{'resultCode' : '33', 'resultMessage' : 'invalid member (session invalid)'}\n" +
                        "{'resultCode' : '36', 'resultMessage' : 'student not registered to this lecture'}\n" +
                        "{'resultCode' : '38', 'resultMessage' : 'invalid week or num_per_week parameter (script not saved in database)'}"))
        })
    })
    @PutMapping("/lecture/script")
    public ResponseEntity modifyLectureRegisteredScript(HttpServletRequest request, @Parameter(hidden=true) @RequestBody LectureScriptDto lectureScriptDto) {
        System.out.println(lectureScriptDto);
        return service.modifyLectureRegisteredScript(request, lectureScriptDto);
    }
}