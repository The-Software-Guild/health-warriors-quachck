const APP_ID = "4262aadd";
const API_KEY = "4b7086bb3c6f33c3cd914e1948631c5f";

$(document).ready(function () {
  $("#searchButton").click(async function () {
    const recipeName = $("#recipeInput").val().trim();

    if (recipeName === "" && !isAnyFilterChecked()) {
      $("#recipeInput").attr(
        "placeholder",
        "Please fill in at least 1 search option"
      );
      return;
    }

    const recipes = await fetchRecipes(recipeName);
    displayRecipes(recipes);
  });

  $("#slider-range").slider({
    range: true,
    min: 0,
    max: 5000,
    values: [100, 300],
    slide: function (event, ui) {
      $("#amount").val(ui.values[0] + " - " + ui.values[1] + " calories");
    },
  });
  $("#amount").val(
    $("#slider-range").slider("values", 0) +
      " - " +
      $("#slider-range").slider("values", 1) +
      " calories"
  );
});

async function fetchRecipes(name) {
  const healthFilters = $("input[name=health]:checked")
    .map(function () {
      return `health=${this.value}`;
    })
    .get()
    .join("&");

  const dietFilters = $("input[name=diet]:checked")
    .map(function () {
      return `diet=${this.value}`;
    })
    .get()
    .join("&");

  const filterQueries = [healthFilters, dietFilters].filter(Boolean).join("&");
  const calories = $("#slider-range").slider("values");

  console.log(
    `https://api.edamam.com/search?q=${name}&app_id=${APP_ID}&app_key=${API_KEY}&from=0&to=10&${filterQueries}&calories=${calories[0]}-${calories[1]}`
  );

  try {
    const response = await fetch(
      `https://api.edamam.com/search?q=${name}&app_id=${APP_ID}&app_key=${API_KEY}&from=0&to=10&${filterQueries}&calories=${calories[0]}-${calories[1]}`
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data.hits;
  } catch (error) {
    console.error("There was a problem with the fetch operation: ", error);
  }
}

function displayRecipes(recipes) {
  $("#recipeContainer").empty();

  recipes.forEach((recipeData) => {
    const recipe = recipeData.recipe;

    // Get the calories and round down to nearest whole number
    const mealType = recipe.mealType[0];
    const capitalizedMealType =
      mealType.charAt(0).toUpperCase() + mealType.slice(1);

    // Create card components
    const cardDiv = $("<div>").addClass("col-sm-6 col-md-4 col-lg-3 mb-4");
    const card = $("<div>")
      .addClass("card clickable-card")
      .css("cursor", "pointer")
      .attr("onclick", `window.open('${recipe.url}', '_blank')`);
    const img = $("<img>")
      .addClass("card-img-top")
      .attr("src", recipe.image)
      .attr("alt", recipe.label);
    const cardBody = $("<div>").addClass("card-body");
    const cardTitle = $("<h5>").addClass("card-title").text(recipe.label);
    const text = $("<p>").addClass("card-text mb-2").text(capitalizedMealType);

    // Assemble card
    cardBody.append(cardTitle, text);
    card.append(img, cardBody);
    cardDiv.append(card);

    // Add card to container
    $("#recipeContainer").append(cardDiv);
  });
}

function isAnyFilterChecked() {
  return (
    $("input[name=health]:checked").length > 0 ||
    $("input[name=diet]:checked").length > 0
  );
}
