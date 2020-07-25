package pnu.classplus.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pnu.classplus.domain.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUid(String uid);
}