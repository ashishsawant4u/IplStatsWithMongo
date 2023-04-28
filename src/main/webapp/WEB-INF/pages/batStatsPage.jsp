<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>IPL Batsman Stats</title>
</head>
<body class="bat-stats-page">


<div class="container-fluid">

<!-- <div class="container d-flex justify-content-center my-2">
	<input  id="batsmanNameText" class="form-control form-control-lg" type="text" placeholder="enter player name" aria-label=".form-control-lg example">
	<input  id="nosOfInnText" class="form-control form-control-lg" type="text" value="10" placeholder="innings count" aria-label=".form-control-lg example">
</div> -->


<div class="row my-2">
	<div class="col-xl-6">
		<form class="form-floating col-8 float-end">
		  <input type="text" class="form-control fs-3" id="batsmanNameText" placeholder="enter player name">
		  <label for="batsmanNameText">Player Name</label>
		</form>
	</div>
	<div class="col-xl-6 row">
		<form class="form-floating col-4">
		  <select class="form-select" id="inningDropdown" aria-label="Floating label select example">
		    <option value="1">1st</option>
		    <option value="2">2nd</option>
		    <option value="0" selected="selected">Both</option>
		  </select>
		  <label for="inningDropdown" class="ps-3">Innings</label>
		</form>
		
		<form class="form-floating col-4">
		  <input type="text" class="form-control fs-3" id="nosOfInnText" placeholder="innings count" value="10" >
		  <label for="nosOfInnText" class="ps-3">Recent innings count</label>
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