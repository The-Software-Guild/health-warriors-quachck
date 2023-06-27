const updateRecipeEndpoint = "http://localhost:8282/recipes";
const getRecipeEndpoint = "http://localhost:8282/recipes";

$(document).ready(function () {
  const params = new URLSearchParams(window.location.search);
  const requestParamRecipeId = params.get("recipeId");

  fetchAndDisplayRecipe(requestParamRecipeId);

  $("#update-recipe-form").on("submit", async function (e) {
    e.preventDefault();

    const title = $("#title").val();
    const shortDesc = $("#shortDesc").val();
    const ingredients = $("#ingredients").val();
    const instructions = $("#instructions").val();
    const cookingDurationInMinutes = $("#cookingDurationInMinutes").val();
    const suitableFor = [];
    const notSuitableFor = [];

    $("input[name='suitableFor']:checked").each(function () {
      suitableFor.push($(this).val());
    });
    $("input[name='notSuitableFor']:checked").each(function () {
      notSuitableFor.push($(this).val());
    });

    const updatedRecipe = {
      title,
      shortDesc,
      ingredients,
      instructions,
      cookingDurationInMinutes,
      suitableFor,
      notSuitableFor,
    };

    try {
      const user = JSON.parse(localStorage.getItem("user"));
      const token = user.token;

      const response = await fetch(
        `${updateRecipeEndpoint}/${requestParamRecipeId}`,
        {
          method: "PUT",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(updatedRecipe),
        }
      );

      if (!response.ok) {
        throw new Error("Error in response from server");
      }

      const data = await response.json();
      const recipeId = data.recipeId;

      $("#success-banner").text("Recipe successful updated").show();
      setTimeout(
        () => (window.location.href = `recipe.html?recipeId=${recipeId}`),
        1000
      );
    } catch (error) {
      console.error("Error:", error);
      // Show error to user
    }
  });
});

async function fetchAndDisplayRecipe(recipeId) {
  try {
    const response = await fetch(`${getRecipeEndpoint}/${recipeId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("HTTP error " + response.status);
    }

    const recipe = await response.json();
    $("#title").val(recipe.title);
    $("#shortDesc").val(recipe.shortDesc);
    $("#ingredients").val(recipe.ingredients);
    $("#instructions").val(recipe.instructions);
    $("#cookingDurationInMinutes").val(recipe.cookingDurationInMinutes);

    recipe.suitableFor.forEach((item) => {
      $(`input[name='suitableFor'][value='${item}']`).prop("checked", true);
    });

    recipe.notSuitableFor.forEach((item) => {
      $(`input[name='notSuitableFor'][value='${item}']`).prop("checked", true);
    });
  } catch (error) {
    console.log(error);
  }
}
