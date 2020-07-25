package pnu.classplus.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.classplus.domain.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
