<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>

<html> <head>

<meta charset="UTF-8">

<title>Registration | N Logistics</title>

<style> * { box-sizing: border-box; margin: 0; padding: 0; font-family: Arial, sans-serif; } body { background: #f4f6f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; } .register-container { width: 400px; background: white; padding: 35px; border-radius: 10px; box-shadow: 0 5px 25px rgba(0,0,0,0.12); } .brand { text-align: center; font-size: 28px; font-weight: bold; margin-bottom: 8px; } .brand span { color: #2563eb; } .subtitle { text-align: center; color: #777; margin-bottom: 25px; } .form-group { margin-bottom: 16px; } label { display: block; margin-bottom: 7px; font-weight: bold; color: #333; } input { width: 100%; padding: 11px; border: 1px solid #ccc; border-radius: 6px; font-size: 14px; } input:focus { outline: none; border-color: #2563eb; } .btn { width: 100%; padding: 12px; border: none; border-radius: 6px; background: #2563eb; color: white; font-size: 16px; cursor: pointer; } .btn:hover { background: #1d4ed8; } .login { text-align: center; margin-top: 20px; color: #666; } .login a { color: #2563eb; text-decoration: none; font-weight: bold; } .error { background: #fee2e2; color: #b91c1c; padding: 10px; border-radius: 5px; margin-bottom: 15px; text-align: center; } </style>

</head>

<body>

<div class="register-container">

<div class="brand">
    N <span>Logistics</span>
</div>

<div class="subtitle">
    Create Your Account
</div>

<div id="errorMessage" class="error" style="display:none;"></div>

<form id="registerForm">

    <div class="form-group">
        <label>Full Name</label>
        <input type="text"
               id="name"
               placeholder="Enter full name"
               required>
    </div>

    <div class="form-group">
        <label>Email</label>
        <input type="email"
               id="email"
               placeholder="Enter email"
               required>
    </div>

    <div class="form-group">
        <label>Username</label>
        <input type="text"
               id="username"
               placeholder="Choose username"
               required>
    </div>

    <div class="form-group">
        <label>Password</label>
        <input type="password"
               id="password"
               placeholder="Create password"
               required>
    </div>

    <div class="form-group">
        <label>Confirm Password</label>
        <input type="password"
               id="confirmPassword"
               placeholder="Confirm password"
               required>
    </div>

    <button type="submit" class="btn">
        Register
    </button>

</form>

<div class="login">
    Already have an account?
    <a href="login.jsp">Login</a>
</div>

</div>

<script> document.getElementById("registerForm").addEventListener("submit", function(event) { event.preventDefault(); var name = document.getElementById("name").value.trim(); var email = document.getElementById("email").value.trim(); var username = document.getElementById("username").value.trim(); var password = document.getElementById("password").value; var confirmPassword = document.getElementById("confirmPassword").value; var errorMessage = document.getElementById("errorMessage"); /* * Check password confirmation */ if (password !== confirmPassword) { errorMessage.innerHTML = "Passwords do not match."; errorMessage.style.display = "block"; return; } /* * Save registration data in browser localStorage */ var user = { name: name, email: email, username: username, password: password }; localStorage.setItem("nLogisticsUser", JSON.stringify(user)); /* * Go to login page */ window.location.href = "login.jsp?registered=true"; }); </script>

</body> </html>