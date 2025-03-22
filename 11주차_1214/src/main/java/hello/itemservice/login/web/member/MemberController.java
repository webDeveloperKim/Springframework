package hello.itemservice.login.web.member;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.itemservice.domain.member.Member;
import hello.itemservice.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberRepository memberRepository;
	
	@GetMapping("/add")
	public String addForm(@ModelAttribute Member member) {
		log.info("member={}" , member);
		return "members/addMemberForm";
	}
	

	@PostMapping("/add")
	public String save(@ModelAttribute Member member, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "members/addMemberForm";
		}
		
		memberRepository.save(member); 
		return "redirect:/";
	}

	
	
}










