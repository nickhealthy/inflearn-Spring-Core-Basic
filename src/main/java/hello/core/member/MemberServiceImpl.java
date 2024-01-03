package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /**
     * DI(의존성 주입) 설계 변경으로 인해 더이상 MemberServiceImpl 클라이언트는 구현에 의존하지 않는다.
     * 마치 배우자가 다른 배우자를 캐스팅하는 '다양한 책임'을 지지 않는다.
     * '의존관계에 대한 고민은 외부에 맡기고, 실행에만 집중한다.'
     * 즉, 해당 클라이언트는 추상(인터페이스)에만 의존한다.
     * @param memberRepository
     */
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    // 유저 조회
    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
