package pnu.classplus.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.DataResponse;
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.*;
import pnu.classplus.domain.repository.*;
import pnu.classplus.dto.LectureDetailsDto;
import pnu.classplus.dto.LectureScriptDto;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static pnu.classplus.domain.entity.QLectureRegisteredEntity.lectureRegisteredEntity;
import static pnu.classplus.domain.entity.QLectureScriptEntity.lectureScriptEntity;
import static pnu.classplus.domain.entity.QMemberEntity.memberEntity;

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
            LectureRegisteredEntity lectureRegisteredEntity = optionalLectureRegistered.get();
            if (!lectureRegisteredEntity.isEnabled()) {
                return new ResponseEntity(new ApiResponse(39, "student already deleted this lecture"),
                    HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity(new ApiResponse(36, "student already registered this lecture"),
                HttpStatus.BAD_REQUEST);
        }
        /*
         * 같은과목 다른 분반 중복체크 필요할까?
         */
        lectureRegisteredRepo.save(LectureRegisteredEntity.builder()
                                                        .student(memberStudent)
                                                        .lectureDetails(lectureDetails)
                                                        .take_count(0)
                                                        .enabled(true)
                                                        .build());

        return new ResponseEntity(new ApiResponse(0, "lecture register successful"),
            HttpStatus.OK);
    }

    public ResponseEntity deleteLecture(HttpServletRequest request, Long lec_details_idx) {
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
        if (!optionalLectureRegistered.isPresent()) {
            return new ResponseEntity(new ApiResponse(36, "cannot delete lecture, student not registered this lecture"),
                HttpStatus.BAD_REQUEST);
        }

        LectureRegisteredEntity lectureRegisteredEntity = optionalLectureRegistered.get();
        if (!lectureRegisteredEntity.isEnabled()) {
            return new ResponseEntity(new ApiResponse(39, "already deleted this lecture"),
                HttpStatus.BAD_REQUEST);
        }
        lectureRegisteredEntity.setEnabled(false);
        lectureRegisteredRepo.save(lectureRegisteredEntity);

        return new ResponseEntity(new ApiResponse(0, "lecture delete successful"),
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
        if (!lectureRegistered.isEnabled()) {
            return new ResponseEntity(new ApiResponse(36, "student not registered to this lecture"),
                HttpStatus.BAD_REQUEST);
        }
        /*
         * take_count 증가 위한 쿼리 추가 예정
         */
        /*
         * num_per_week가 cnt 넘지는 않았는지 예외 처리 (중요). week, num_per_week 중복 체크
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

    public ResponseEntity getLectureRegisteredList(HttpServletRequest request) {
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

        List<LectureRegisteredEntity> lectureRegisteredEntities = queryFactory
            .select(Projections.bean(LectureRegisteredEntity.class, lectureRegisteredEntity.idx,
                lectureRegisteredEntity.lectureDetails, lectureRegisteredEntity.take_count))
            .from(lectureRegisteredEntity)
            .innerJoin(lectureRegisteredEntity.student, memberEntity)
            .where(lectureRegisteredEntity.student.eq(memberStudent).and(lectureRegisteredEntity.enabled.eq(true)))
            .fetch();

        if (lectureRegisteredEntities.isEmpty()) {
            return new ResponseEntity(new DataResponse(41, "the student didn't registered to any lecture", lectureRegisteredEntities),
                HttpStatus.OK);
        }

        return new ResponseEntity(new DataResponse(0, "registered lecture lists response successful", lectureRegisteredEntities),
            HttpStatus.OK);
    }

    public ResponseEntity getLectureRegisteredBriefList(HttpServletRequest request, long lec_details_idx) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optMemberStudent = memberRepo.findByUid(uid);
        if (!optMemberStudent.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid member (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }
        MemberEntity memberStudent = optMemberStudent.get();

        Optional<LectureDetailsEntity> optLectureDetails = lectureDetailsRepo.findById(lec_details_idx);
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

        if (!lectureRegistered.isEnabled()) {
            return new ResponseEntity(new ApiResponse(36, "student not registered to this lecture"),
                HttpStatus.BAD_REQUEST);
        }

        Set<LectureScriptEntity> lectureScriptEntitySet = lectureRegistered.getLectureScriptSet();

        if (lectureScriptEntitySet.isEmpty()) {
            return new ResponseEntity(new DataResponse(37, "student didn't save any scripts (empty)", lectureScriptEntitySet),
                HttpStatus.OK);
        }

        return new ResponseEntity(new DataResponse(0, "lecture brief lists response successful", lectureScriptEntitySet),
            HttpStatus.OK);
    }

    public ResponseEntity getLectureRegisteredScript(HttpServletRequest request, long lec_details_idx, int week, int num_per_week) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optMemberStudent = memberRepo.findByUid(uid);
        if (!optMemberStudent.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid member (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }
        MemberEntity memberStudent = optMemberStudent.get();

        Optional<LectureDetailsEntity> optLectureDetails = lectureDetailsRepo.findById(lec_details_idx);
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

        if (!lectureRegistered.isEnabled()) {
            return new ResponseEntity(new ApiResponse(36, "student not registered to this lecture"),
                HttpStatus.BAD_REQUEST);
        }

        List<LectureScriptEntity> lectureScriptEntities = queryFactory
            .select(Projections.bean(LectureScriptEntity.class, lectureScriptEntity.summary, lectureScriptEntity.script))
            .from(lectureScriptEntity)
            .where(lectureScriptEntity.lectureRegistered.eq(lectureRegistered)
                .and(lectureScriptEntity.week.eq(week))
                .and(lectureScriptEntity.num_per_week.eq(num_per_week)))
            .fetch();

        if (lectureScriptEntities.isEmpty()) {
            return new ResponseEntity(new ApiResponse(38, "invalid week or num_per_week parameter (script not saved in database)"),
                HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> responseData = new HashMap<String, Object>();
        responseData.put("week", week);
        responseData.put("num_per_week", num_per_week);
        responseData.put("lec_details_idx", lec_details_idx);
        responseData.put("script", lectureScriptEntities.get(0).getScript());
        responseData.put("summary", lectureScriptEntities.get(0).getSummary());

        return new ResponseEntity(new DataResponse(0, "lecture script get successful", responseData),
            HttpStatus.OK);
    }

    public ResponseEntity modifyLectureRegisteredScript(HttpServletRequest request, LectureScriptDto lectureScriptDto) {
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

        if (!lectureRegistered.isEnabled()) {
            return new ResponseEntity(new ApiResponse(36, "student not registered to this lecture"),
                HttpStatus.BAD_REQUEST);
        }

        List<LectureScriptEntity> lectureScriptEntities = queryFactory
            .selectFrom(lectureScriptEntity)
            .where(lectureScriptEntity.lectureRegistered.eq(lectureRegistered)
                .and(lectureScriptEntity.week.eq(lectureScriptDto.getWeek()))
                .and(lectureScriptEntity.num_per_week.eq(lectureScriptDto.getNum_per_week())))
            .fetch();

        if (lectureScriptEntities.isEmpty()) {
            return new ResponseEntity(new ApiResponse(38, "invalid week or num_per_week parameter (script not saved in database)"),
                HttpStatus.BAD_REQUEST);
        }
        LectureScriptEntity lectureScriptEntity = lectureScriptEntities.get(0);
        lectureScriptEntity.setSummary(lectureScriptDto.getSummary());
        lectureScriptEntity.setScript(lectureScriptDto.getScript());

        lectureScriptRepo.save(lectureScriptEntity);
        return new ResponseEntity(new ApiResponse(0, "lecture script put successful"),
            HttpStatus.OK);
    }
}
