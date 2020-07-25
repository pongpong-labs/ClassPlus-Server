package pnu.classplus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pnu.classplus.dto.MemberDto;
import pnu.classplus.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService service;

    @PostMapping("/join")
    public Long join(@RequestBody MemberDto memberDto) {
        return service.save(memberDto);
    }

}
