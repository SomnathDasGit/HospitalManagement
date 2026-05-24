<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Paitent Form</title>
</head>
<body>

 <!-- <h2>Paitent Registration Form Submitted by <%= session.getAttribute("name") %></h2>
-->
 <h2>Hi ${paitent.getFirstName()} ${paitent.getLastName()} your unique id is <%= session.getAttribute("id") %></h2>

<h3> Use this unique id to <a href="/login" class="btn">Login</a> </h3>



</body>
</html>
