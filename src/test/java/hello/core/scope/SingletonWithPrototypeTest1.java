package hello.core.scope;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;


/* 9. 빈 스코프 - 싱글톤 VS 프로토타입 빈 스코프 차이 */
public class SingletonWithPrototypeTest1 {

    /*
     * 목적: PrototypeBean Scope 사용시 각 요청마다 다른 인스턴스를 반환하는지 테스트합니다.
     * 시나리오: 각 요청마다 addCount를 한번 호출해 count 값은 모두 1이 나와야합니다.
     * 예상 결과: count1 = 1, count2 = 2 / 초기화 메서드 두번 실행 / 소멸 메서드 실행 x
     * */
    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        int count1 = prototypeBean1.getCount();
        assertThat(count1).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        int count2 = prototypeBean2.getCount();
        assertThat(count2).isEqualTo(1);

        ac.close();
    }

    /*
     * 목적: SingletonBean Scope 사용시 각 요청마다 같은 인스턴스를 반환하는지 테스트합니다.
     * 시나리오: 동일한 인스턴스이므로 각 요청마다 addCount를 한번 호출하면 count 값은 증가해야합니다.
     * 예상 결과: count1 = 1, count2 = 2 / 초기화 메서드 한번 실행 / 소멸 메서드 실행 o
     * */
    @Test
    void singletonFind() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(SingletonBean.class);

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        singletonBean1.addCount();
        int count1 = singletonBean1.getCount();
        assertThat(count1).isEqualTo(1);

        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        singletonBean2.addCount();
        int count2 = singletonBean1.getCount();
        assertThat(count2).isEqualTo(2);

        ac.close();

    }

    @Scope("singleton")
    static class SingletonBean {

        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("SingletonBean.init\t" + this);
        }

        // 프로토타입 스코프 빈은 스프링 컨테이너 생성 -> 빈 생성 -> 초기화 메서드까지만 컨테이너에서 관리하기 때문에 소멸 메서드는 관리하지 않는다.
        @PreDestroy
        public void destory() {
            System.out.println("SingletonBean.destory");
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
            System.out.println("PrototypeBean.init\t" + this);
        }

        // 프로토타입 스코프 빈은 스프링 컨테이너 생성 -> 빈 생성 -> 초기화 메서드까지만 컨테이너에서 관리하기 때문에 소멸 메서드는 관리하지 않는다.
        @PreDestroy
        public void destory() {
            System.out.println("PrototypeBean.destory");
        }
    }


}
