<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />

  <div class="corners-mask corners"> <!-- for IE9 rounded corner fix -->
  <div class="box fl-panel corners" id="login">
  			<div class="header">
				<div class="logo"></div>
				<h2><spring:message code="chpassform.header" /></h2>
        	</div>

                <form:form method="POST" id="chpsfm" cssClass="fm-v clearfix" commandName="passwordchange" htmlEscape="true">
                    <form:errors path="*" id="msg" cssClass="errors" element="div" />
                    <div class="row field-pair fl-controls-left clearfix">
                        <span style="white-space:nowrap"> <label for="username" class="fl-label"><spring:message code="chpassform.label.username" /></label></span>
                        <form:input cssClass="required" cssErrorClass="error" id="username" size="25" tabindex="1" path="username" autocomplete="false" />
                    </div>
                    <div class="row field-pair fl-controls-left clearfix">
                        <span style="white-space:nowrap"> <label for="password" class="fl-label"><spring:message code="chpassform.label.password" /></label></span>
                        <form:password cssClass="required" cssErrorClass="error" id="password" size="25" tabindex="2" path="password" autocomplete="off" />
                    </div>
                    <div class="row field-pair fl-controls-left clearfix">
                        <span style="white-space:nowrap"> <label for="newPassword" class="fl-label"><spring:message code="chpassform.label.newpassword" /></label></span>
                        <form:password cssClass="required" cssErrorClass="error" id="newPassword" size="25" tabindex="3" path="newPassword" autocomplete="off" />
                    </div>
                    <div class="row field-pair fl-controls-left clearfix">
                        <span style="white-space:nowrap"> <label for="confirmPassword" class="fl-label"><spring:message code="chpassform.label.confirmpassword" /></label></span>
                        <form:password cssClass="required" cssErrorClass="error" id="confirmPassword" size="25" tabindex="4" path="confirmPassword" autocomplete="off" />
                    </div>
                    <div class="row btn-row clearfix">
                        <span class="button">
                        <input class="btn-submit" name="submit" value="<spring:message code="chpassform.button.submit" />" tabindex="5" type="submit" />
                        </span>
                        <input class="btn-reset" name="reset" value="<spring:message code="chpassform.button.clear" />" tabindex="6" type="reset" />
                    </div>
                </form:form>

          </div>
	</div>
<jsp:directive.include file="includes/bottom.jsp" />
