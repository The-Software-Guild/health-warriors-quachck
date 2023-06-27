const getAllRecipesEndpoint = "http://localhost:8282/recipes";

$(document).ready(function () {
  fetchRecipes();
});

const fetchRecipes = async () => {
  try {
    const response = await fetch(getAllRecipesEndpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipes = await response.json();
    console.log(recipes[0].user)
    displayRecipes(recipes);
  } catch (error) {
    console.log(error);
  }
};

const displayRecipes = (recipes) => {
  const recipeContainer = $("#sharedRecipeContainer");

  recipes.forEach((recipe) => {
    console.log(recipe)
    // Create the column div
    const colDiv = $("<div>").addClass("col");

    // Create the card div
    const cardDiv = $("<div>")
      .addClass("card h-100 clickable-card")
      .css("cursor", "pointer")
      .attr(
        "onclick",
        `window.location.href='recipe.html?recipeId=${recipe.recipeId}'`
      );

    // Create the image tag and append to card div
    const imgTag = $("<img>")
      .addClass("card-img-top")
      .attr("src", recipe.imageUrl)
      .attr("alt", "Recipe image")
      .appendTo(cardDiv);

    // Create the card body div and append to card div
    const cardBodyDiv = $("<div>").addClass("card-body");

    // Create the card title and append to card body div
    $("<h5>").addClass("card-title").text(recipe.title).appendTo(cardBodyDiv);

    // Create the card text and append to card body div
    $("<p>").text(recipe.shortDesc).appendTo(cardBodyDiv);

    // Create the footer and append to card body div
    $("<footer>")
      .addClass("blockquote-footer card-text mb-1")
      .text("Recipe by ")
      .append(
        $("<cite>").attr("title", "Source Title").text(recipe.user.username)
      )
      .appendTo(cardBodyDiv);

    // Append the card body div to the card div
    cardBodyDiv.appendTo(cardDiv);

    // Append the card div to the column div
    cardDiv.appendTo(colDiv);

    // Append the column div to the container
    recipeContainer.append(colDiv);
  });
};
