package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.LectureEntity;

public interface LectureRepository extends CrudRepository<LectureEntity, Long> {
}
