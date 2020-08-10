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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value=
                    "{\n 'resultCode' : '0',\n 'resultMessage' : 'response successful',\n" +
                        " 'data' : [\n" +
                        "  {\n   'idx' : 1,\n   'name' : '부산대학교'\n  },\n" +
                        "  {\n   'idx' : 2,\n   'name' : '서울대학교'\n  },\n" +
                        "  {\n   'idx' : 3,\n   'name' : '경북대학교'\n  }\n" +
                        " ]\n" + "}"))
        }),
    })
    @GetMapping("/university")
    public ResponseEntity getUniversityInfo() {
        return service.getUniversityInfo();
    }


    @Parameter(name="univ_idx", description="대학교 코드", schema=@Schema(type="integer"))
    @Operation(summary="학과 정보 불러오기", description="학과 코드, 이름을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value=
                    "{\n 'resultCode' : '0',\n 'resultMessage' : 'response successful',\n" +
                        " 'data' : [\n" +
                        "  {\n   'idx' : 65,\n   'name' : '정보컴퓨터공학부'\n  },\n" +
                        "  {\n   'idx' : 66,\n   'name' : '전기공학과'\n  },\n" +
                        "  {\n   'idx' : 67,\n   'name' : '의생명융합공학부'\n  }\n" +
                        " ]\n" + "}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '31', 'resultMessage' : 'invalid univ_idx parameter'}"))
        }),
    })
    @GetMapping("/department")
    public ResponseEntity getDepartmentInfo(@RequestParam("univ_idx") final String univ_idx) {
        final long univCode = Long.parseLong(univ_idx);
        return service.getDepartmentInfo(univCode);
    }


    @Parameter(name="dept_idx", description="학과 코드", schema=@Schema(type="integer"))
    @Operation(summary="강의 정보 불러오기", description="강의 코드, 이름을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value=
                    "{\n 'resultCode' : '0',\n 'resultMessage' : 'response successful',\n" +
                        " 'data' : [\n" +
                        "  {\n   'idx' : 65,\n   'name' : '국어맞춤법'\n  },\n" +
                        "  {\n   'idx' : 66,\n   'name' : '시조와가사'\n  },\n" +
                        "  {\n   'idx' : 67,\n   'name' : '문예비평'\n  }\n" +
                        " ]\n" + "}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType= MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '31', 'resultMessage' : 'invalid dept_idx parameter'}"))
        }),
    })
    @GetMapping("/lecture")
    public ResponseEntity getLectureInfo(@RequestParam("dept_idx") final String dept_idx) {
        final long deptCode = Long.parseLong(dept_idx);
        return service.getLectureInfo(deptCode);
    }
}