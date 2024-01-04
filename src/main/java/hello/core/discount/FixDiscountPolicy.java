package hello.core.discount;


import hello.core.annotation.MainDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * VIP면 1000원 할인,
 */
@Component
/*
* 7. 의존관계 자동 주입 - @Autowired 필드 명, @Qualifier, @Primary
* // @Qualifier("mainDiscountPolicy")
* @Primary
* */

// 7. 의존관계 자동 주입 - 애노테이션 직접 만들기
@MainDiscountPolicy
public class FixDiscountPolicy implements DiscountPolicy {

    private final int discountFixAmount = 1000; // 1000원 할인

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        } else {
            return 0;
        }
    }
}
