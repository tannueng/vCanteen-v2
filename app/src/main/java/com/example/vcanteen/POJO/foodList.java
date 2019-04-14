package com.example.vcanteen.POJO;

public class foodList {
    public int foodId;
    public String foodName;
    public int foodPrice;
    public String foodType;


    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    @Override
    public String toString() {
        return "\"foodId\": "+foodId+
                ", \"foodName\": "+foodName+
                ", \"foodPrice\": "+foodPrice+
                ", \"foodType\": "+foodType;
    }
}
