"use strict";
var KTDatatablesExtensionsScroller = function() {

	var initTable1 = function() {
		var table = $('#kt_table_1');

		// begin first table
		table.DataTable({
		    "language": {"url": "//cdn.datatables.net/plug-ins/1.10.22/i18n/Spanish.json"},
			responsive: true,
			"ajax": {
 			 "url": "/registrosNominas",
  			 "dataSrc": ""
			},
			deferRender: true,
			scrollY: '500px',
			scrollCollapse: true,
			scroller: false,
			columns: 
			[
				{data: 'id', visible: false},
				{data: 'trabajador.nombres'},
				{data: 'trabajador.apellidos'},
				{data: 'fecha', render: function ( data, ) { moment.locale('es'); return moment(data).locale('es').format("MM/YYYY"); }}, 
				{data: 'vrTotalDevengado',  render: function ( data, ) { return '$ ' + (data).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'); }},
				{data: 'vrTotalDesc' ,  render: function ( data ) { return '$ ' + (data).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'); }},
				{data: 'vrNetoPagar',  render: function ( data ) { return '$ ' + (data).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,'); }},
				{data: 'id'}
			],
			columnDefs: 
			[
				{
					targets: -1, 
        			data: null,
					targets: -1,
					title: 'Detalle',
					orderable: false,
					render: function(data, type, full, meta) 
					{
						return ' <a href="/nominaTrabajadorId/'+ data +'" class="btn btn-sm btn-clean btn-icon btn-icon-md" title="View"> <i class="la la-edit"></i> </a>';
					}
				}
			]
		});
	};


	return {

		//main function to initiate the module
		init: function() {
			initTable1();
		}
	};
}();

jQuery(document).ready(function() {
	KTDatatablesExtensionsScroller.init();
});
