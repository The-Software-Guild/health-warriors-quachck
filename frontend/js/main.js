const APP_ID = "4262aadd";
const APP_KEY = "4b7086bb3c6f33c3cd914e1948631c5f";

$(document).ready(function () {
  async function fetchFeaturedRecipes() {
    const dietLabels = ["low-carb", "low-fat", "low-sodium", "high-fiber"];
    const promises = dietLabels.map((dietLabel) => fetchRecipe(dietLabel));
    const recipes = await Promise.all(promises);
    displayFeaturedRecipes(recipes, dietLabels);
  }

  async function fetchRecipe(dietLabel) {
    try {
      const response = await fetch(
        `https://api.edamam.com/search?q=${dietLabel}&app_id=${APP_ID}&app_key=${APP_KEY}&from=0&to=20`
      );
      const data = await response.json();
      const hits = data.hits;
      const randomIndex = Math.floor(Math.random() * hits.length);
      return hits[randomIndex];
    } catch (error) {
      console.error("There was a problem with the fetch operation: ", error);
    }
  }

  function formatLabel(label) {
    return label
      .split("-")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  }

  function displayFeaturedRecipes(recipes, dietLabels) {
    recipes.forEach((recipe, index) => {
      const formattedLabel = formatLabel(dietLabels[index]);
      const card = $("<div>")
        .addClass("card clickable-card")
        .css("cursor", "pointer")
        .attr("onclick", `window.open('${recipe.recipe.url}', '_blank')`);

      const img = $("<img>")
        .addClass("card-img-top")
        .attr("src", recipe.recipe.image)
        .attr("alt", recipe.recipe.label);
      const cardBody = $("<div>").addClass("card-body");
      const title = $("<h5>").addClass("card-title").text(recipe.recipe.label);
      const text = $("<p>").addClass("card-text mb-2").text(formattedLabel);

      cardBody.append(title, text);
      card.append(img, cardBody);
      $(`#${dietLabels[index]}`).html(card);
    });
  }

  fetchFeaturedRecipes();
});
