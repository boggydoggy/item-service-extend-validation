package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static hello.itemservice.Constants.*;

@Component
public class ItemValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        // 검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < MIN_PRICE || item.getPrice() > MAX_PRICE) {
            errors.rejectValue("price", "range", new Object[]{MIN_PRICE, MAX_PRICE}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() > MAX_QUANTITY) {
            errors.rejectValue("quantity", "max", new Object[]{MAX_QUANTITY}, null);
        }

        // 특정 필드가 아닌 복합 규칙 적용
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < PRICE_MULTI_QUANTITY) {
                errors.reject("totalPriceMin", new Object[]{PRICE_MULTI_QUANTITY}, null);
            }
        }
    }
}
