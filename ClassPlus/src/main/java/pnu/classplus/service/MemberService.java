package pnu.classplus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pnu.classplus.domain.entity.MemberEntity;
import pnu.classplus.domain.repository.MemberRepository;
import pnu.classplus.dto.MemberDto;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository repository;

    @Transactional
    public Long save(MemberDto memberDto) {
        return repository.save(memberDto.toEntity()).getIdx();
    }
}
