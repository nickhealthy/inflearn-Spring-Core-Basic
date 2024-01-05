package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.UUID;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/*
* 웹 스코프 - request
* 특정 사용자가 request 요청 시 로그를 남기기 위한 request 스코프(빈)
*
* request 스코프는 HTTP 요청이 왔을 때 생성된다.
* 하지만 Spring 애플리케이션이 기동될 때 스프링 컨테이너 생성 및 DI로 인해 request 스코프를 요청하게 되고(HTTP 요청이 없는 상태),
* request 스코프의 생명주기 시점을 벗어났기 때문에 오류를 발생하게 된다.
* 따라서 앞서 배운 ObjectProvider 를 사용해 스프링 컨테이너에 해당 request 빈을 요청하는 시점을 지연시켜야 한다.
 */
@Component
@Scope(value = "request") // 해당 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸됨
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message) {
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        // 이 빈은 HTTP 요청당 하나씩 생성되므로, uuid를 저장해 다른 HTTP 요청과 구별함
        this.uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);

    }

    @PreDestroy
    public void close() {
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }
}
