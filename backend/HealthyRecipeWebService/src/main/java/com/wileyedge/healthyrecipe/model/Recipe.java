package com.wileyedge.healthyrecipe.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "recipe")
public class Recipe implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recipeId;
	
	private String title;
	
	@Column(length = 1000)
	private String shortDesc;
	
	@Column(length = 1000)
	private String ingredients;
	
	@Column(length = 1000)
	private String instructions;

	@Enumerated(EnumType.STRING)
	@ElementCollection
	@Column
	private Set<HealthCategory> suitableFor;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection
	@Column
	private Set<HealthCategory> notSuitableFor;
	
	@Column(name = "duration")
	private int cookingDurationInMinutes;


//	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;


	@Transient  // ignore this when send to database jpa
	@JsonIgnore // don't display in json output
	private MultipartFile imageFile;

	@Column(name = "imageUrl")
	private String imageUrl;
	
	@Column
	private LocalDate createdAt;
	
	@Column
	private LocalDate lastUpdated;

	public Recipe() {
	}

	public Recipe(String title, String shortDesc, String ingredients, String instructions,
			Set<HealthCategory> suitableFor, Set<HealthCategory> notSuitableFor, int cookingDurationInMinutes) {
		this.title = title;
		this.shortDesc = shortDesc;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.suitableFor = suitableFor;
		this.notSuitableFor = notSuitableFor;
		this.cookingDurationInMinutes = cookingDurationInMinutes;
	}

	public Recipe(String title, String shortDesc, String ingredients, String instructions,
				  Set<HealthCategory> suitableFor, Set<HealthCategory> notSuitableFor, int cookingDurationInMinutes, MultipartFile imageFile) {
		this.title = title;
		this.shortDesc = shortDesc;
		this.ingredients = ingredients;
		this.instructions = instructions;
		this.suitableFor = suitableFor;
		this.notSuitableFor = notSuitableFor;
		this.cookingDurationInMinutes = cookingDurationInMinutes;
		this.imageFile = imageFile;
	}
	

	public Long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "Recipe [recipeId=" + recipeId + ", title=" + title + ", shortDesc=" + shortDesc + ", ingredients="
				+ ingredients + ", instructions=" + instructions + ", suitableFor=" + suitableFor + ", notSuitableFor="
				+ notSuitableFor + ", cookingDurationInMinutes=" + cookingDurationInMinutes + ", imageUrl=" + imageUrl
				+ "]";
	}


}
