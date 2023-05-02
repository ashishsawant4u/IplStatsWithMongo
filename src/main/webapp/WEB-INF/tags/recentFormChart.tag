<%@ tag language="java" pageEncoding="UTF-8"%>

 
 
<!-- <div class="accordion accordion-flush mt-3 d-none" id="recentFormChartAccordian">
  <div class="accordion-item">
    <h2 class="accordion-header" id="flush-headingOne">
      <button class="accordion-button collapsed bg-dark text-white text-center" type="button" data-bs-toggle="collapse" data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
       <span class="fs-5">Recent Form Graph</span>
       <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#22bdd0" class="bi bi-caret-down-fill" viewBox="0 0 16 16">
		  <path d="M7.247 11.14 2.451 5.658C1.885 5.013 2.345 4 3.204 4h9.592a1 1 0 0 1 .753 1.659l-4.796 5.48a1 1 0 0 1-1.506 0z"/>
		</svg>
       
      </button>
    </h2>
    <div id="flush-collapseOne" class="accordion-collapse collapse" aria-labelledby="flush-headingOne" data-bs-parent="#accordionFlushExample">
      <div class="accordion-body">
      		<canvas id="recentFormChart" height="100" class="mt-3"></canvas>
      </div>
    </div>
  </div>
</div> -->


<div class="p-2 d-none mt-2" id="recentFormChartSection">
<span class="fs-4">Performance Graph
	<span class="fs-5 fst-italic text-muted">(last <span class="inningscount"></span> innings)</span>
</span>
	
	
<div class="chart-container">	
	<canvas id="recentFormChart"></canvas>
</div>

</div>