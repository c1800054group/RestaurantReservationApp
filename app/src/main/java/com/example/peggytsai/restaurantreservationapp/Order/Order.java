package com.example.peggytsai.restaurantreservationapp.Order;



public class Order {
    private int typeImage;
    private String type;
    private  int  nextImage;

    public Order(int typeImage, String type, int nextImage) {
        this.typeImage = typeImage;
        this.type = type;
        this.nextImage = nextImage;
    }

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNextImage() {
        return nextImage;
    }

    public void setNextImage(int nextImage) {
        this.nextImage = nextImage;
    }
}
