package com.wileyedge.healthyrecipe.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.dao.UserRepository;
import com.wileyedge.healthyrecipe.exception.InvalidTokenException;
import com.wileyedge.healthyrecipe.exception.UnauthorizedAccessException;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.RecipeDTO;
import com.wileyedge.healthyrecipe.model.User;

class RecipeServiceTest {

	private RecipeRepository recipeRepositoryMock;
	private IAuthService authServiceMock;
	private IImageStorageService imageStorageServiceMock;
	private UserRepository userRepositoryMock;
	private IRecipeService recipeService;

	@BeforeEach
	void setup() {
		recipeRepositoryMock = new RecipeRepositoryMock();
		authServiceMock = new AuthServiceMock();
		imageStorageServiceMock = new ImageStorageServiceMock();
		recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);
	}	
	
	@Test
	void testCreateRecipe_ValidData_Success() {
	    // Mock user data
	    User user = new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");

	    // Mock recipe data
	    RecipeDTO recipeDTO = new RecipeDTO();
	    recipeDTO.setTitle("Chocolate Cake");
	    recipeDTO.setShortDesc("Delicious chocolate cake");

	    // Mock image file
	    MockMultipartFile imageFile = new MockMultipartFile("image.jpg", new byte[0]);

	    // Mock token
	    String token = "valid_token";

	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    when(authServiceMock.isTokenValid(token)).thenReturn(user); // Set up valid token

	    // Set up the imageStorageServiceMock
	    IImageStorageService imageStorageServiceMock = Mockito.mock(IImageStorageService.class);
	    try {
	        when(imageStorageServiceMock.uploadImage(imageFile)).thenReturn("s3Key"); // Set up image upload
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);

	    // Create the recipe
	    Recipe createdRecipe = recipeService.createRecipe(recipeDTO, imageFile, token);

	    // Verify the recipe is created and associated with the user
	    Assertions.assertNotNull(createdRecipe);
	    Assertions.assertEquals(user, createdRecipe.getUser());
	}
	
	@Test
	public void createRecipe_InvalidToken_ThrowsInvalidTokenException() throws IOException {

		// Mock recipe data
		RecipeDTO recipeDTO = new RecipeDTO();
		recipeDTO.setTitle("Chocolate Cake");
		recipeDTO.setShortDesc("Delicious chocolate cake");

		// Mock image file
		MockMultipartFile imageFile = new MockMultipartFile("image.jpg", new byte[0]);

		// Mock invalid token
		String token = "invalid_token";

		// Set up the authServiceMock
		IAuthService authServiceMock = Mockito.mock(IAuthService.class);
		when(authServiceMock.isTokenValid(token)).thenReturn(null); // Set up invalid token

		// Set up the imageStorageServiceMock
		IImageStorageService imageStorageServiceMock = Mockito.mock(IImageStorageService.class);
		try {
			when(imageStorageServiceMock.uploadImage(imageFile)).thenReturn("s3Key"); // Set up image upload
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the recipe service with the mocked dependencies
		IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);

		// Attempt to create the recipe with an invalid token and expect an InvalidTokenException
		try {
			recipeService.createRecipe(recipeDTO, imageFile, token);
			Assertions.fail("Expected InvalidTokenException to be thrown");
		} catch (InvalidTokenException e) {
			Assertions.assertEquals("invalid token", e.getMessage());
		}
	}

	@Test
	void testDeleteRecipe_ValidToken_Success() {
	    // Mock user data
	    User user = new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");
	    
	    // Mock token
	    String token = "valid_token";
	    
	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    doReturn(user).when(authServiceMock).isTokenValid(token); // Set up valid token
	    
	    // Set up the recipeRepositoryMock
	    RecipeRepository recipeRepositoryMock = Mockito.mock(RecipeRepository.class);
	    
	    // Create a recipe with ID 1
	    Recipe recipe = new Recipe();
	    recipe.setRecipeId(1L);
	    recipe.setUser(user);
	    
	    // Set up recipeRepositoryMock to return the recipe when findById is called
	    when(recipeRepositoryMock.findById(1L)).thenReturn(Optional.of(recipe));
	    
	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);
	    
	    // Call the deleteRecipe method
	    Assertions.assertDoesNotThrow(() -> recipeService.deleteRecipe(1L, token));
	    
	    // Verify that the recipe was deleted successfully
	    Mockito.verify(recipeRepositoryMock).delete(recipe);
	}


	@Test
	void testDeleteRecipe_InvalidToken_ThrowsUnauthorizedAccessException() {
	    // Mock token
	    String token = "invalid_token";
	    
	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    doThrow(UnauthorizedAccessException.class).when(authServiceMock).isTokenValid(token); // Set up invalid token
	    
	    // Set up the recipeRepositoryMock
	    RecipeRepository recipeRepositoryMock = Mockito.mock(RecipeRepository.class);
	    
	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);
	    
	    // Call the deleteRecipe method and assert that it throws UnauthorizedAccessException
	    Assertions.assertThrows(UnauthorizedAccessException.class, () -> recipeService.deleteRecipe(1L, token));
	}

	@Test
	void testUpdateRecipe_ValidData_Success() {
	    // Mock user data
	    User user = new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");

	    // Mock token
	    String token = "valid_token";

	    // Create the original recipe
	    Recipe originalRecipe = new Recipe();
	    originalRecipe.setRecipeId(1L);
	    originalRecipe.setUser(user);
	    originalRecipe.setTitle("Original Recipe");
	    originalRecipe.setShortDesc("Original description");
	    originalRecipe.setInstructions("Original instructions");
	    originalRecipe.setCookingDurationInMinutes(30);

	    // Create the updated recipe
	    Recipe updatedRecipe = new Recipe();
	    updatedRecipe.setTitle("Updated Recipe");
	    updatedRecipe.setShortDesc("Updated description");
	    updatedRecipe.setInstructions("Updated instructions");
	    updatedRecipe.setCookingDurationInMinutes(45);

	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    doReturn(user).when(authServiceMock).isTokenValid(token); // Set up valid token

	    // Set up the recipeRepositoryMock
	    RecipeRepository recipeRepositoryMock = Mockito.mock(RecipeRepository.class);
	    doReturn(Optional.of(originalRecipe)).when(recipeRepositoryMock).findById(originalRecipe.getRecipeId());
	    doReturn(updatedRecipe).when(recipeRepositoryMock).save(originalRecipe); // Return the updated recipe

	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);

	    // Call the updateRecipe method
	    Recipe updated = recipeService.updateRecipe(originalRecipe.getRecipeId(), updatedRecipe, token);

	    // Verify the updated recipe matches the expected values
	    Assertions.assertEquals(updatedRecipe.getTitle(), updated.getTitle());
	    Assertions.assertEquals(updatedRecipe.getShortDesc(), updated.getShortDesc());
	    Assertions.assertEquals(updatedRecipe.getIngredients(), updated.getIngredients());
	    Assertions.assertEquals(updatedRecipe.getInstructions(), updated.getInstructions());
	    Assertions.assertEquals(updatedRecipe.getSuitableFor(), updated.getSuitableFor());
	    Assertions.assertEquals(updatedRecipe.getNotSuitableFor(), updated.getNotSuitableFor());
	    Assertions.assertEquals(updatedRecipe.getCookingDurationInMinutes(), updated.getCookingDurationInMinutes());
	}

	
	@Test
	void testUpdateRecipe_InvalidToken_Failure() {
	    // Mock token
	    String invalidToken = "invalid_token";

	    // Create the original recipe
	    Recipe originalRecipe = new Recipe();
	    originalRecipe.setRecipeId(1L);
	    User user = new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");
	    originalRecipe.setUser(user);

	    // Create the updated recipe
	    Recipe updatedRecipe = new Recipe();
	    updatedRecipe.setTitle("Updated Recipe");
	    updatedRecipe.setShortDesc("Updated description");

	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    doThrow(new InvalidTokenException("Invalid token")).when(authServiceMock).isTokenValid(invalidToken);

	    // Set up the recipeRepositoryMock
	    RecipeRepository recipeRepositoryMock = Mockito.mock(RecipeRepository.class);
	    doReturn(Optional.of(originalRecipe)).when(recipeRepositoryMock).findById(originalRecipe.getRecipeId());

	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);

	    // Call the updateRecipe method and expect an InvalidTokenException
	    Assertions.assertThrows(InvalidTokenException.class, () ->
	            recipeService.updateRecipe(originalRecipe.getRecipeId(), updatedRecipe, invalidToken));
	}


	@Test
	void testUpdateRecipe_UserNotFound_Failure() {
	    // Mock token
	    String token = "valid_token";

	    // Create the original recipe
	    Recipe originalRecipe = new Recipe();
	    originalRecipe.setRecipeId(1L);
	    originalRecipe.setUser(null); // Simulate user not found

	    // Create the updated recipe
	    Recipe updatedRecipe = new Recipe();
	    updatedRecipe.setTitle("Updated Recipe");
	    updatedRecipe.setShortDesc("Updated description");

	    // Set up the authServiceMock
	    IAuthService authServiceMock = Mockito.mock(IAuthService.class);
	    User loggedInUser = new User("john.doe", "john.doe@example.com", "password123", "John", "Doe");
	    doReturn(loggedInUser).when(authServiceMock).isTokenValid(token); // Set up valid token

	    // Set up the recipeRepositoryMock
	    RecipeRepository recipeRepositoryMock = Mockito.mock(RecipeRepository.class);
	    doReturn(Optional.of(originalRecipe)).when(recipeRepositoryMock).findById(originalRecipe.getRecipeId());

	    // Create the recipe service with the mocked dependencies
	    IRecipeService recipeService = new RecipeServiceImpl(recipeRepositoryMock, userRepositoryMock, authServiceMock, imageStorageServiceMock);

	    // Call the updateRecipe method and expect an UnauthorizedAccessException
	    Assertions.assertThrows(UnauthorizedAccessException.class, () ->
	            recipeService.updateRecipe(originalRecipe.getRecipeId(), updatedRecipe, token));
	}
	

}
