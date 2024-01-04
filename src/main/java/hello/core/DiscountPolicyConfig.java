package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* 7. 의존관계 자동 주입 - 자동, 수동의 올바른 실무 기준
*
* - 웬만하면 자동 기능을 기본으로 사용하자
* - 수동 빈을 선택할 경우는(핵심은 애플리케이션에 대한 분석을 다른 사람이 파악할 때 딱 보고 이해가 되도록 하는 것이 중요하다.)
*   - 애플리케이션은 크게 업무/기술 지원 로직으로 나눌 수 있는데 기술 지원 빈 같이 공통 관심사나(AOP) 기술적인 문제를 다룰 때 주로 사용한다.
*   - 따라서 애플리케이션 전반적에 영향을 미치는 '기술 지원 로직'이나 '비즈니스 로직 중에서 다형성을 적극 활요할 때' 설정 정보에 명확하게 보일 수 있도록 수동 빈으로 등록하는 것이 더 좋다.
*   - 수동 빈이 아닌 자동 빈으로 설정할 경우 최소한 '특정 패키지에 같이 묶어'두는 것이 좋다.
*  - 아래는 여러 개의 DiscountPolicy 중에서 어떤 정책들이 있는지 나열하여 한 눈에 애플리케이션을 파악하기 쉽도록 수동 빈으로 설정한 것이다.
* */

/*
* 요약 정리
* 1. 편리한 자동 기능을 기본으로 사용할 것
* 2. 직접 등록하는 기술 지원 객체는 수동 등록
* 3. 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해볼 것
* */
@Configuration
public class DiscountPolicyConfig {

    @Bean
    public DiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }

    @Bean
    public DiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }

}
