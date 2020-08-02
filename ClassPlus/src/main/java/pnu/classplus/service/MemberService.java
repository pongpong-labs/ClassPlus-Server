package pnu.classplus.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.JwtResponse;
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.dto.MemberDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Transactional
    public ResponseEntity join(MemberDto memberDto) {
        if (repository.existsByUid(memberDto.getId())) {
            return new ResponseEntity(new ApiResponse(11, "User ID is already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        if (repository.existsByEmail(memberDto.getEmail())) {
            return new ResponseEntity(new ApiResponse(12, "Email is already registered."),
                    HttpStatus.BAD_REQUEST);
        }

        repository.save(MemberEntity.builder()
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
                .build());

        return ResponseEntity.ok().body(new ApiResponse(0, "Join Successful!"));
    }

    public ResponseEntity login(MemberDto memberDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(memberDto.getId(), memberDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenProvider.generateJwtToken(authentication);
            return ResponseEntity.ok(new JwtResponse(0, "Login Successful!", token));
        } catch(BadCredentialsException e) {
            return new ResponseEntity(new ApiResponse(13, "ID or PW is not correct!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateJwtToken(token)) {
            long validTime = jwtTokenProvider.getValidTime(token);
            redisTemplate.opsForValue().set(token, "1", validTime, TimeUnit.MILLISECONDS);
            logger.info("Logout Called >> Redis Key : {}", token);
        }
        return ResponseEntity.ok(new ApiResponse(0, "Logout Successful!"));
    }
}
