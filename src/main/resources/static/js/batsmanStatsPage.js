var recentFormChart;
var PAGE_SIZE;
var PLAYER_NAME;
var INNING = 0;
var RESULT_COUNT;
$( document ).ready(function() {
	
	
	if ($("body").hasClass("bat-stats-page"))
	{

		
		
		$( "#batsmanNameText" ).autocomplete({
		  	minLength : 2,
		  	source : searchPlayerNameurl,
		  	select: function( event, ui ) {
				  PAGE_SIZE = Number($('#nosOfInnText').val());
				  PLAYER_NAME = ui.item.value;
				  INNING = Number($('#inningDropdown').val());
				  initBatsmanStats();
			  }
		});
		
		$("#nosOfInnText , #inningDropdown" ).on( "change", function() {
		   	PLAYER_NAME = $( "#batsmanNameText" ).val();
		   	PAGE_SIZE = Number($('#nosOfInnText').val());
		   	INNING = Number($('#inningDropdown').val());
		   	initBatsmanStats();
		});
		
		
	}
	
});

function validation()
{
	if($( "#batsmanNameText" ).val() === '' | $( "#nosOfInnText" ).val() === '' | $('#inningDropdown').val() === '')
	{
		alert("all fields are mandatory");
		return false;
	}
	else
	{
		return true;	
	}
}

function initBatsmanStats()
{
	  let isValid = validation();
	  if(isValid === true)
	  {
		  getRecentForm(PLAYER_NAME);
		  getScoreRange(PLAYER_NAME);
		  getBatsmanCommonStats(PLAYER_NAME);
	  }
}

function getRecentForm(playerName)
{
	getBatsmanRecentForm(playerName).then(result => {
										      $("#batRecentFormUl" ).click();
										      $("#batRecentFormUl").empty();
										      $(".inningscount").text(PAGE_SIZE);
										      
										      RESULT_COUNT = result.length;
										      (result.length < 10) ? $('#limitedInnCount').text(result.length) : $('#limitedInnCount').text(10);
										      
										      
										      for(entry in result.slice(0, 10))
											  {
												  let record = result[Number(entry)];
												  let runs = '<span class="me-1 ">'+record.batsmanScore+'</span>';
												  let balls = '<span>('+record.ballsFaced+')</span>';
												  let strikeRate = '<br><span class="text-blue1"> SR : '+record.strikeRate.toFixed(2)+'</span>';
												  let content = runs + balls + strikeRate;
												  
												  let match = record.match.split('-');
												  match.pop();
												  let matchTitle = match.join(' ');
												  let matchDateArr = record.matchDate.split(' ');
												  let matchDate =  matchDateArr[2] + '-' + matchDateArr[1] + '-' + matchDateArr[5];
												  let popoverContent =  matchTitle + '  (' + matchDate + ')';
												  $('<li class="fs-5 text-center p-3 list-group-item flex-fill bg-dark text-white border border-2" data-bs-toggle="popover" tabindex="0"  data-bs-trigger="focus" data-bs-placement="bottom" data-bs-content="'+popoverContent+'">'+content+'</li> ').appendTo("#batRecentFormUl");
											  }
											  $('#recentFormRunsSection').removeClass('d-none');
											  recentformChart(result);  
											  
											  var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
											  var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
											        return new bootstrap.Popover(popoverTriggerEl)
											   });
									          
									  }).catch(error => {
											console.log('error while recent form ',error);
							          		reject();
							          });
}

function getBatsmanRecentForm(playerName)
{
	return new Promise(resolve => {
	
	let request = {
		playerName : playerName,
		pageNumber : 1,
		pageSize :  PAGE_SIZE,
		inning : INNING
	}
	
	$.ajax({
			       type: "POST",
			       contentType : 'application/json;',
			       dataType : 'json',
			       url: batsmanRecentFormUrl,
	      		   data: JSON.stringify(request),
			       success :function(result) {
			       		console.log('success ',result);
			       		resolve(result);
			       },
			       error :function(err) {
			       		console.log('error ',err);
			       }
			});
			
	});
}

