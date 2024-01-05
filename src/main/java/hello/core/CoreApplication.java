package hello.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
// DiscountPolicyConfig 애플리케이션 전반에서 다형성이 적극 필요한 빈으로써
// 수동으로 등록한 빈을 제외하는 코드 - WEB 어플리케이션이 기동하고 ComponentScan 시 다른 Component와 충돌됨
@ComponentScan(
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = DiscountPolicyConfig.class)
)
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
