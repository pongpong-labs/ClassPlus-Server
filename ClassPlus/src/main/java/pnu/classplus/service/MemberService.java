package pnu.classplus.service;

import lombok.Builder;
import lombok.Getter;
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
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.DepartmentEntity;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.entity.UniversityEntity;
import pnu.classplus.domain.repository.DepartmentRepository;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.domain.repository.UniversityRepository;
import pnu.classplus.dto.MemberDto;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepo;
    private final DepartmentRepository deptRepo;
    private final UniversityRepository univRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Transactional
    public ResponseEntity join(MemberDto memberDto) {
        if (memberRepo.existsByUid(memberDto.getUid())) {
            return new ResponseEntity(new ApiResponse(11, "User ID is already registered"),
                    HttpStatus.BAD_REQUEST);
        }

        if (memberRepo.existsByEmail(memberDto.getEmail())) {
            return new ResponseEntity(new ApiResponse(12, "Email is already registered"),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<UniversityEntity> optUniv = univRepo.findById(memberDto.getUniv());
        if (!optUniv.isPresent()) {
            return new ResponseEntity(new ApiResponse(13, "Invalid University Code"),
                    HttpStatus.BAD_REQUEST);
        }
        UniversityEntity univ = optUniv.get();

        Optional<DepartmentEntity> optDept = deptRepo.findById(memberDto.getDept());
        if (!optDept.isPresent()) {
            return new ResponseEntity(new ApiResponse(14, "Invalid Department Code"),
                    HttpStatus.BAD_REQUEST);
        }
        DepartmentEntity dept = optDept.get();

        memberRepo.save(MemberEntity.builder()
                .uid(memberDto.getUid())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .role(memberDto.getRole())
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .university(univ)
                .department(dept)
                .enabled(true)
                .roles(Collections.singleton(memberDto.getRole().toString()))
                .build());

        return ResponseEntity.ok().body(new ApiResponse(0, "Join Successful!"));
    }

    public ResponseEntity login(MemberDto memberDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(memberDto.getUid(), memberDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtTokenProvider.generateJwtToken(authentication);

            MemberEntity member = memberRepo.findByUid(memberDto.getUid()).get();

            return ResponseEntity.ok(new LoginResponse(0, "Login Successful!", token, member.getIdx(), member.getUid(), member.getRole().toString(), member.getName(), member.getEmail(), member.getUniversity().getIdx(), member.getDepartment().getIdx()));
        } catch(BadCredentialsException e) {
            return new ResponseEntity(new ApiResponse(21, "ID or PW is not correct!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity logout(HttpServletRequest request) {
        final String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateJwtToken(token)) {
            long validTime = jwtTokenProvider.getValidTime(token);
            redisTemplate.opsForValue().set(token, "1", validTime, TimeUnit.MILLISECONDS);
            logger.info("Logout Called >> Redis Key : {}", token);
        }
        return ResponseEntity.ok(new ApiResponse(0, "Logout Successful!"));
    }

    public ResponseEntity checkIdOverlap(final String id) {
        if (memberRepo.existsByUid(id)) {
            return new ResponseEntity(new ApiResponse(11, "id is overlapped"),
                    HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(0, "id is not overlapped"),
                HttpStatus.OK);
    }

    public ResponseEntity checkEmailOverlap(final String email) {
        if (memberRepo.existsByEmail(email)) {
            return new ResponseEntity(new ApiResponse(12, "email is overlapped"),
                    HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(0, "email is not overlapped"),
                HttpStatus.OK);
    }

    public ResponseEntity findId(MemberDto memberDto) {
        final String name = memberDto.getName();
        final String email = memberDto.getEmail();

        Set<MemberEntity> memberSet = memberRepo.findByNameContainingAndEmailContaining(name, email);
        if (memberSet.size() == 0) {
            return new ResponseEntity(new ApiResponse(22, "no member"),
                    HttpStatus.OK);
        }
        Iterator<MemberEntity> it = memberSet.iterator();
        MemberEntity member = it.next();

        return new ResponseEntity(new FoundResponse(0, "found id", member.getUid()),
                HttpStatus.OK);
    }

    public ResponseEntity checkPw(MemberDto memberDto) {
        final String id = memberDto.getUid();
        final String pw = memberDto.getPassword();
        Optional<MemberEntity> optMember = memberRepo.findByUid(id);
        MemberEntity member = optMember.get();
        if (!passwordEncoder.matches(pw, member.getPassword())) {
            return new ResponseEntity(new ApiResponse(13, "incorrect password"),
                HttpStatus.OK);
        }
        return new ResponseEntity(new ApiResponse(0, "correct password"),
            HttpStatus.OK);
    }

    public ResponseEntity changePw(final String id, final String newPassword) {
        Optional<MemberEntity> optMember = memberRepo.findByUid(id);
        MemberEntity member = optMember.get();

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepo.save(member);

        return new ResponseEntity(new ApiResponse(0, "password change completed"),
            HttpStatus.OK);
    }
}

@Getter
class FoundResponse extends ApiResponse {
    private String data;

    public FoundResponse(int resultCode, String resultMessage, String data) {
        super(resultCode, resultMessage);
        this.data = data;
    }
}

@Getter
class LoginResponse extends ApiResponse {
    private long idx;
    private String type;
    private String token;
    private String uid;
    private String role;
    private String name;
    private String email;
    private long univ;
    private long dept;

    public LoginResponse(int resultCode, String resultMessage, String token, long idx, String uid, String role, String name, String email, long univ, long dept) {
        super(resultCode, resultMessage);
        this.type = "Bearer";
        this.token = token;
        this.idx = idx;
        this.uid = uid;
        this.role = role;
        this.name = name;
        this.email = email;
        this.univ = univ;
        this.dept = dept;
    }
}