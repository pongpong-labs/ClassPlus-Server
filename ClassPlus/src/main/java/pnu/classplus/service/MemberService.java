package pnu.classplus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.dto.MemberDto;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository repository;

    @Transactional
    public Long join(MemberDto memberDto) {
        return repository.save(MemberEntity.builder()
                .uid(memberDto.getId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .role(memberDto.getRole())
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .address(memberDto.getAddress())
                .phone(memberDto.getPhone())
                .univ(memberDto.getUniv())
                .dept(memberDto.getDept())
                .enabled(true)
                .roles(Collections.singletonList(memberDto.getRole().toString()))
                .build()).getIdx();
    }

    @Transactional
    public String login(MemberDto memberDto) {
        Optional<MemberEntity> optional = repository.findByUid(memberDto.getId());
        MemberEntity member;
        if (!optional.isPresent()) {
            throw new UsernameNotFoundException(memberDto.getId() + " 사용자 없음");
        } else {
            member = optional.get();
        }
        if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }
}
