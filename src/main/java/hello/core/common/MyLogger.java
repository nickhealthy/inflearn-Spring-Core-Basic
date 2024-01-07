package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.UUID;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


/*
* 웹 스코프 - request
* 특정 사용자가 request 요청 시 로그를 남기기 위한 request 스코프(빈)
*
* request 스코프는 HTTP 요청이 왔을 때 생성된다.
* 하지만 Spring 애플리케이션이 기동될 때 스프링 컨테이너 생성 및 DI로 인해 request 스코프를 요청하게 되고(HTTP 요청이 없는 상태),
* request 스코프의 생명주기 시점을 벗어났기 때문에 오류를 발생하게 된다.
* 따라서 앞서 배운 ObjectProvider 를 사용해 스프링 컨테이너에 해당 request 빈을 요청하는 시점을 지연시켜야 한다.
*
* 지연 방법 2가지
*   - Provider 이용
*   - 프록시 모드 ScopredProxyMode.TARGET_CLASS
*
* 핵심은 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다.
 */
@Component
//@Scope(value = "request") // 해당 빈은 HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸됨
// proxyMode를 사용하면 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
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
