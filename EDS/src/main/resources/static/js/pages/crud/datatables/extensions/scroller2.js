"use strict";
var KTDatatablesExtensionsScroller = function() {

	var initTable2 = function() {
		var table = $('#kt_table_2');

		// begin first table
		table.DataTable({
		    "language": {"url": "//cdn.datatables.net/plug-ins/1.10.22/i18n/Spanish.json"},
			responsive: true,
			"ajax": {
 			 "url": "/calculosOP",
  			 "dataSrc": ""
			},
			deferRender: true,
			scrollY: '500px',
			scrollCollapse: true,
			scroller: false,
			columns: 
			[
				{data: 'id', visible: false},
				{data: 'fecha', render: function ( data, ) { moment.locale('es'); return moment(data).locale('es').format("DD/MM/YYYY"); }},
				{data: 'combustibleRecibido'},
				{data: 'medidaTanque1'}, 
				{data: 'medidaTanque2'},
				{data: 'cantidadVendida'},
				{data: 'iventarioLibros', visible:false},
				{data: 'medidaFinalGalones'},
				{data: 'diferencia', visible:false},
				{data: 'totalVendido', visible:false},
				{data: 'medidaAnterior', visible:false},
				{data: 'id'}
			],
			columnDefs: 
			[
				{
					targets: -1, 
        			data: null,
					targets: -1,
					orderable: false,
					render: function(data, type, full, meta) 
					{
						return ' <a href="/eliminarOP/'+ data +'" class="btn btn-danger btn-sm btn-upper"> Eliminar </a>';		
					}
				}
			]
		});
	};


	return {

		//main function to initiate the module
		init: function() {
			initTable2();
		}
	};
}();

jQuery(document).ready(function() {
	KTDatatablesExtensionsScroller.init();
});
