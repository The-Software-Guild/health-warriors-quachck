package com.wileyedge.healthyrecipe.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.RecipeNotFoundException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.exception.UserNotFoundException;
import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.RecipeDTO;
import com.wileyedge.healthyrecipe.model.User;

@Service
public class RecipeServiceImpl implements IRecipeService {

	private RecipeRepository recipeRepository;
	private UserRepository userRepository;
	private IAuthService authService;

	private IImageStorageService imageStorageService;

	@Autowired
	public RecipeServiceImpl(RecipeRepository recipeRepository,UserRepository userRepository, IAuthService authService, IImageStorageService imageStorageService) {
		this.recipeRepository = recipeRepository;
		this.userRepository = userRepository;
		this.authService = authService;
		this.imageStorageService = imageStorageService;
	}

	@Override
	public List<Recipe> getAllRecipes(){
		return recipeRepository.findAll();
	}

	@Override
	public Recipe getRecipeById(long recipeId) {
		return recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));
	}


	@Override
	public List<Recipe> getRecipesByUserId(long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (userOptional.isPresent()) {
			return recipeRepository.findByUserId(userId);
		}
		throw new UserNotFoundException("User ID : " + userId);
	}


	@Override
	public List<Recipe> getRecipesByHealthCategories(List<HealthCategory> healthCategories) {
		return recipeRepository.findBySuitableForIn(healthCategories);
	}

	@Override
	public Recipe createRecipe(RecipeDTO recipeDTO, MultipartFile image, String token) {
		System.out.println("Inside createRecipe of Service layer");

		// Validate token
		User loggedInUser = authService.isTokenValid(token);
		if(loggedInUser == null) {
			throw new InvalidTokenException("invalid token");
		}

		Recipe recipe = new Recipe();

		// Upload image file to S3 and get the S3 key
		String s3Key = "";
		try {
			System.out.println("Image in Service layer: " + image);
			s3Key = imageStorageService.uploadImage(image);
			System.out.println("After creating S3: " + s3Key);
			recipe.setImageFile(null);
			recipe.setImageUrl(s3Key);
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null if an exception occurs during image upload
		}

		// Associate user with recipe
		recipe.setUser(loggedInUser);

		// Set all fields of recipe
		recipe.setIngredients(recipeDTO.getIngredients());
		recipe.setInstructions(recipeDTO.getInstructions());
		recipe.setTitle(recipeDTO.getTitle());
		recipe.setSuitableFor(recipeDTO.getSuitableFor());
		recipe.setNotSuitableFor(recipeDTO.getNotSuitableFor());
		recipe.setCookingDurationInMinutes(recipeDTO.getCookingDurationInMinutes());
		recipe.setShortDesc(recipeDTO.getShortDesc());
		
		// Set createdDate to current time
		recipe.setCreatedAt(LocalDate.now());

		return recipeRepository.save(recipe);
	}


	@Override
	public Recipe updateRecipe(long recipeId, Recipe updatedRecipe, String token) {
		// Validate token
		User loggedInUser = authService.isTokenValid(token);

		// Get the recipe by ID
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));

		// Check if the logged-in user is the owner of the recipe
		if (recipe.getUser() == null || !recipe.getUser().equals(loggedInUser)) {
			throw new UnauthorizedAccessException("You are not authorized to update this recipe");
		}

		// Update the recipe details if provided and not empty
		if (updatedRecipe.getTitle() != null && !updatedRecipe.getTitle().isEmpty()) {
			recipe.setTitle(updatedRecipe.getTitle());
		}
		if (updatedRecipe.getShortDesc() != null && !updatedRecipe.getShortDesc().isEmpty()) {
			recipe.setShortDesc(updatedRecipe.getShortDesc());
		}
		if (updatedRecipe.getIngredients() != null && !updatedRecipe.getIngredients().isEmpty()) {
			recipe.setIngredients(updatedRecipe.getIngredients());
		}
		if (updatedRecipe.getInstructions() != null && !updatedRecipe.getInstructions().isEmpty()) {
			recipe.setInstructions(updatedRecipe.getInstructions());
		}
		if (updatedRecipe.getSuitableFor() != null && !updatedRecipe.getSuitableFor().isEmpty()) {
			recipe.setSuitableFor(updatedRecipe.getSuitableFor());
		}
		if (updatedRecipe.getNotSuitableFor() != null && !updatedRecipe.getNotSuitableFor().isEmpty()) {
			recipe.setNotSuitableFor(updatedRecipe.getNotSuitableFor());
		}
		if (updatedRecipe.getCookingDurationInMinutes() != 0) {
			recipe.setCookingDurationInMinutes(updatedRecipe.getCookingDurationInMinutes());
		}
			
		// Set updatedDate to current time
		recipe.setLastUpdated(LocalDate.now());

		return recipeRepository.save(recipe);
	}

	@Override
	public void deleteRecipe(long recipeId, String token) {
		// Validate token
		User loggedInUser = authService.isTokenValid(token);

		// Get the recipe by ID
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new RecipeNotFoundException("ID : " + recipeId));

		// Check if the logged-in user is the owner of the recipe
		if (!recipe.getUser().equals(loggedInUser)) {
			throw new UnauthorizedAccessException("You are not authorized to delete this recipe");
		}

		recipeRepository.delete(recipe);
	}


}
