package pnu.classplus.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.*;
import pnu.classplus.domain.repository.*;
import pnu.classplus.dto.LectureDetailsDto;
import pnu.classplus.dto.LectureScriptDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class LectureService {

    private final MemberRepository memberRepo;
    private final LectureRepository lectureRepo;
    private final LectureDetailsRepository lectureDetailsRepo;
    private final LectureRegisteredRepository lectureRegisteredRepo;
    private final LectureScriptRepository lectureScriptRepo;
    private final JPAQueryFactory queryFactory;
    private final JwtTokenProvider tokenProvider;

    public ResponseEntity addLectureDetails(LectureDetailsDto lectureDetailsDto) {
        Optional<LectureEntity> optLecture = lectureRepo.findById(lectureDetailsDto.getLec_idx());
        if (!optLecture.isPresent()) {
            return new ResponseEntity(new ApiResponse(32, "invalid lecture code"),
                HttpStatus.BAD_REQUEST);
        }
        LectureEntity lecture = optLecture.get();

        Optional<MemberEntity> optMemberProfessor = memberRepo.findById(lectureDetailsDto.getProf_mem_idx());
        if (!optMemberProfessor.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid member code"),
                HttpStatus.BAD_REQUEST);
        }
        MemberEntity memberProfessor = optMemberProfessor.get();
        if (memberProfessor.getRole() != Role.ROLE_PROFESSOR) {
            return new ResponseEntity(new ApiResponse(34, "the member is not professor"),
                HttpStatus.BAD_REQUEST);
        }

        /*
         * 동일 분반 && 동일 과목 이미 등록되어있는지 예외 처리 code 35
         */

        lectureDetailsRepo.save(LectureDetailsEntity.builder()
                        .section(lectureDetailsDto.getSection())
                        .week(lectureDetailsDto.getWeek())
                        .count_per_week(lectureDetailsDto.getCount_per_week())
                        .start_time(lectureDetailsDto.getStart_time())
                        .end_time(lectureDetailsDto.getEnd_time())
                        .professor(memberProfessor)
                        .lecture(lecture)
                        .build());
        return new ResponseEntity(new ApiResponse(0, "lecture add successful"),
            HttpStatus.OK);
    }

    public ResponseEntity registerLecture(HttpServletRequest request, Long lec_details_idx) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optMemberStudent = memberRepo.findByUid(uid);
        if (!optMemberStudent.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid member (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }

        MemberEntity memberStudent = optMemberStudent.get();
        if (memberStudent.getRole() != Role.ROLE_STUDENT) {
            return new ResponseEntity(new ApiResponse(34, "the member is not student"),
                HttpStatus.BAD_REQUEST);
        }

        Optional<LectureDetailsEntity> optLectureDetails = lectureDetailsRepo.findById(lec_details_idx);
        if (!optLectureDetails.isPresent()) {
            return new ResponseEntity(new ApiResponse(32, "invalid lecture_details code"),
                HttpStatus.BAD_REQUEST);
        }
        LectureDetailsEntity lectureDetails = optLectureDetails.get();

        Optional<LectureRegisteredEntity> optionalLectureRegistered = lectureRegisteredRepo.findByStudentAndLectureDetails(memberStudent, lectureDetails);
        if (optionalLectureRegistered.isPresent()) {
            return new ResponseEntity(new ApiResponse(36, "student already registered this lecture"),
                HttpStatus.BAD_REQUEST);
        }

        lectureRegisteredRepo.save(LectureRegisteredEntity.builder()
                                                        .student(memberStudent)
                                                        .lectureDetails(lectureDetails)
                                                        .take_count(0)
                                                        .build());

        return new ResponseEntity(new ApiResponse(0, "lecture register successful"),
            HttpStatus.OK);
    }

    public ResponseEntity addLectureScript(HttpServletRequest request, LectureScriptDto lectureScriptDto) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optMemberStudent = memberRepo.findByUid(uid);
        if (!optMemberStudent.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid member (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }
        MemberEntity memberStudent = optMemberStudent.get();

        Optional<LectureDetailsEntity> optLectureDetails = lectureDetailsRepo.findById(lectureScriptDto.getLec_details_idx());
        if (!optLectureDetails.isPresent()) {
            return new ResponseEntity(new ApiResponse(32, "invalid lecture_details code"),
                HttpStatus.BAD_REQUEST);
        }
        LectureDetailsEntity lectureDetails = optLectureDetails.get();

        Optional<LectureRegisteredEntity> optionalLectureRegistered = lectureRegisteredRepo.findByStudentAndLectureDetails(memberStudent, lectureDetails);
        if (!optionalLectureRegistered.isPresent()) {
            return new ResponseEntity(new ApiResponse(36, "student not registered to this lecture"),
                HttpStatus.BAD_REQUEST);
        }
        LectureRegisteredEntity lectureRegistered = optionalLectureRegistered.get();

        /*
         * take_count 증가 위한 쿼리 추가 예정
         */

        lectureScriptRepo.save(LectureScriptEntity.builder()
                        .lectureRegistered(lectureRegistered)
                        .week(lectureScriptDto.getWeek())
                        .num_per_week(lectureScriptDto.getNum_per_week())
                        .summary(lectureScriptDto.getSummary())
                        .script(lectureScriptDto.getScript())
                        .feedback(lectureScriptDto.getFeedback())
                        .build());

        return new ResponseEntity(new ApiResponse(0, "lecture script add successful"),
            HttpStatus.OK);
    }
}

