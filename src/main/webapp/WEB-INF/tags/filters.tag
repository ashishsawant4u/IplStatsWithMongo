<%@ tag language="java" pageEncoding="UTF-8"%>

<div class="row mx-3 my-2">

		<form class="form-floating col-12 p-2 col-xl-6">
		  <input type="text" class="form-control fs-3" id="batsmanNameText" placeholder="enter player name">
		  <label for="batsmanNameText">Player Name</label>
		</form>
		
		<form class="form-floating col-12 p-2 col-xl-3">
		  <select class="form-select" id="inningDropdown" aria-label="Floating label select example">
		    <option value="1">1st</option>
		    <option value="2">2nd</option>
		    <option value="0" selected="selected">Both</option>
		  </select>
		  <label for="inningDropdown" class="">Innings</label>
		</form>
		
		<form class="form-floating col-12 p-2 col-xl-3">
		  <input type="text" class="form-control fs-3" id="nosOfInnText" placeholder="innings count" value="10" >
		  <label for="nosOfInnText" class="">Recent innings count</label>
		</form>
		
</div>
