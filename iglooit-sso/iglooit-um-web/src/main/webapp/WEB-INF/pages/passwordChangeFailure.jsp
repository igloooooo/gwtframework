<%@ page contentType="text/html; charset=UTF-8" %>

<script>
function goBack()
  {
  window.history.back()
  }
</script>

<jsp:directive.include file="includes/top.jsp" />
    <div id="msg" class="errors">
        <fmt:setBundle basename="com.clarity.commons.iface.um.UserPasswordUpdateStatus" var="typeBundle"/>
        <fmt:message key="${passwordchange.result}" bundle="${typeBundle}" var="reason"/>
        <h2><spring:message code="failure.header" /></h2>
        <p/><spring:message code="failure.message" arguments="${passwordchange.username};${reason}" htmlEscape="false" argumentSeparator=";"/>
        <p/><spring:message code="failure.warning" />
        <p/><spring:message code="failure.footer" />
    </div>
<jsp:directive.include file="includes/bottom.jsp" />
