package hello.itemservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

// 스프링 핵심 기능
// 의존성 주입 (생성자 주입, setter 주입, 필드 주입)

@Component
public class TestDataInit {

    private final ItemRepository itemRepository;
    
    @Autowired
    public TestDataInit(ItemRepository itemRepository) {
    	this.itemRepository = itemRepository;
    }
    

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}