package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.MemberEntity;

import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUid(String uid);
    Optional<MemberEntity> findByIdx(long idx);
    Boolean existsByUid(String uid);
    Boolean existsByEmail(String email);
    Set<MemberEntity> findByNameContainingAndEmailContaining(String name, String email);
    Set<MemberEntity> findByUidContainingAndNameContainingAndEmailContaining(String uid, String name, String email);
}