"use strict";
var KTDatatablesExtensionsScroller = function() {

	var initTable2 = function() {
		var table = $('#kt_table_2');
		var table2 = $('#kt_table_3');

		// begin first table
		table.DataTable({
		    "language": {"url": "//cdn.datatables.net/plug-ins/1.10.22/i18n/Spanish.json"},
			responsive: true,
			"ajax": {
 			 "url": "/calculosOPGasolina",
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
				{data: 'medidaAnterior'},
				{data: 'combustibleRecibido'},
				{data: 'cantidadVendida'},
				{data: 'iventarioLibros'},
				{data: 'medidaTanque1'}, 
				{data: 'medidaTanque2', visible:false},
				{data: 'medidaTanque3', visible:false},
				{data: 'medidaFinalGalones'},
				{data: 'diferencia'},
				{data: 'totalVendido'},
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
						return ' <a href="/detalleOP/'+ data +'" class="btn btn-warning btn-sm btn-upper"> Editar </a> <a href="/eliminarOP/'+ data +'" class="btn btn-danger btn-sm btn-upper"> Eliminar </a>';		
					}
				},
				
			]
		});
		
		table2.DataTable({
		    "language": {"url": "//cdn.datatables.net/plug-ins/1.10.22/i18n/Spanish.json"},
			responsive: true,
			"ajax": {
 			 "url": "/calculosOPACPM",
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
				{data: 'medidaAnterior'},
				{data: 'combustibleRecibido'},
				{data: 'cantidadVendida'},
				{data: 'iventarioLibros'},
				{data: 'medidaTanque1', visible:false}, 
				{data: 'medidaTanque2'},
				{data: 'medidaTanque3'},
				{data: 'medidaFinalGalones'},
				{data: 'diferencia'},
				{data: 'totalVendido'},
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
						return ' <a href="/detalleOP/'+ data +'" class="btn btn-warning btn-sm btn-upper"> Editar </a> <a href="/eliminarOP/'+ data +'" class="btn btn-danger btn-sm btn-upper"> Eliminar </a>';		
					}
				},
				
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