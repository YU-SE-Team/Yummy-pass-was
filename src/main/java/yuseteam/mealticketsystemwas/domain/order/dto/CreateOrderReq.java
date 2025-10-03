package yuseteam.mealticketsystemwas.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderReq {
    private List<Item> items;

    @Getter
    public static class Item {
        private Long menuId;
        private int quantity;
    }
}