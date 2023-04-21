<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body class="bat-stats-page">


<div class="container-fluid">

<div class="container d-flex justify-content-center">
	<input  id="batsmanNameText" class="form-control form-control-lg my-2 w-50 text-center" type="text" placeholder="enter player name" aria-label=".form-control-lg example">
</div>


<tags:recentForm />


<div class="row">
		<div class="col-xl-6 col-12">
			<tags:recentFormChart />
		</div>
		<div class="col-xl-6 col-12">
			<tags:batsmanScoreRange />
		</div>
</div>











</div>
<tags:scripts />
</body>
</html>