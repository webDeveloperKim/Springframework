package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
   

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes attributes, Model model) {
    	
//    	model.addAttribute("item", item)
    	
    	// 검증 로직
    	// 상품명 필수 조건
    	if(!StringUtils.hasText(item.getItemName())) {
    		
    		/*
    		objectName : 오류발생한 객체 이름
    		field : 오류 필드명
    		rejectedValue : 사용자가 입력한 값(거절된 오류값)
    		bindingFailure : 타입 오류같은 바인딩 실패인지, 검증실패인지 구분하는 값
    		codes : 메시지 코드
    		arguments : 메세지에서 사용하는 인자
    		defaultMessage : 기본 오류 메세지
    		
    		*/
    		
    		bindingResult.addError(new FieldError("item", "itemName", "상품이름은 필수입니다."));
    	}
    	
    	// 가격 1000이상, 1백만원 이하 조건
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000까지 허용합니다."));
    	}
    	
    	// 수량 최대 9999 조건
    	if(item.getQuantity() == null || item.getQuantity() > 9999) {
    		bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    	}
    	
    	
    	// 특정 필드가 아닌 복합 검증
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int resultPrice = item.getPrice() * item.getQuantity();
    		if(resultPrice < 10000) {
    			bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 =" + resultPrice));
    			log.info("errors={}", bindingResult);
    		}
    	}
    	
    	// 검증실패시 다시 입력폼으로
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("errors", bindingResult);
    		log.info("errors={}", bindingResult);
    		return "validation/v2/addForm";
    	}
    	
    	
    	// 성공시 로직
    	Item savedItem = itemRepository.save(item);
    	attributes.addAttribute("itemId", savedItem.getId());
    	attributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    	
    }
    
    
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes attributes) {
    	itemValidator.validate(item, bindingResult);
    	
    	log.info("{}",bindingResult);
    	
    	if(bindingResult.hasErrors()) {
    		return "validation/v2/addForm";
    	}
    	
    	// 성공시 로직
    	Item savedItem = itemRepository.save(item);
    	attributes.addAttribute("itemId", savedItem.getId());
    	attributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    	
    }
    
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes attributes, Model model) {
    	
    	// 상품명 미입력시
    	if(!StringUtils.hasText(item.getItemName())) {
    		bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[] {"required.item.itemName"}, null , "기본 오류 메세지"));
    	}
    	
    	// 가격 범위 조건
    	if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
    		bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[] {"range.item.price"} , new Object[] {1000,1000000} , null));
    	}
    	
    	// 수량 범위 조건
    	if(item.getQuantity() == null || item.getQuantity() > 10000) {
    		bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[] {"max.item.quantity"}, new Object[] {9999}, null)); 		
    	}
    	
    	// 특정 필드 예외가 아닌 경우 (전체)
    	if(item.getPrice() != null && item.getQuantity() != null) {
    		int resultPrice = item.getPrice() * item.getQuantity();
    		
    		if(resultPrice < 10000) {
    			bindingResult.addError(new ObjectError("item", new String[] {"totalPriceMin"}, new Object[] {10000, resultPrice}, null));
    		}		
    	}
    	
    	// 오류 발생 여부
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("errors", bindingResult);
    		log.info("errors={}", bindingResult);	
    		return "validation/v2/addForm";
    	}
    	
    	// 성공시 로직
    	Item savedItem = itemRepository.save(item);
    	attributes.addAttribute("itemId", savedItem.getId());
    	attributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    	
    }
    
    

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

	

}

