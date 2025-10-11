<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.example.registration.model.User" %>
<html>
<head><title>Профил</title></head>
<body>
<h1>Change profile</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
<% User user = (User) request.getAttribute("user"); %>
<form action="/registration/profile" method="post">
    <label>New username: <input type="text" name="name" value="<%= user.getName() %>"></label><br/>
    <label>New password: <input type="password" name="password"></label><br/>
    <button type="submit">Save</button>
</form>
<a href="/registration/logout">Log out</a>
</body>
</html>