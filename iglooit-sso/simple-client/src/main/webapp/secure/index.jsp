<html>

<body>

<head>
<style type="text/css" >
body
{
    background: rgb(197, 204, 210);
}
</style>
</head>

<h1>Clarity SSO - Simple Client App</h1>

<h2>This is a protected page. You can get here if you've been remembered or authenticated.</h2>

<%if (request.isUserInRole("ROLE_CAS_ADMIN")) { %>
	You are an administrator! You can therefore see the <a href="extreme/index.jsp">extremely secure page</a>.<br><br>
<% } %>

<hr/>
<ul>
<li><a href="../">Home</a></li>
<li>Secure Page</li>
<li><a href="extreme/index.jsp">Extremely Secure Page</a></li>
<li><a href="/sso-simple-client/j_spring_security_logout">Logout</a></li>
</ul>

</body>

</html>
