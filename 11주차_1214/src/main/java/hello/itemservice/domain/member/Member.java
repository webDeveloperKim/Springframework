package hello.itemservice.domain.member;

import lombok.Data;

@Data
public class Member {
	
	private Long id;
	
	private String loginId; // 로그인 ID
	
	private String name; // 회원 이름
		
	private String password;
	
}
