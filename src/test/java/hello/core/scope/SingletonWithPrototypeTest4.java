package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/* 9. 빈 스코프 - 싱글톤과 프로토타입 빈을 같이 사용할 때 문제점 해결 방법 */
public class SingletonWithPrototypeTest4 {


    /*
     * 목적: SingletonBean, PrototypeBean Scope 같이 사용할 때 나온 문제점을 해결합니다.
     *   - 이전 테스트에서 프로토타입 인스턴스가 개별적이 아닌 같은 인스턴스의 문제
     * 시나리오: SingletonBean 안에 PrototypeBean이 속해있으며, 원하는 결과는 클라이언트가 요청할 때마다 새로운 프로토타입 빈이 생성되어 addCount를 호출해 count 값은 모두 1이 나와야합니다.
     * 예상 결과: count1 = 1, count2 = 1
     * 결과
     *  - 앞서 테스트 한 SingletonWithPrototypeTest3 에선 스프링 애플리케이션 컨텍스트 전체를 주입받았지만,
     *  - 현재 테스트한 결과에선 딱 필요한 기능인 DL(Dependency Lookup)으로 필요한 스프링 빈(프로토타입 빈)을 찾아주는 방식으로 사용하였습니다.
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

        @Autowired
        /*
         * 스프링에서 지원하는 DL - ObjectProvider, ObjectFactory
         * 자바 진영에서 공식적으로 지원하는 jakarta.inject.Provider 가 있다.
         * private Provider<PrototypeBean> prototypeBeanProvider;
         *
         * 자바 표준이므로 스프링이 아닌 다른 컨테이너에서도 사용할 수 있다.
         * */

        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic() {
            // 아래 코드로 스프링 컨테이너에서 필요한 스프링 빈을 찾아준다.
            PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
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
