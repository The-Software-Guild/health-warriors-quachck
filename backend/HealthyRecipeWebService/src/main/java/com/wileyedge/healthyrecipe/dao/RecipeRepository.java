package com.wileyedge.healthyrecipe.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	
	List<Recipe> findByUserId(long userId);
	
	List<Recipe> findBySuitableForIn(List<HealthCategory> healthCategories);

}
