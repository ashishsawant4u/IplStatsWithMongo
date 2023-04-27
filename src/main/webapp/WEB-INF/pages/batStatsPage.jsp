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

<!-- <div class="container d-flex justify-content-center my-2">
	<input  id="batsmanNameText" class="form-control form-control-lg" type="text" placeholder="enter player name" aria-label=".form-control-lg example">
	<input  id="nosOfInnText" class="form-control form-control-lg" type="text" value="10" placeholder="innings count" aria-label=".form-control-lg example">
</div> -->


<div class="row my-2">
	<div class="col-xl-8">
		<form class="form-floating col-8 float-end">
		  <input type="text" class="form-control fs-3" id="batsmanNameText" placeholder="enter player name">
		  <label for="batsmanNameText">Player Name</label>
		</form>
	</div>
	<div class="col-xl-4">
		<form class="form-floating col-2">
		  <input type="text" class="form-control fs-3" id="nosOfInnText" placeholder="innings count" value="10" >
		  <label for="nosOfInnText">Innings</label>
		</form>
	</div>
</div>


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