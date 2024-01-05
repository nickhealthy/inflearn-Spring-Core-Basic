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
//    private final MyLogger myLogger;
    // request 스코프 빈은 HTTP 요청이 왔을 때부터 생명주기 시점이 발생하므로, ObjectProvider 를 사용해 스프링 컨테이너에 해당 request 빈을 요청하는 시점을 지연시켜야 한다.
    private final ObjectProvider<MyLogger> myLoggerObjectProvider;


    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        MyLogger myLogger = myLoggerObjectProvider.getObject();
        String requestURL = request.getRequestURI();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test"); // myLogger 빈은 HTTP 요청마다 구별된다.(request 빈)
        logDemoService.logic("testId");
        return "OK";
    }
}
