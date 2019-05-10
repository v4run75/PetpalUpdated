package com.crossapps.petpal.NavigationDrawer;

public class DrawerItem  {
    private int id;
    private int drawable;
    private String optionName;

    public DrawerItem(){ }

    public DrawerItem(int id,int drawable,String optionName){
        this.id=id;
        this.drawable=drawable;
        this.optionName=optionName;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getId() {
        return id;
    }

    public String getOptionName() {
        return optionName;
    }
}
