package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;


/* 9. 빈 스코프 - 싱글톤과 프로토타입 빈을 같이 사용할 때 문제점 */
public class SingletonWithPrototypeTest2 {


    /*
     * 목적: SingletonBean, PrototypeBean Scope 같이 사용할 때 어떤 결과 값이 나오는지 확인합니다.
     * 시나리오: SingletonBean 안에 PrototypeBean이 속해있으며, 원하는 결과는 클라이언트가 요청할 때마다 새로운 프로토타입 빈이 생성되어 addCount를 호출해 count 값은 모두 1이 나와야합니다.
     * 예상 결과: count1 = 1, count2 = 1
     * 결과
     *  - 예상 결과와 다르게 클라이언트 요청마다 다른 PrototypeBean을 생성하는 것이 아닌,
     *  - SingletonBean에서 스프링 컨테이너가 생성되었을 때 의존성 주입(PrototypeBean)을 한번만 받습니다.
     *  - 따라서 클라이언트 요청이 들어올 때마다 같은 프로토타입 빈을 반환하고 count의 값은 계속해서 증가합니다.
     * */
    @Test
    void singletonclientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }


    static class ClientBean {

        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
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
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }



}
