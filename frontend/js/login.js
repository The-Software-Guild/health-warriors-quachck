const loginEndpoint = "http://localhost:8282/auth/login";
const userEndpoint = "http://localhost:8282/users/username";

$(document).ready(function () {
  $("#login-form").on("submit", async function (e) {
    e.preventDefault();

    const username = $("#username").val();
    const password = $("#password").val();

    try {
      const token = await login(username, password);
      const user = await getUser(username, token);

      const userJson = JSON.stringify({ user, token });
      localStorage.setItem("user", userJson);

      $("#success-banner").text("Log in successful").show();
      setTimeout(() => (window.location.href = "index.html"), 1000);
    } catch (error) {
      console.error("Login failed:", error);
    }
  });
});

async function login(username, password) {
  const response = await fetch(loginEndpoint, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ identifier: username, password }),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error("Login failed: " + message);
  }

  return await response.text();
}

async function getUser(username, token) {
  const response = await fetch(`${userEndpoint}/${username}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Could not retrieve user");
  }

  const userData = await response.json();
  return userData;
}
