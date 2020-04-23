package com.example.secondhomework;

import java.io.Serializable;

public class Item implements  Serializable {
    String  item_todo;
    String item_date;
    String item_date_end;
    int item_pro;
    byte[] image;//图片字节数组

    public Item(String item_todo, String item_date,String item_date_end,int item_pro,byte[] image){
        this.item_todo = item_todo;
        this.item_date =item_date;
        this.item_pro =item_pro;
        this.item_date_end=item_date_end;
        this.image=image;

    }
//public Item(String item_todo, String item_date,String item_date_end,int item_pro){
//    this.item_todo = item_todo;
//    this.item_date =item_date;
//    this.item_pro =item_pro;
//    this.item_date_end=item_date_end;
//
//
//}
    public int getItem_pro() {
        return item_pro;
    }

    public String getItem_date() {
        return item_date;
    }

    public String getItem_todo() {
        return item_todo;
    }

    public String getItem_date_end() {
        return item_date_end;
    }

    public byte[] getImage() {
        return image;
    }
    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }

    public void setItem_pro(int item_pro) {
        this.item_pro = item_pro;
    }

    public void setItem_todo(String item_todo) {
        this.item_todo = item_todo;
    }

    public void setImage(byte[] image) {
        this.image=image;
    }
    public void setItem_date_end(String item_date_end) {
        this.item_date_end = item_date_end;
    }



}
