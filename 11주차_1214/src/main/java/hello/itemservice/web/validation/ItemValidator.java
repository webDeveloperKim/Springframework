package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hello.itemservice.domain.item.Item;


// 컨트롤러에서 작성한 유효성 검증 로직을 
// 따로 관리하는 클래스

@Component
public class ItemValidator implements Validator {
	
	// 넘어온 클래스를 지원하는지 여부확인
//	clazz는 사용자가 임의로 넣은 클래스입니다. 
//	메서드 supports의 매개변수로 Class<?> clazz가 전달되며, 
//	이는 사용자가 호출할 때 넘긴 클래스 객체를 나타냅니다.
//	예를 들어, supports 메서드가 호출되는 코드에서 사용자는 
//	특정 클래스를 clazz로 전달할 수 있습니다. 
//	이 클래스는 Item과 관련이 있을 수도 있고 없을 수도 있습니다. 
//	clazz는 그때그때 다르게 설정됩니다.
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return Item.class.isAssignableFrom(clazz);
	}
	
	
	// 검증 대상 객체와 BindingResult 
	@Override
	public void validate(Object target, Errors errors) {
		
		Item item = (Item)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");
		
		if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			errors.rejectValue("price", "range", new Object[] {1000, 1000000},null);
		}
		
		if(item.getQuantity() == null || item.getQuantity() > 10000) {
			errors.rejectValue("quantity", "max", new Object[] {9999}, null);
		}
		
		// 특정 필드 예외 아닌 전체예외
		if(item.getPrice() != null && item.getQuantity() != null) {
			int resultPrice = item.getPrice() * item.getQuantity();
			if(resultPrice < 10000) {
				errors.reject("totalPriceMin", new Object[] {10000, resultPrice},null);
			}
		}
		
	}
}












