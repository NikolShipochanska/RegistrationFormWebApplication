<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Registration</title></head>
<body>
<h1>Sign up</h1>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red"><%= request.getAttribute("error") %></p>
<% } %>
<form action="/registration/register" method="post">
    <label>Username: <input type="text" name="name"></label><br/>
    <label>Email: <input type="email" name="email"></label><br/>
    <label>Password: <input type="password" name="password"></label><br/>
    <label><%= request.getAttribute("captchaQuestion") %></label>
    <input type="number" name="captchaAnswer" placeholder="Answer"><br/>
    <button type="submit">Sign up</button>
</form>
<a href="/registration/login">Log in</a>
</body>
</html>