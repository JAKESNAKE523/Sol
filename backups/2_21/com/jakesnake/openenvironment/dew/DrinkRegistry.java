package com.jakesnake.openenvironment.dew;

import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class DrinkRegistry {

    HashMap<String, Integer> data = new HashMap<String, Integer>();

    public DrinkRegistry (){

    }
    public void RegisterDrink (String id, int value){
        if(data.containsKey(id))
            return;
        data.put(id, value);
    }
    public int getNutrients(String id) {
        if(!data.containsKey(id))
            return 0;
        else return data.get(id);
    }
}
