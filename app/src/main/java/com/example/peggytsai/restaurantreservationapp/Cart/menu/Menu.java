package com.example.peggytsai.restaurantreservationapp.Cart.menu;

import java.io.Serializable;


public class Menu implements Serializable {

        private  int id;
        private  String name;
        private  String price;
        private  int type;

        private  String note = "";
        private  int stock = 0;

        public Menu(){

        }

        public Menu getMenu() {
        return this;
    }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof OrderMenu)) {
                return false;
            }

            return this.getId() == ((Menu) obj).getId();
        }
        
        
        
        public Menu(int id, String name, String price, int type, int stock) {
			super();
			this.id = id;
			this.name = name;
			this.price = price;
			this.type = type;
			this.stock = stock;
		}

        public Menu(String name, String price, int type, int stock) {
            this.name = name;
            this.price = price;
            this.type = type;
            this.stock = stock;
        }

        public Menu(String name, String price, int type) {
			super();
			this.name = name;
			this.price = price;
			this.type = type;
		}

		public Menu(int id, String name, String price, int type) {
			super();
			this.id = id;
			this.name = name;
			this.price = price;
			this.type = type;
		}
		
		public Menu(String name, String price) {
	        this.name = name;
	        this.price = price;
	    }
		
		public Menu(int id, String name, String price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public Menu(int id, String name, String price, int type, String note, int remain) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.type = type;
            this.note = note;
//            this.image = image;
            this.stock = remain;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

//        public byte[] getImage() {
//            return image;
//        }
//
//        public void setImage(byte[] image) {
//            this.image = image;
//        }

        public int getStock() {
            return stock;
        }

        public void setStock(int remain) {
            this.stock = remain;
        }
}
