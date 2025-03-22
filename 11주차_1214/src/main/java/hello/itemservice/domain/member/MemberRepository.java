package hello.itemservice.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MemberRepository {
	
	private static Map<Long, Member> store = new HashMap<>();
	private static long sequence = 0L;
	
	// 회원가입
	public Member save(Member member) {
		member.setId(++sequence);
		log.info("save: member={}", member);
		store.put(member.getId(), member);
		return member;
	}
	
	// 회원 아이디로 1명 조회하기
	public Member findById(Long id) {
		return store.get(id);
	}
	
	public Optional<Member> findByLoginId(String loginId){
		return findAll().stream()
				.filter(m -> m.getLoginId().equals(loginId))
				.findFirst();
	}

	public List<Member> findAll() {
		return new ArrayList<>(store.values());
	}
	
	public void clearStore() {
		store.clear();
	}
}
