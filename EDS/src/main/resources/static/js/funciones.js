function agregar(){
	swal({
	  title: "Completado",
	  text: "El cliente ha sido agregado.",
	  icon: "success",
	  button: "Aceptar",
	})
	.then((OK) => {
		location.href="/dashBoardClientes"
	});
}