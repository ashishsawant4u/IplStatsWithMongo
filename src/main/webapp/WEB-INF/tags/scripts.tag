<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>   

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<link href="https://fonts.googleapis.com/css2?family=EB+Garamond&display=swap" rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script src="${contextPath}/webjars/bootstrap/5.0.0/js/bootstrap.min.js"></script>
<script src="${contextPath}/webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="${contextPath}/webjars/jquery-ui/1.12.1/jquery-ui.min.js"></script>

<script src="${contextPath}/webjars/datatables/1.13.2/js/dataTables.bootstrap5.min.js"></script>
<script src="${contextPath}/webjars/datatables/1.13.2/js/dataTables.dataTables.min.js"></script>
<script src="${contextPath}/webjars/datatables/1.13.2/js/jquery.dataTables.min.js"></script>

<link href="${contextPath}/webjars/bootstrap/5.0.0/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextPath}/webjars/jquery-ui/1.12.1/jquery-ui.min.css" rel="stylesheet">

<link href="${contextPath}/webjars/datatables/1.13.2/css/dataTables.bootstrap5.min.css" rel="stylesheet">
<link href="${contextPath}/webjars/datatables/1.13.2/css/jquery.dataTables.css" rel="stylesheet">




<link href="${contextPath}/css/style.css" rel="stylesheet">
<script src="${contextPath}/js/batsmanStatsPage.js"></script>

<tags:javascriptVariables />