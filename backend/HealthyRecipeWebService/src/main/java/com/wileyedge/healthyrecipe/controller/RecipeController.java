package com.wileyedge.healthyrecipe.controller;

import java.util.ArrayList;
import java.util.List;

import com.wileyedge.healthyrecipe.model.RecipeDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.service.IRecipeService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

	private IRecipeService recipeService;
	private final String IMAGE_BASE_URL = "https://foodimagesbucket.s3.ap-southeast-2.amazonaws.com/";

	@Autowired
	public RecipeController(IRecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@PostMapping(consumes = { "multipart/form-data" })
	public Recipe createRecipe(@RequestPart("recipe") RecipeDTO recipeDTO,
							   @RequestPart("image") MultipartFile image, @RequestHeader("Authorization") String token) {
	    System.out.println("Inside Recipe Controller ");
		Recipe returnedRecipe = recipeService.createRecipe(recipeDTO, image, token);
		if(!returnedRecipe.getImageUrl().isEmpty()){
			String fullImageUrl = IMAGE_BASE_URL + returnedRecipe.getImageUrl();
			returnedRecipe.setImageUrl(fullImageUrl);
		}
		return returnedRecipe;
	}
		
	@GetMapping
	public List<Recipe> getAllRecipes() {
		List<Recipe> recipes = recipeService.getAllRecipes();
		String fullImageUrl = "";
		for(Recipe recipe: recipes){
			if(recipe != null && !recipe.getImageUrl().isEmpty() ){
				fullImageUrl = IMAGE_BASE_URL + recipe.getImageUrl();
				recipe.setImageUrl(fullImageUrl);
			}
		}
		
		return recipes;
	}
	
	@GetMapping("/{recipeId}")
	public Recipe getRecipesById(@PathVariable long recipeId) {
		Recipe returnedRecipe = recipeService.getRecipeById(recipeId);
		
		if(returnedRecipe!= null && !returnedRecipe.getImageUrl().isEmpty()){
			String fullImageUrl = IMAGE_BASE_URL + returnedRecipe.getImageUrl();
			returnedRecipe.setImageUrl(fullImageUrl);
		}
		
		return returnedRecipe;
	}

	@GetMapping("/users/{userId}")
	public List<Recipe> getRecipesByUserId(@PathVariable long userId) {
		List<Recipe> recipes = recipeService.getRecipesByUserId(userId);
		String fullImageUrl = "";
		for(Recipe recipe: recipes){
			if(recipe != null && !recipe.getImageUrl().isEmpty() ){
				fullImageUrl = IMAGE_BASE_URL + recipe.getImageUrl();
				recipe.setImageUrl(fullImageUrl);
			}
		}
		return recipes;
	}

	@GetMapping("/health")
	public List<Recipe> getRecipesByHealthType(@RequestBody List<String> healthTypes) {
	    List<HealthCategory> healthCategories = new ArrayList<>();
	    for (String healthType : healthTypes) {
	        healthCategories.add(HealthCategory.valueOf(healthType.toUpperCase()));
	    }
	  
	    List<Recipe> recipes = recipeService.getRecipesByHealthCategories(healthCategories);
	    
	    for(Recipe recipe: recipes) {
	    	String fullImageUrl = "";
	    	if(recipe != null && !recipe.getImageUrl().isEmpty()) {
	    		fullImageUrl = IMAGE_BASE_URL + recipe.getImageUrl();
	    	}
	    }
	    
	    return recipes;
	}
	
	@DeleteMapping("/{recipeId}")
	public String deleteRecipe(@PathVariable long recipeId, @RequestHeader("Authorization") String token) {
	    recipeService.deleteRecipe(recipeId, token);
	    return "SUCCESS: The Recipe has been deleted successfully";	
	}

	@PutMapping("/{recipeId}")
	public Recipe updateRecipe(@PathVariable long recipeId, @RequestBody Recipe updatedRecipe, @RequestHeader("Authorization") String token) {
		Recipe returnedRecipe = recipeService.updateRecipe(recipeId, updatedRecipe, token);
		
		if(returnedRecipe!= null && !returnedRecipe.getImageUrl().isEmpty()){
			String fullImageUrl = IMAGE_BASE_URL + returnedRecipe.getImageUrl();
			returnedRecipe.setImageUrl(fullImageUrl);
		}
		
		return returnedRecipe;
	}
	
}

