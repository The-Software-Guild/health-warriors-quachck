package com.wileyedge.healthyrecipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Set;

public class RecipeDTO {
    private String title;

    private String shortDesc;

    private String ingredients;

    private String instructions;

    private Set<HealthCategory> suitableFor;

    private Set<HealthCategory> notSuitableFor;

    private int cookingDurationInMinutes;

    private MultipartFile imageFile;


    public RecipeDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Set<HealthCategory> getSuitableFor() {
        return suitableFor;
    }

    public void setSuitableFor(Set<HealthCategory> suitableFor) {
        this.suitableFor = suitableFor;
    }

    public Set<HealthCategory> getNotSuitableFor() {
        return notSuitableFor;
    }

    public void setNotSuitableFor(Set<HealthCategory> notSuitableFor) {
        this.notSuitableFor = notSuitableFor;
    }

    public int getCookingDurationInMinutes() {
        return cookingDurationInMinutes;
    }

    public void setCookingDurationInMinutes(int cookingDurationInMinutes) {
        this.cookingDurationInMinutes = cookingDurationInMinutes;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        return "RecipeDTO{" +
                "title='" + title + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", instructions='" + instructions + '\'' +
                ", suitableFor=" + suitableFor +
                ", notSuitableFor=" + notSuitableFor +
                ", cookingDurationInMinutes=" + cookingDurationInMinutes +
                ", imageFile=" + imageFile +
                '}';
    }
}



