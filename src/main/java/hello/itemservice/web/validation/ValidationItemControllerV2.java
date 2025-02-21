package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static hello.itemservice.Constants.*;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
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
    public String item(@PathVariable long itemId, Model model) {
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
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < MIN_PRICE || item.getPrice() > MAX_PRICE) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 의 범위에서 입력해야 합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > MAX_QUANTITY) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999까지 허용됩니다."));
        }

        // 특정 필드가 아닌 복합 규칙 적용
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < PRICE_MULTI_QUANTITY) {
                bindingResult.addError(new ObjectError("item", "가격 x 수량의 합은 10,000원 이상이야 합니다. 현재값 = " + resultPrice));
            }
        }

        // 검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        // 검증 성공 시
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null,"상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < MIN_PRICE || item.getPrice() > MAX_PRICE) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 의 범위에서 입력해야 합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > MAX_QUANTITY) {
            bindingResult.addError(new FieldError("item", "quantity", item.getPrice(), false, null, null, "수량은 최대 9,999까지 허용됩니다."));
        }

        // 특정 필드가 아닌 복합 규칙 적용
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < PRICE_MULTI_QUANTITY) {
                bindingResult.addError(new ObjectError("item", "가격 x 수량의 합은 10,000원 이상이야 합니다. 현재값 = " + resultPrice));
            }
        }

        // 검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        // 검증 성공 시
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null,null));
        }
        if (item.getPrice() == null || item.getPrice() < MIN_PRICE || item.getPrice() > MAX_PRICE) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{MIN_PRICE, MAX_PRICE}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() > MAX_QUANTITY) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{MAX_QUANTITY}, null));
        }

        // 특정 필드가 아닌 복합 규칙 적용
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < PRICE_MULTI_QUANTITY) {
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"}, new Object[]{PRICE_MULTI_QUANTITY, resultPrice}, null));
            }
        }

        // 검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        // 검증 성공 시
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // 검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//        if (!StringUtils.hasText(item.getItemName())) {
//            bindingResult.rejectValue("itemName", "required");
//        }
        if (item.getPrice() == null || item.getPrice() < MIN_PRICE || item.getPrice() > MAX_PRICE) {
            bindingResult.rejectValue("price", "range", new Object[]{MIN_PRICE, MAX_PRICE}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() > MAX_QUANTITY) {
            bindingResult.rejectValue("quantity", "max", new Object[]{MAX_QUANTITY}, null);
        }

        // 특정 필드가 아닌 복합 규칙 적용
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < PRICE_MULTI_QUANTITY) {
                bindingResult.reject("totalPriceMin", new Object[]{PRICE_MULTI_QUANTITY}, null);
            }
        }

        // 검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        // 검증 성공 시
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        itemValidator.validate(item, bindingResult);

        // 검증 실패 시 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "validation/v2/addForm";
        }

        // 검증 성공 시
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

