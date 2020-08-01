package pnu.classplus.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.classplus.dto.MemberDto;
import pnu.classplus.service.MemberService;

import javax.validation.Valid;

@Api(tags="회원, 로그인 관리 기능")
@RestController
@RequestMapping("/member/*")
public class MemberController {
    @Autowired
    private MemberService service;

    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="아이디 (최대 20바이트)", dataType="string"),
            @ApiImplicitParam(name="password", value="비밀번호", dataType="string"),
            @ApiImplicitParam(name="role", value="회원 유형 (학생/조교/교수)", dataType="string"),
            @ApiImplicitParam(name="name", value="이름 (최대 30바이트)", dataType="string"),
            @ApiImplicitParam(name="email", value="이메일 (최대 100바이트)", dataType="string"),
            @ApiImplicitParam(name="address", value="주소 (최대 300바이트)", dataType="string"),
            @ApiImplicitParam(name="phone", value="전화번호 (최대 20바이트)", dataType="string"),
            @ApiImplicitParam(name="univ", value="대학교 (최대 20바이트)", dataType="string"),
            @ApiImplicitParam(name="dept", value="학과 (최대 20바이트)", dataType="string")
    })
    @ApiOperation(value="회원 가입", notes="role 은 Models - MemberDto에 설명된 Enum 타입을 스트링으로 반환하면 됩니다.")
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberDto memberDto) {
        return service.join(memberDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value="아이디 (최대 20바이트)", dataType="string"),
            @ApiImplicitParam(name="password", value="비밀번호", dataType="string"),
    })
    @ApiOperation(value="로그인")
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MemberDto memberDto) {
        return service.login(memberDto);
    }
}
