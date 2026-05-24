<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Patient Registration</title>
</head>
<body>
<h2>Patient Registration</h2>

<!-- Display error messages -->
<div th:if="${error}" style="color:red">
    <p th:text="${error}"></p>
</div>

<!-- Display success message -->
<div th:if="${message}" style="color:green">
    <p th:text="${message}"></p>
</div>

<form th:action="@{/register}" th:object="${paitent}" method="post">

<!-- CSRF Token for Spring Security -->
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>

    <div>
        <label>First XXXXX Name:</label>
        <input type="text" th:field="*{firstName}" required/>
    </div>
    <div>
        <label>Middle Name:</label>
        <input type="text" th:field="*{middleName}"/>
    </div>
    <div>
        <label>Last Name:</label>
        <input type="text" th:field="*{lastName}" required/>
    </div>
    <div>
        <label>Date of Birth:</label>
        <input type="date" th:field="*{dob}" required/>
    </div>
    <div>
        <label>Address:</label>
        <input type="text" th:field="*{address}" required/>
    </div>
    <div>
        <label>State:</label>
        <input type="text" th:field="*{state}" required/>
    </div>
    <div>
        <label>Country:</label>
        <input type="text" th:field="*{country}" required/>
    </div>
    <div>
        <label>Mobile Number:</label>
        <input type="text" th:field="*{mobileNumber}" required/>
    </div>
    <div>
        <label>Relative Name:</label>
        <input type="text" th:field="*{relativeName}"/>
    </div>
    <div>
        <label>Relative Mobile:</label>
        <input type="text" th:field="*{relativeMobile}"/>
    </div>
    <div>
        <label>Illness Details:</label>
        <textarea th:field="*{illnessDetails}"></textarea>
    </div>

    <!-- Password fields not in Patient entity -->
    <div>
        <label>Password:</label>
        <input type="password" name="password" required/>
    </div>
    <div>
        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword" required/>
    </div>





    <div>
        <button type="submit">Register</button>
    </div>
</form>

<p>
    Already registered? <a th:href="@{/login}">Login here</a>
</p>
</body>
</html>
