package com.jakesnake.openenvironment.dew;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DrinkRegistry {

    HashMap<Item, Integer> data = new HashMap<Item, Integer>();

    public DrinkRegistry (){

    }
    public void RegisterDrink (Item item, int value){
        if(data.containsKey(item))
            return;
        data.put(item, value);
    }
    public int getNutrients(Item item) {
        if(!data.containsKey(item))
            return 0;
        else return data.get(item);
    }
}
