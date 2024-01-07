package hello.core.web;

import hello.core.common.MyLogger;
import hello.core.logdemo.LogDemoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/*
    결과:
    [6ac70b3a-6879-438e-b015-d0d92e33e0e6] request scope bean create: hello.core.common.MyLogger@27b9f15f
    [6ac70b3a-6879-438e-b015-d0d92e33e0e6][/log-demo] controller test
    [6ac70b3a-6879-438e-b015-d0d92e33e0e6][/log-demo] service id = testId
    [6ac70b3a-6879-438e-b015-d0d92e33e0e6] request scope bean close: hello.core.common.MyLogger@27b9f15f
 */
@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    // request 스코프 빈은 HTTP 요청이 왔을 때부터 생명주기 시점이 발생하므로, ObjectProvider 를 사용해 스프링 컨테이너에 해당 request 빈을 요청하는 시점을 지연시켜야 한다.
//    private final ObjectProvider<MyLogger> myLoggerObjectProvider;


    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
//        MyLogger myLogger = myLoggerObjectProvider.getObject();


        /*
        * 출력 결과: myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0
        *
        * 1. 'CGLIB'라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 미리 만들어서 의존 관계를 주입한다.
        *   - 스프링 컨터이너에 myLogger 라는 이름으로 진짜 대신에 가짜 프록시 객체를 등록한다.
        *   - CGLIB은 바이트코드를 조작하는 라이브러리
        * 2. 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
        *   - 가짜 프록시 객체는 내부에 진짜 MyLogger 객체를 찾는 방법을 알고 있다.
        *   - 클라이언트가 myLogger.log()을 호출하면 가짜 프록시를, 가짜 프록시는 이때 진짜 myLogger.log()를 호출한다.
        * */
        System.out.println("myLogger = " + myLogger.getClass());
        String requestURL = request.getRequestURI();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test"); // myLogger 빈은 HTTP 요청마다 구별된다.(request 빈)
        logDemoService.logic("testId");
        return "OK";
    }
}
