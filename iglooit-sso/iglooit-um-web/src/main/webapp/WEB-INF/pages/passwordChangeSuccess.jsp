<%@ page contentType="text/html; charset=UTF-8" %>

<script>
function closeWindow()
  {
  window.close()
  }
</script>

<jsp:directive.include file="includes/top.jsp" />
    <div id="msg" class="success">
        <h2><spring:message code="success.header" /></h2>
        <p/><spring:message code="success.message" arguments="${passwordchange.username}" htmlEscape="false"/>
    </div>
<jsp:directive.include file="includes/bottom.jsp" />
