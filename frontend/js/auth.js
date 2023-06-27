$(document).ready(function () {
  // Check if user is logged in
  const user = getUserFromLocalStorage(); // Function to get user info from local storage
  if (user) {
    // User is logged in
    showLoggedInUser(user);
  } else {
    // User is not logged in
    showLoggedOutUser();
  }

  $("#user-nav").on("click", "#logout-button", function (e) {
    e.preventDefault();
    // Clear user data from local storage
    localStorage.removeItem("user");

    // Restore login/register links and remove welcome message
    $("#welcome-message").text("");
    $("#login-button").show();
    $("#signup-button").show();
    $("#logout-button").hide();

    $("#success-banner").text("Logout successful").show().delay(2000).fadeOut();
    window.location.href = "index.html";
  });
});

function showLoggedInUser(user) {
  const userNav = $("#user-nav");
  const welcomeHeader = $("#welcome-header");
  welcomeHeader.text(`Welcome, ${user.user.firstName}`);

  $("#login-button").hide();
  $("#signup-button").hide();
  $("#create-recipe-btn").show();

  const logoutNav = $("<li>").addClass("nav-item").attr("id", "logout-button");
  userNav.append(logoutNav);
  logoutNav.append(
    $("<a>").addClass("nav-link").attr("href", "index.html").text("Logout")
  );
}

function showLoggedOutUser() {
  $("#user-recipes").hide();
}

function getUserFromLocalStorage() {
  const userJson = localStorage.getItem("user");
  return JSON.parse(userJson);
}
