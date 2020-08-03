package pnu.classplus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pnu.classplus.dto.MemberDto;
import pnu.classplus.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Tag(name="회원, 로그인 관리 기능")
@RestController
@RequestMapping("/member/*")
public class MemberController {
    @Autowired
    private MemberService service;

    @Parameters({
        @Parameter(name="id", description="아이디", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=20), required=true),
        @Parameter(name="password", description="비밀번호", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
        @Parameter(name="role", description="회원 유형", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true, example="'ROLE_STUDENT' or 'ROLE_ADMIN' or 'ROLE_ASSISTANT' or 'ROLE_PROFESSOR'"),
        @Parameter(name="name", description="이름", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=30), required=true),
        @Parameter(name="email", description="이메일", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=100), required=true),
        @Parameter(name="univ", description="대학교 코드", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true),
        @Parameter(name="dept", description="학과 코드", in=ParameterIn.QUERY, schema=@Schema(type="integer"), required=true)
    })
    @Operation(summary="회원 가입", description="대학교, 학과 코드는 정수형, 회원 유형은 예시에 명시된 스트링을 사용하세요.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'Join Successful!'}"))
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
    @PostMapping("/join")
    public ResponseEntity join(@Parameter(hidden=true) @RequestBody MemberDto memberDto) {
        return service.join(memberDto);
    }


    @Parameters({
        @Parameter(name="uid", description="아이디", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=20), required=true),
        @Parameter(name="password", description="비밀번호", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    })
    @Operation(summary="로그인", description="로그인 성공시 JWT 토큰을 반환합니다.\nAPI 호출시 로그아웃 패러미터에 명시된 예시와 같이 항상 HTTP Header에 인증 정보를 담아 전송하세요.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'Login Successful!', 'type' : 'Bearer',\n" +
                    " 'token' : 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU5NjQ2MDk2NSwiZXhwIjoxNTk2NDYyNzY1fQ.TGPpH410WEo2zZvnbDQgy8I2TmCXREMhs3KER4uE7WnodIMIrJJVsv4WHCOiPOEoZ5NIRccwIvCMzARiLgnPuA'\n" +
                    " 'idx' : '2', 'uid' : 'admin', 'name' : '관리자', 'role' : 'ROLE_ADMIN',\n 'email' : 'admin@pusan.ac.kr', 'univ' : 1, 'dept' : 3" +
                "}"))
        }),
        @ApiResponse(responseCode="400", description="Error", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples={
                @ExampleObject(value="{'resultCode' : '21', 'resultMessage' : 'ID or PW is not correct!'}")
            })
        })
    })
    @PostMapping("/login")
    public ResponseEntity login(@Parameter(hidden=true) @RequestBody MemberDto memberDto) {
        return service.login(memberDto);
    }


    @Parameters({
        @Parameter(name="Authorization", description="'토큰타입 + 공백 + 토큰'을 스트링에 포함합니다.",
                example="Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwcjEiLCJpYXQiOjE1OTYzNjg0NjgsImV4cCI6MTU5NjM3MDI2OH0.EMqKBsL828noeh9_9-VEKY0Q0iwwYEnJJcox1PwF8hd-bH52vrrPSz54yzbRuwMyvaY1m2twjfgMEaQbZ20rQQ",
                in=ParameterIn.HEADER, schema=@Schema(type="string"), required=true),
    })
    @Operation(summary="로그아웃")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'Logout Successful!'}"))
        })
    })
    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) { return service.logout(request); }


    @Parameter(name="uid", description="아이디", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=20), required=true)
    @Operation(summary="아이디 중복 체크")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'id is not overlapped'}\n" +
                                                                                            "{'resultCode' : '11', 'resultMessage' : 'id is overlapped'}"))
        })
    })
    @GetMapping("/check/id")
    public ResponseEntity checkIdOverlap(@RequestParam("uid") final String id) { return service.checkIdOverlap(id); }


    @Parameter(name="email", description="이메일", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    @Operation(summary="이메일 중복 체크")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'email is not overlapped'}\n" +
                                                                                            "{'resultCode' : '12', 'resultMessage' : 'email is overlapped'}"))
        })
    })
    @GetMapping("/check/email")
    public ResponseEntity checkEmailOverlap(@RequestParam("email") final String email) { return service.checkEmailOverlap(email); }

    @Parameters({
        @Parameter(name="name", description="이름", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=30), required=true),
        @Parameter(name="email", description="이메일", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=100), required=true),
    })
    @Operation(summary="아이디 찾기")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '0', 'resultMessage' : 'found id', 'data' : 'abcd12'}\n" +
                            "{'resultCode' : '22', 'resultMessage' : 'no member'}"))
        })
    })
    @PostMapping("/find/id")
    public ResponseEntity findId(@Parameter(hidden=true) @RequestBody MemberDto memberDto) {
        return service.findId(memberDto);
    }


    @Parameters({
        @Parameter(name="uid", description="아이디", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=20), required=true),
        @Parameter(name="password", description="비밀번호", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true),
    })
    @Operation(summary="비밀번호 확인", description="잘못된 아이디값 요청 시 예외 처리되어 있지 않습니다. 주의하세요.")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE,
                examples=@ExampleObject(
                    value="{'resultCode' : '0', 'resultMessage' : 'correct password'}\n" +
                        "{'resultCode' : '13', 'resultMessage' : 'incorrect password'}"))
        })
    })
    @PostMapping("/check/pw")
    public ResponseEntity checkPw(@Parameter(hidden=true) @RequestBody MemberDto memberDto) {
        return service.checkPw(memberDto);
    }


    @Parameters({
        @Parameter(name="uid", description="아이디", in=ParameterIn.QUERY, schema=@Schema(type="string", maxLength=20), required=true),
        @Parameter(name="new_pw", description="새 비밀번호", in=ParameterIn.QUERY, schema=@Schema(type="string"), required=true)
    })
    @Operation(summary="비밀번호 변경")
    @ApiResponses({
        @ApiResponse(responseCode="200", description="Success", content={
            @Content(mediaType=MediaType.APPLICATION_JSON_VALUE, examples=@ExampleObject(value="{'resultCode' : '0', 'resultMessage' : 'password change completed'}"))
        })
    })
    @PostMapping("/change/pw")
    public ResponseEntity changePw(@Parameter(hidden=true) @RequestBody Map<String, Object> param) {
        final String id = (String) param.get("uid");
        final String newPassword = (String) param.get("new_pw");
        return service.changePw(id, newPassword);
    }
}