package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/*
* 결과
* 프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을 조회할 때 생성되고, 초기화 메서드도 실행된다.(아래 코드 참고),
* PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
                * (중요) 이 시점에 초기화 메서드가 실행된다!
                * 즉, 싱글톤과는 다르게 스프링 컨테이너 생성 시점에 초기화 메서드가 실행되는 것이 아닌, 조회 시 초기화 메서드가 실행된다.

    find prototypeBean1
    PrototypeBean.init
    find prototypeBean2
    PrototypeBean.init
    bean1 = hello.core.scope.PrototypeTest$PrototypeBean@bcef303
    bean2 = hello.core.scope.PrototypeTest$PrototypeBean@41709512

* */

public class PrototypeTest {

    @Test
    public void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);

        assertThat(bean1).isNotSameAs(bean2);
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }

        @PreDestroy
        public void destory() {
            System.out.println("PrototypeBean.destory");
        }
    }
}
