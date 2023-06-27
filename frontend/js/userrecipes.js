const getRecipesByUserEndpoint = "http://localhost:8282/recipes/users/";
const deleteRecipeEndpoint = "http://localhost:8282/recipes/";

const userJson = localStorage.getItem("user");
const user = JSON.parse(userJson);
$(document).ready(function () {
  fetchUserRecipes();
});

const fetchUserRecipes = async () => {
  try {
    const response = await fetch(`${getRecipesByUserEndpoint}${user.user.id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipes = await response.json();
    displayRecipes(recipes);
  } catch (error) {
    console.log(error);
  }
};

const displayRecipes = (recipes) => {
  if (recipes.length === 0) {
    $("#recipes-msg").hide();
    $("#no-recipes-msg").show();
  }
  const recipeContainer = $("#userRecipeContainer");

  recipes.forEach((recipe) => {
    // Create the column div
    const colDiv = $("<div>").addClass("col");

    // Create the card div
    const cardDiv = $("<div>")
      .addClass("card h-100 clickable-card")
      .css("cursor", "pointer")
      .on("click", function (event) {
        if (!$(event.target).closest("button").length) {
          window.location.href = `recipe.html?recipeId=${recipe.recipeId}`;
        }
      });

    // Create the image tag and append to card div
    const imgTag = $("<img>")
      .addClass("card-img-top")
      .attr("src", recipe.imageUrl)
      .attr("alt", "Recipe image")
      .appendTo(cardDiv);

    // Create the card body div and append to card div
    const cardBodyDiv = $("<div>").addClass("card-body position-relative");

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

    // Create the button div and append to card body div
    const buttonDiv = $("<div>")
      .addClass("position-absolute top-0 end-0 pt-2 pr-2")
      .appendTo(cardBodyDiv);

    // Create the edit button and append to button div
    $("<button>")
      .addClass("btn btn-primary mr-2")
      .text("Edit")
      .attr(
        "onclick",
        `window.location.href='updateRecipe.html?recipeId=${recipe.recipeId}'`
      )
      .appendTo(buttonDiv);

    // Create the delete button and append to button div
    $("<button>")
      .addClass("btn btn-danger")
      .text("Delete")
      .attr("onclick", `deleteRecipe(event, '${recipe.recipeId}')`)
      .appendTo(buttonDiv);

    // Append the card body div to the card div
    cardBodyDiv.appendTo(cardDiv);

    // Append the card div to the column div
    cardDiv.appendTo(colDiv);

    // Append the column div to the container
    recipeContainer.append(colDiv);
  });
};

async function deleteRecipe(event, recipeId) {
  event.stopPropagation(); // To prevent triggering the onclick event of the parent card

  if (confirm("Are you sure you want to delete this recipe?")) {
    try {
      const response = await fetch(`${deleteRecipeEndpoint}${recipeId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`HTTP error ${response.status}`);
      }

      const responseData = await response.text();

      if (
        responseData === "SUCCESS: The Recipe has been deleted successfully"
      ) {
        alert("Recipe deleted successfully.");
        location.reload(); // Reload the page to reflect the changes
      } else {
        throw new Error("Error in deleting the recipe");
      }
    } catch (error) {
      console.error(error);
      alert("Failed to delete the recipe.");
    }
  }
}
