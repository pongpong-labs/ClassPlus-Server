package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.DepartmentEntity;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, Long> {
}
