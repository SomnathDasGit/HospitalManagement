<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>

<!-- Link External CSS -->
<link rel="stylesheet" type="text/css" href="css/style.css">

</head>
<body>

    <!-- Header Section -->
    <div class="header">
        <h1>User Login System</h1>
        <p>Please enter your Unique ID and Password</p>
    </div>

    <!-- Login Form Container -->
    <div class="form-container">
        <h2>Login</h2>

        <form action="LoginServlet" method="post">

            <!-- Unique ID -->
            <div class="form-group">
                <label for="uniqueId">Unique ID</label>
                <!-- <input type="text" id="uniqueId" name="uniqueId" value=<%= session.getAttribute("uniqueId") %> required> -->
                    <input type="text" id="uniqueId" name="uniqueId"  required>

            </div>

            <!-- Password -->
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>

            <!-- Submit Button -->
            <button type="submit" class="btn">Login</button>

        </form>
    </div>

</body>
</html>
