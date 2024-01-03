package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 전체 동작 방식을 구성(config)하기 위해,
 * 구현 객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스
 * <p>
 * AppConfig는 구체 클래스를 선택한다. 배역에 맞는 담당배우를 선태한다.
 * 애플리케이션이 어떻게 동작해야 할지 전체 구성을 책임진다.
 */

@Configuration
public class AppConfig {
    /**
     * memberServiceImpl 객체에 memoryMemberRepository 객체를 주입하였다.
     * 이를 '생성자 주입'라고도 한다.
     * @return
     */

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
