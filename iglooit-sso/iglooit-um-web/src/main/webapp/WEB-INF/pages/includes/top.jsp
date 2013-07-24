<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <title>Clarity - User Management</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="cas/cas.css" />" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	    <link rel="icon" href="<c:url value="/images/favicon.ico" />" type="image/x-icon" />
		<!--[if IE]><link type="text/css" rel="stylesheet" href="css/ie_cas.css" /><![endif]-->
		<!--[if lt IE 8]><style type="text/css">#cas #login .btn-submit{border: 0 none;}</style><![endif]--> 
	</head>
	<body id="cas" class="fl-theme-iphone">
	<table height="100%" width="100%" id="content">
	<tbody>
	<tr>
	<td align="center" valign="middle">
