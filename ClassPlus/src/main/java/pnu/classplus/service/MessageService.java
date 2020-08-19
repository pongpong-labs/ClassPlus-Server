package pnu.classplus.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.classplus.ApiResponse;
import pnu.classplus.DataResponse;
import pnu.classplus.config.security.JwtTokenProvider;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.entity.MessageEntity;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.domain.repository.MessageRepository;
import pnu.classplus.dto.MessageDto;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static pnu.classplus.domain.entity.QMessageEntity.messageEntity;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MemberRepository memberRepo;
    private final MessageRepository messageRepo;
    private final JwtTokenProvider tokenProvider;
    private final JPAQueryFactory queryFactory;

    public ResponseEntity postMessage(HttpServletRequest request, MessageDto dto) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optSender = memberRepo.findByUid(uid);
        if (!optSender.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid sender (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }

        MemberEntity sender = optSender.get();

        Optional<MemberEntity> optReceiver = memberRepo.findById(dto.getReceiver_mem_idx());
        if (!optReceiver.isPresent()) {
            return new ResponseEntity(new ApiResponse(40, "invalid receiver"),
                HttpStatus.BAD_REQUEST);
        }

        MemberEntity receiver = optReceiver.get();

        if (sender.equals(receiver)) {
            return new ResponseEntity(new ApiResponse(41, "sender equals to receiver"),
                HttpStatus.BAD_REQUEST);
        }

        messageRepo.save(MessageEntity.builder()
                                    .sender(sender)
                                    .receiver(receiver)
                                    .message(dto.getMessage())
                                    .build());

        return new ResponseEntity(new ApiResponse(0, "Message Send Successful"),
            HttpStatus.OK);
    }

    public ResponseEntity getMessage(HttpServletRequest request) {
        String uid = tokenProvider.getUserIdFromJwtToken(tokenProvider.resolveToken(request));

        Optional<MemberEntity> optReceiver = memberRepo.findByUid(uid);
        if (!optReceiver.isPresent()) {
            return new ResponseEntity(new ApiResponse(33, "invalid sender (session invalid)"),
                HttpStatus.BAD_REQUEST);
        }

        MemberEntity receiver = optReceiver.get();

        List<MessageEntity> messageEntityList = queryFactory.selectFrom(messageEntity)
                                                            .where(messageEntity.receiver.eq(receiver))
                                                            .fetch();

        if (messageEntityList.isEmpty()) {
            return new ResponseEntity(new ApiResponse(42, "No message"),
                HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(new DataResponse(0, "Message get Successful", messageEntityList),
            HttpStatus.OK);
    }
}
