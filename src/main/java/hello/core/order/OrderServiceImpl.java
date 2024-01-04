package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 주문 서비스 구현체
 * 주문 생성 요청이 들어오면, 회원 정보를 조회하고, 할인 정책을 적용한 다음 주문 객체를 생성해서 반환
 * 메모리 회원 리포지토리와, 고정 금액 할인 정책을 구현체로 생성
 */

/**
 * DI(의존성 주입) 설계 변경으로 인해 더이상 OrderServiceImpl 클라이언트는 구현에 의존하지 않는다.
 * 마치 배우자가 다른 배우자를 캐스팅하는 '다양한 책임'을 지지 않는다.
 * '의존관계에 대한 고민은 외부에 맡기고, 실행에만 집중한다.'
 * 즉, 해당 클라이언트는 추상(인터페이스)에만 의존한다.
 */

@Component
/*
*  7. 의존관계 자동 주입 - 롬복 적용
* // @RequiredArgsConstructor
* */

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository; // 회원 조회
    private final DiscountPolicy discountPolicy; // 할인 정책 확인

    /*
    * 7. 의존관계 자동 주입 - 애노테이션 직접 만들기
    *
    * @Qualifier 애너테이션을 사용하면 문자열로 지정하는 것이기 때문에 에러를 발생하기 쉽다.
    * 가장 큰 문제는 문자열이기 때문에 컴파일 시점에서 에러를 찾아낼 수가 없다.
    * 따라서 @Qualifier 애너테이션을 지정한 '사용자 정의 애너테이션'을 따로 만든 후,
    * 사용하고 곳에 @Qualifier 사용법처럼 사용자 정의 애너테이션으로 적용해준다면 컴파일 시점에 에러를 확인 가능하다.
    * */
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        /**
         * 주문 결과 반환
         */
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
