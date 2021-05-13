"use strict";

// Class definition
var KTDashboard = function() {

    // Based on Chartjs plugin - http://www.chartjs.org/
    var salesStats = function() {
    
    	var chartjsData = [];
    	var labels = [];
		var myJSON = $.getJSON( "pagosNominalesMensuales", function(data) {
	  		
		    $.each( data, function( key, val ) {
		        labels.push(val[0] + ' ' + val[1]);
		        chartjsData.push(val[2]);
		      });
		})
	
        if (!KTUtil.getByID('kt_chart_sales_stats')) {
            return;
        }

        var config = {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: "Vr. Neto Pagado",
                    borderColor: KTApp.getStateColor('brand'),
                    borderWidth: 2,
                    //pointBackgroundColor: KTApp.getStateColor('brand'),
                    backgroundColor: KTApp.getStateColor('brand'),                    
                    pointBackgroundColor: Chart.helpers.color('#ffffff').alpha(0).rgbString(),
                    pointBorderColor: Chart.helpers.color('#ffffff').alpha(0).rgbString(),
                    pointHoverBackgroundColor: KTApp.getStateColor('danger'),
                    pointHoverBorderColor: Chart.helpers.color(KTApp.getStateColor('danger')).alpha(0.2).rgbString(),
                    data: chartjsData
                }]
            },
            options: {
                title: {
                    display: false,
                },
                tooltips: {
                    intersect: false,
                    mode: 'nearest',
                    xPadding: 10,
                    yPadding: 10,
                    caretPadding: 10
                },
                legend: {
                    display: false,
                    labels: {
                        usePointStyle: false
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
                hover: {
                    mode: 'index'
                },
                scales: {
                    xAxes: [{
                        display: false,
                        gridLines: false,
                        scaleLabel: {
                            display: true,
                            labelString: 'Month'
                        }
                    }],
                    yAxes: [{
                        display: false,
                        gridLines: false,
                        scaleLabel: {
                            display: true,
                            labelString: 'Value'
                        }
                    }]
                },

                elements: {
                    point: {
                        radius: 3,
                        borderWidth: 0,

                        hoverRadius: 8,
                        hoverBorderWidth: 2
                    }
                }
            }
        };

        var chart = new Chart(KTUtil.getByID('kt_chart_sales_stats'), config);
        }


    return {
        init: function() 
        {
            salesStats();
        }
    };
}();

// Class initialization on page load
jQuery(document).ready(function() 
{
    KTDashboard.init();  
    $("#kt_chart_sales_stats").css("height","162px");
});


