package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.LectureDetailsEntity;
import pnu.classplus.domain.entity.LectureRegisteredEntity;
import pnu.classplus.domain.entity.MemberEntity;

import java.util.Optional;

public interface LectureRegisteredRepository extends CrudRepository<LectureRegisteredEntity, Long> {
    Optional<LectureRegisteredEntity> findByStudent(MemberEntity student);
    Optional<LectureRegisteredEntity> findByStudentAndLectureDetails(MemberEntity student, LectureDetailsEntity lectureDetails);
}
