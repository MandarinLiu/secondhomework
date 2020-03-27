package com.example.secondhomework;

import java.io.Serializable;

public class Item  {
    String  item_todo;
    String item_date;
    int item_pro;

    public Item(String item_todo, String item_date,int item_pro){
        this.item_todo = item_todo;
        this.item_date =item_date;
        this.item_pro =item_pro;

    }

    public int getItem_pro() {
        return item_pro;
    }

    public String getItem_date() {
        return item_date;
    }

    public String getItem_todo() {
        return item_todo;
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
}
