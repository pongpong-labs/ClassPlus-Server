package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.UniversityEntity;

import java.util.Set;

public interface UniversityRepository extends CrudRepository<UniversityEntity, Long> {
    Set<UniversityEntity> findAll();
}
