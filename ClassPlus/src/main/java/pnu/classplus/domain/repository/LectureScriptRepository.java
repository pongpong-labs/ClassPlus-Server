package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.LectureRegisteredEntity;
import pnu.classplus.domain.entity.LectureScriptEntity;

import java.util.Optional;

public interface LectureScriptRepository extends CrudRepository<LectureScriptEntity, Long> {
    Optional<LectureScriptEntity> findByLectureRegisteredAndWeekAndNumPerWeek(LectureRegisteredEntity lectureRegistered, int week, int numPerWeek);
}
