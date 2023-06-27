const imgUrl = "https://foodimagesbucket.s3.ap-southeast-2.amazonaws.com/";

$(document).ready(async function () {
  const urlParams = new URLSearchParams(window.location.search);
  const recipeId = urlParams.get("recipeId");

  try {
    const response = await fetch(`http://localhost:8282/recipes/${recipeId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${getToken()}`,
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipe = await response.json();

    // Split the ingredients and instructions by newline character
    let ingredients = recipe.ingredients.split("\n");
    let instructions = recipe.instructions.split("\n");

    // Assign values to the elements
    $("#recipe-title").text(
      `${recipe.title} - Recipe by ${recipe.user.username}`
    );
    $("#recipe-img").attr({
      class: "img-fluid mb-3",
      src: recipe.imageUrl,
      alt: recipe.title,
    });
    $("#recipe-shortDesc").text(recipe.shortDesc);
    // Loop over each ingredient and append to the list
    $("#recipe-ingredients").append(
      $("<h4>").text("Ingredients"),
      ...ingredients.map((item) => $("<p>").text(item))
    );

    // Loop over each instruction and append to the list
    $("#recipe-instructions").append(
      $("<h4>").text("Instructions"),
      ...instructions.map((item) => $("<p>").text(item))
    );
    $("#recipe-suitableFor").append(
      $("<h4>").text("Suitable For"),
      $("<ul>").append(
        recipe.suitableFor.map((category) =>
          $("<li>").text(formatCategory(category))
        )
      )
    );
    $("#recipe-notSuitableFor").append(
      $("<h4>").text("Not Suitable For"),
      $("<ul>").append(
        recipe.notSuitableFor.map((category) =>
          $("<li>").text(formatCategory(category))
        )
      )
    );

    $("#recipe-cookingDuration").text(
      `Cooking duration: ${recipe.cookingDurationInMinutes} minutes`
    );
  } catch (error) {
    console.log(error);
  }
});

function getToken() {
  const user = JSON.parse(localStorage.getItem("user"));
  return user.token;
}

function formatCategory(category) {
  return category
    .split("_") // Split by underscore
    .map((word) => word[0].toUpperCase() + word.substr(1).toLowerCase()) // Capitalize first letter of each word
    .join(" "); // Join them back with space
}
