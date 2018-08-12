package com.example.peggytsai.restaurantreservationapp.Cart.menu;


import com.example.peggytsai.restaurantreservationapp.Cart.menu.Menu;

@SuppressWarnings("serial")
public class OrderMenu extends Menu {
//    private int OrderMenuId; //沒用
//    private int order_id;    // 好像也用不到
    private int quantity;

    public OrderMenu(int id, String name, String price, int type, String note, int remain) {
        super(id,  name, price,  type,  note,  remain);
        this.quantity = quantity;
    }

    public OrderMenu(Menu menu, int quantity) {
        this(menu.getId(),menu.getName(),menu.getPrice(),menu.getType(),menu.getNote(),menu.getStock());
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
