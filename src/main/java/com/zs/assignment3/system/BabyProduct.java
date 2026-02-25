package com.zs.assignment3.system;

public class BabyProduct extends Product {
    private String recommendedAge;

    public BabyProduct(String id, String name, double price, String brand, String recommendedAge) {
        super(id, name, price, brand);
        this.recommendedAge = recommendedAge;
    }

    @Override
    public boolean isReturnable() {
        return true;
    }

    public String getRecommendedAge() {
        return recommendedAge;
    }

    public void setRecommendedAge(String age) {
        this.recommendedAge = age;
    }

    @Override
    public String toString() {
        String base = super.toString();
        String safeAge = recommendedAge == null ? "(no-age)" : recommendedAge;
        return base + " | Age: " + safeAge;
    }
}