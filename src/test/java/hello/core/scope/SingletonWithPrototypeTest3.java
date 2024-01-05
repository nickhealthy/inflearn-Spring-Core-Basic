package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/* 9. 빈 스코프 - 싱글톤과 프로토타입 빈을 같이 사용할 때 문제점 해결 방법 */

public class SingletonWithPrototypeTest3 {


    /*
     * 목적: SingletonBean, PrototypeBean Scope 같이 사용할 때 나온 문제점을 해결합니다.
     *   - 이전 테스트에서 프로토타입 인스턴스가 개별적이 아닌 같은 인스턴스의 문제
     * 시나리오: SingletonBean 안에 PrototypeBean이 속해있으며, 원하는 결과는 클라이언트가 요청할 때마다 새로운 프로토타입 빈이 생성되어 addCount를 호출해 count 값은 모두 1이 나와야합니다.
     * 예상 결과: count1 = 1, count2 = 1
     * 결과
     *  - 예상대로 결과가 나왔지만 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청하고 있습니다.
     *  - 이렇게 스프링의 애플리케이션 컨텍스트 전체를 주입받게 되면, 스프링 컨테이너에 종속적인 코드가 되고 단위 테스트가 어려워지는 문제가 있습니다.
     *  - (참고) 프로토타입 빈은 조회 시(ac.getBean(PrototypeBean.class) 생성된다.
     * */
    @Test
    void providerTest() {

        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    static class ClientBean {

        private final ApplicationContext ac;

        // 스프링 컨테이너 의존성 주입
        // 생성자가 하나이기 때문에 @Autowired를 붙인 것과 같은 효과
        public ClientBean(ApplicationContext ac) {
            this.ac = ac;
        }

        public int logic() {
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }

    }

    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destory() {
            System.out.println("PrototypeBean.destory");
        }

    }
}
