package pnu.classplus.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.domain.entity.*;
import pnu.classplus.domain.repository.DepartmentRepository;
import pnu.classplus.domain.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pnu.classplus.domain.entity.QDepartmentEntity.departmentEntity;
import static pnu.classplus.domain.entity.QLectureEntity.lectureEntity;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class InfoService {

    private final UniversityRepository univRepo;
    private final DepartmentRepository deptRepo;
    private final JPAQueryFactory queryFactory;

    public ResponseEntity getUniversityInfo() {
        Set<UniversityEntity> data = univRepo.findAll();

        return new ResponseEntity(new DataResponse(0, "response successful", data),
            HttpStatus.OK);
    }

    public ResponseEntity getDepartmentInfo(final long univCode) {
        Optional<UniversityEntity> optUniv = univRepo.findById(univCode);
        if (!optUniv.isPresent()) {
            return new ResponseEntity(new ApiResponse(31, "invalid univ_idx parameter"),
                HttpStatus.BAD_REQUEST);
        }
        UniversityEntity univ = optUniv.get();
        QDepartmentEntity dept = departmentEntity;
        List<DepartmentEntity> data = queryFactory.select(Projections.bean(DepartmentEntity.class, dept.idx, dept.name))
                                                .where(dept.university.eq(univ))
                                                .from(dept)
                                                .fetch();
        return new ResponseEntity(new DataResponse(0, "response successful", data),
                HttpStatus.OK);
    }

    public ResponseEntity getLectureInfo(final long deptCode) {
        Optional<DepartmentEntity> optDept = deptRepo.findById(deptCode);
        if (!optDept.isPresent()) {
            return new ResponseEntity(new ApiResponse(31, "invalid dept_idx parameter"),
                HttpStatus.BAD_REQUEST);
        }
        DepartmentEntity dept = optDept.get();
        QLectureEntity lecture = lectureEntity;
        List<LectureEntity> data = queryFactory.select(Projections.bean(LectureEntity.class, lecture.idx, lecture.name))
                                            .where(lecture.department.eq(dept))
                                            .from(lecture)
                                            .fetch();
        return new ResponseEntity(new DataResponse(0, "response successful", data),
            HttpStatus.OK);
    }
}

@Getter
class DataResponse extends ApiResponse {
    private Object data;

    public DataResponse(int resultCode, String resultMessage, Object data) {
        super(resultCode, resultMessage);
        this.data = data;
    }
}