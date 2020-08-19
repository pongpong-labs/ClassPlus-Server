package pnu.classplus.domain.repository;

import org.springframework.data.repository.CrudRepository;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.entity.MessageEntity;

import java.util.Set;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {
    Set<MessageEntity> findByReceiverContaining(MemberEntity receiver);
}
