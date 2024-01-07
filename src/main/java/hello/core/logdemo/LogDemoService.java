package hello.core.logdemo;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;
    // request 스코프 빈은 HTTP 요청이 왔을 때부터 생명주기 시점이 발생하므로, ObjectProvider 를 사용해 필요한 시점에 스프링 컨테이너에 해당 request 빈을 요청해야한다.
    private final ObjectProvider<MyLogger> myLoggerObjectProvider;

    public void logic(String id) {
//        MyLogger myLogger = myLoggerObjectProvider.getObject();

        myLogger.log("service id = " + id);
    }


}
