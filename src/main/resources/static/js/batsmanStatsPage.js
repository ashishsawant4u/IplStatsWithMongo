var recentFormChart;

$( document ).ready(function() {
	
	
	if ($("body").hasClass("bat-stats-page"))
	{
		
		$( "#batsmanNameText" ).autocomplete({
		  	minLength : 2,
		  	source : searchPlayerNameurl,
		  	select: function( event, ui ) {
				  console.log(ui.item.value);
				  getBatsmanRecentForm(ui.item.value).then(result => {
										           
										      $("#batRecentFormUl").empty();
										      for(entry in result.slice(0, 9))
											  {
												  let record = result[Number(entry)];
												  let runs = '<span class="me-1">'+record.batsmanScore+'</span>';
												  let balls = '('+record.ballsFaced+')';
												  let strikeRate = '<br> SR : '+record.strikeRate.toFixed(2);
												  let content = runs + balls + strikeRate;
												  $('<li class="fs-4 text-center p-3 list-group-item flex-fill">'+content+'</li> ').appendTo("#batRecentFormUl");
											  	  
											  }
											  
											  recentformChart(result);  
									          
									  }).catch(error => {
											console.log('error while recent form ',error);
							          		reject();
							          });
				  
				  
				  getScoreRange(ui.item.value);
			  }
		});
	}
	
});

function getBatsmanRecentForm(playerName)
{
	return new Promise(resolve => {
	
	let request = {
		playerName : playerName,
		pageNumber : 1,
		pageSize :  20
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

		runsData.push({ label: formattedDate , y: record.batsmanScore });
	
		prevDate = formattedDate;
	 }
	runsData =  runsData.reverse();
	 
	recentFormChart = new Chart(
    document.getElementById('myChart'),
    {
      type: 'line',
      data: {
        labels: runsData.map(row => row.label),
        datasets: [
          {
			borderColor: "red",  
            label: 'runs',
            data: runsData.map(row => row.y)
          }
        ]
      }
    }
  );
	
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
						  	"playerName": playerName
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
				  }
				],
				columns: [
					  { data: '_id' },
					  { data: 'count' },
					  { data: 'weight' }
				]
			});
			
			$('#scoreRangeDataTable').on('xhr.dt', function ( e, settings, json, xhr ) {
       						$('#scoreRangeDataTable').removeClass('d-none');
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