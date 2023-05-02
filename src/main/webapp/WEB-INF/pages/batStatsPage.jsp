<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>IPL Batsman Stats</title>
</head>
<body class="bat-stats-page">


<div class="container-fluid">



<tags:header />

<tags:filters />

<tags:recentForm />



<div class="row mt-3">
		<div class="col-xl-8 col-12">
			<tags:batsmanScoreRange />
		</div>
		<div class="col-xl-4 col-12">
			<tags:commonStats />
		</div>
</div>

<tags:recentFormChart />








</div>
<tags:scripts />
</body>
</html>