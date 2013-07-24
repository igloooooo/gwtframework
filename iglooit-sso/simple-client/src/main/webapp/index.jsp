<%@ page import="org.springframework.security.cas.authentication.CasAuthenticationToken" %>

<html>

<head>
<style type="text/css" >
body
{
    background: rgb(197, 204, 210);
}
</style>
</head>

<body>

<h1>Clarity SSO - Simple Client App</h1>

<h2>This is home page. Anyone can see this page.</h2>

<% if (request.getUserPrincipal() != null) { %>
<p>Your username is: <%= ((CasAuthenticationToken) request.getUserPrincipal()).getUserDetails().getUsername() %></p>
<p>Authorities: <%= ((CasAuthenticationToken) request.getUserPrincipal()).getAuthorities() %></p>
<p>Credentials (Ticket): <%= ((CasAuthenticationToken) request.getUserPrincipal()).getCredentials() %></p>
<% } else { %>
<p>You have not logged in yet.</p>
<% } %>

<hr/>
<ul>
<li>Home</li>
<li><a href="secure/index.jsp">Secure Page</a></li>
<li><a href="secure/extreme/index.jsp">Extremely Secure Page</a></li>
<li><a href="/sso-simple-client/j_spring_security_logout">Logout</a></li>
</ul>

</body>

</html>
