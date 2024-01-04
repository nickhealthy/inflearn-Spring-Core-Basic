package hello.core.lifecycle;

public class NetworkClient {

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    // 서비스 시작시 호출
    private void connect() {
        System.out.println("connect: " + url);
    }

    private void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }


    /*
     * 스프링 컨테이너 생성 -> 스프링 빈 생성(싱글톤 객체 생성) -> 의존 관계 주입 완료 -> 초기화 콜백 -> 작업
     * 이후 '소멸전 콜백' -> 빈 소멸 전 작업할 메서드
     * */
    /* 한 줄 요약: DisposableBean 은 destroy() 메서드로 소멸을 지원 */
//    @Override
//    public void destroy() throws Exception {
//        disconnect();
//    }


    /*
     * 스프링 컨테이너 생성 -> 스프링 빈 생성(싱글톤 객체 생성) -> 의존 관계 주입 완료
     * 이후 '초기화 콜백' -> 초기화 작업 메서드
     * */
    /* 한 줄 요약: InitializingBean 은 afterPropertiesSet() 메서드로 초기화를 지원 */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        connect();
//        call("초기화 연결 메세지");
//    }

    /* 8.빈 생명주기 콜백 - 빈 등록 초기화, 소멸 메서드 지정 */
    private void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    /* 8.빈 생명주기 콜백 - 빈 등록 초기화, 소멸 메서드 지정 */
    private void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
}
