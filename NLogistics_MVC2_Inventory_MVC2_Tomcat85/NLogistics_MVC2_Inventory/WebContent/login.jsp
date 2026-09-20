<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Login | N Logistics</title>

<style> * { box-sizing: border-box; margin: 0; padding: 0; font-family: Arial, sans-serif; } body { background: #f4f6f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; } .login-container { width: 380px; background: white; padding: 35px; border-radius: 10px; box-shadow: 0 5px 25px rgba(0,0,0,0.12); } .brand { text-align: center; font-size: 28px; font-weight: bold; margin-bottom: 8px; } .brand span { color: #2563eb; } .subtitle { text-align: center; color: #777; margin-bottom: 25px; } .form-group { margin-bottom: 18px; } label { display: block; margin-bottom: 7px; font-weight: bold; color: #333; } input { width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; } input:focus { outline: none; border-color: #2563eb; } .btn { width: 100%; padding: 12px; border: none; border-radius: 6px; background: #2563eb; color: white; font-size: 16px; cursor: pointer; } .btn:hover { background: #1d4ed8; } .register { text-align: center; margin-top: 20px; color: #666; } .register a { color: #2563eb; text-decoration: none; font-weight: bold; } .error { background: #fee2e2; color: #b91c1c; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center; } .success { background: #dcfce7; color: #166534; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center; } </style>

</head>

<body>

<div class="login-container">

<div class="brand">
    N <span>Logistics</span>
</div>

<div class="subtitle">
    Inventory Management System
</div>

<% if (request.getParameter("registered") != null) { %>

    <div class="success">
        Registration completed. Please login.
    </div>

<% } %>

<div id="errorMessage"
     class="error"
     style="display:none;">
</div>

<form id="loginForm">

    <div class="form-group">

        <label>Username</label>

        <input type="text"
               id="username"
               placeholder="Enter username"
               required>

    </div>

    <div class="form-group">

        <label>Password</label>

        <input type="password"
               id="password"
               placeholder="Enter password"
               required>

    </div>

    <button type="submit" class="btn">
        Login
    </button>

</form>

<div class="register">

    Don't have an account?

    <a href="register.jsp">
        Register
    </a>

</div>

</div>

<script> document.getElementById("loginForm").addEventListener("submit", function(event) { event.preventDefault(); var username = document.getElementById("username").value.trim(); var password = document.getElementById("password").value; var errorMessage = document.getElementById("errorMessage"); /* * Get registered user from localStorage */ var storedUser = localStorage.getItem("nLogisticsUser"); /* * No registration found */ if (storedUser === null) { errorMessage.innerHTML = "No registered user found. Please register first."; errorMessage.style.display = "block"; return; } /* * Convert stored JSON back into object */ var user = JSON.parse(storedUser); /* * Compare username and password */ if (username === user.username && password === user.password) { /* * Send credentials to LoginController. * * LoginController will create * the Java server session. */ var form = document.createElement("form"); form.method = "POST"; form.action = "LoginController"; var usernameInput = document.createElement("input"); usernameInput.type = "hidden"; usernameInput.name = "username"; usernameInput.value = username; form.appendChild(usernameInput); document.body.appendChild(form); form.submit(); } else { errorMessage.innerHTML = "Invalid username or password."; errorMessage.style.display = "block"; } }); </script>

</body>

</html>