function recentformChart(result)
{
	 if(!isNull(recentFormChart))
	 {
		  recentFormChart.destroy();
	 }
  	
	 let runsData = [];
	 for(entry in result)
	 {
		let record = result[Number(entry)];
		let matchDate = record.matchDate.split(' ');
		let month = new Date(matchDate[1]+'-1-01').getMonth()+1
		let formattedMonth = month < 10 ? '0'+month : month;
		let formattedDate = matchDate[5]+'-'+formattedMonth+'-'+matchDate[2];
		let strikeRate = (record.batsmanScore / record.ballsFaced) * 100;

		runsData.push({ label: formattedDate , runs: record.batsmanScore , sr : strikeRate.toFixed(2) });
		prevDate = formattedDate;
	 }
	runsData =  runsData.reverse();
	
	const plugin = {
		  id: 'customCanvasBackgroundColor',
		  beforeDraw: (chart, args, options) => {
		    const {ctx} = chart;
		    ctx.save();
		    ctx.globalCompositeOperation = 'destination-over';
		    ctx.fillStyle = options.color || '#000000';
		    ctx.fillRect(0, 0, chart.width, chart.height);
		    ctx.restore();
		  }
	};
	
	let chartConfig = {
      type: 'line',
      data: {
        labels: runsData.map(row => row.label),
        datasets: [
          {
            label: 'runs',
            data: runsData.map(row => row.runs),
            borderColor:'#ffffff',
            pointStyle:'triangle',
            pointBackgroundColor:'#22bdd0',
            pointRadius:10,
            backgroundColor:'#22bdd0',
          }
        ]
      },
      options: {
	    //responsive: true,
	    maintainAspectRatio: false,
	    scales: {
	      y: {
	        ticks: { color: '#ffffff', beginAtZero: true },
	        grid: { color: '#1e1f21' }
	      },
	      x: {
	        ticks: { color: '#ffffff', beginAtZero: true },
	        grid: { color: '#1e1f21' }
	      }
	    }
	  },
  	  plugins: [plugin],
    };
    
    
    
    
    let chartContext = document.getElementById('recentFormChart').getContext('2d');
    
	 
	recentFormChart = new Chart( chartContext,chartConfig);
	recentFormChart.update();
	$('#recentFormChartAccordian').removeClass('d-none');
	$('#recentFormChartSection').removeClass('d-none');
	//(RESULT_COUNT < 10) ? $('.inningscount').text(result.length) : $('.inningscount').text(10);
	$('.inningscount').text(result.length);
}

function getScoreRange(playerName)
{
	$('#scoreRangeDataTable').dataTable().fnDestroy();
	
	var scoreRangeDataTable = $('#scoreRangeDataTable').DataTable({
			  
			   ajax: {
			        'type' : 'POST',
			        'url' : batsmanScoreRangeUrl,
			        'contentType' : "application/json",
			        'dataType' : "json",
			        'data' : function () {
					    return JSON.stringify({
						  	"playerName": playerName,
						  	"pageNumber" : 1,
							"pageSize" :  PAGE_SIZE,
							"inning" : INNING
						  	
						});
					},
			    },   
				ordering: false,
				info:false,
				paging:false,
				searching:false,
				columnDefs: [
					 {
						"render": function ( data, type, row ) {
							
		                    return data.toFixed(2)+' %';
		                },
						targets: [2]
				     },
				     {
						"render": function ( data, type, row ) {
							
							let rangeArr = data.split('-');
							if(rangeArr.length > 1 && Number(rangeArr[0]) !== 0)
							{
								let ex = Number(rangeArr[1])-1;
								return rangeArr[0] +'-'+ex;
							}
							
		                    return data;
		                },
						targets: [0]
				     }
				],
				columns: [
					  { data: '_id' },
					  { data: 'count' },
					  { data: 'weight' }
				]
			});
			
			$('#scoreRangeDataTable').on('xhr.dt', function ( e, settings, json, xhr ) {
       						$('#scoreRangeSection').removeClass('d-none');
   			});
}

function getBatsmanCommonStats(playerName)
{
	
	
	let request = {
		playerName : playerName,
		pageNumber : 1,
		pageSize :  PAGE_SIZE,
		inning : INNING
	}
	
	$.ajax({
			       type: "POST",
			       contentType : 'application/json;',
			       dataType : 'json',
			       url: batsmanCommonStatseUrl,
	      		   data: JSON.stringify(request),
			       success :function(result) {
			       		console.log('success ',result);
			       		$('#totalInnings').text(result.totalInnings);
			       		$('#totalFours').text(result.totalFours);
			       		$('#totalSixes').text(result.totalSixes);
			       		$('#totalRuns').text(result.totalRuns);
			       		$('#avgRuns').text(result.avgRuns.toFixed(2));
			       		$('#avgStrikeRate').text(result.avgStrikeRate.toFixed(2));
			       		$('#commonStatsSection').removeClass('d-none');
			       },
			       error :function(err) {
			       		console.log('error ',err);
			       }
			});
			
	
}


function isNull(value)
{
	if(typeof value === "undefined" || value === null || value === ""  || value.length==0){
		return true;
	}else{
		return false;
	}
}