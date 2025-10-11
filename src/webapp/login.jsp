<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Log in</title></head>
<body>
<h1>Log in</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
<form action="/registration/login" method="post">
    <label>Email: <input type="email" name="email"></label><br/>
    <label>Password: <input type="password" name="password"></label><br/>
    <button type="submit">Влез</button>
</form>
<a href="/registration/register">Sign up</a>
</body>
</html>