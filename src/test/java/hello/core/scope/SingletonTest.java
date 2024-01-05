package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

/*
* 결과
* 싱글톤 빈은 '스프링 컨테이너 컨테이너 생성 시점에 초기화 메서드가 실행된다.(<> 프로토타입과는 다르게 작동함에 주의)
* AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
                SingletonBean.class);
                * (중요) 이 시점에 초기화 메서드가 실행된다!

    SingletonBean.init
    find SingletonBean
    bean1 = hello.core.scope.SingletonTest$SingletonBean@bcef303
    bean2 = hello.core.scope.SingletonTest$SingletonBean@bcef303
    SingletonBean.destory

* */

public class SingletonTest {


    /*
        빈 초기화 메서드를 실행하고,
        같은 인스턴스의 빈을 조회하고,
        종료 메서드까지 정상 호출된 것을 확인할 수 있다.
     */
    @Test
    public void singletonBeanFind() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
                SingletonBean.class);
        System.out.println("find SingletonBean");
        SingletonBean bean1 = ac.getBean(SingletonBean.class);
        SingletonBean bean2 = ac.getBean(SingletonBean.class);
        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);
        assertThat(bean1).isSameAs(bean2);

        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean {

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init");
        }

        @PreDestroy
        public void destory() {
            System.out.println("SingletonBean.destory");
        }

    }

}
