package hello.itemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}
// bin 빈 = 클래스 객체
	
	// 빈등록 방식
	// 자동 @component 
	// 직접 @configuration 
}
