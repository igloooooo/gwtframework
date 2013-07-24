<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>

<html>

<head>
<style type="text/css" >
body
{
    background: rgb(197, 204, 210);
}
</style>
    <title>Login to CAS failed!</title>
</head>

<body>
<h2>Login to CAS failed!</h2>

<font color="red">
    Your CAS credentials were rejected.<br/><br/>
    Reason: <%= ((AuthenticationException) session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>
</font>
<p><a href="index.jsp">Home</a>
<p><a href="/sso-simple-client/j_spring_security_logout">Logout</a>

</body>
</html>
