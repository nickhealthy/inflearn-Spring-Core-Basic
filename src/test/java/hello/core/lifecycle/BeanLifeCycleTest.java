package hello.core.lifecycle;

import org.junit.jupiter.api.Test;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(
                LifeCycleConfig.class);
        LifeCycleConfig client = ac.getBean(LifeCycleConfig.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig {

        /* 8.빈 생명주기 콜백 - 빈 등록 초기화, 소멸 메서드 지정
        * 설정 정보에 빈 생명주기 초기화/소멸 메서드 지정
        */
        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
