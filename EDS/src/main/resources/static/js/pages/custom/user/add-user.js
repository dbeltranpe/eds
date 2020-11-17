"use strict";

// Class definition
var KTUserAdd = function () {
	// Base elements
	var wizardEl;
	var formEl;
	var validator;
	var wizard;
	var avatar;

	// Private functions
	var initWizard = function () {
		// Initialize form wizard
		wizard = new KTWizard('kt_user_add_user', {
			startStep: 1, // initial active step number
			clickableSteps: true  // allow step clicking
		});

		// Validation before going to next page
		wizard.on('beforeNext', function(wizardObj) {
			if (validator.form() !== true) {
				wizardObj.stop();  // don't go to the next step
			}
		})

		// Change event
		wizard.on('change', function(wizard) {
			KTUtil.scrollTop();
		});
	}

	var initValidation = function() {
		validator = formEl.validate({
			// Validate only visible fields
			ignore: ":hidden",

			// Validation rules
			rules: 
			{
				// Step 1
				profile_avatar: {
					//required: true
				},
				profile_first_name: {
					required: true
				},
				profile_last_name: {
					required: true
				},
				profile_phone: {
					required: true
				},
				profile_email: {
					required: true,
					email: true
				}
			},

			// Display error
			invalidHandler: function(event, validator) {
				KTUtil.scrollTop();

				swal.fire({
					"title": "",
					"text": "There are some errors in your submission. Please correct them.",
					"type": "error",
					"buttonStyling": false,
					"confirmButtonClass": "btn btn-brand btn-sm btn-bold"
				});
			},

			// Submit valid form
			submitHandler: function (form) {

			}
		});
	}

	var initSubmit = function() {
		var btn = formEl.find('[data-ktwizard-type="action-submit"]');

		btn.on('click', function(e) {
			e.preventDefault();
			
			var mensaje = "";
			
			if(document.getElementById('nombres').value == "")
			{
				mensaje += "&#8226; El nombre no puede ser vacío<br>"
			}
			if(document.getElementById('apellidos').value== "")
			{
				mensaje += "&#8226; Los apellidos no pueden ser vacíos<br>"
			}
			if(document.getElementById('identificacion').value.length < 5)
			{
				mensaje += "&#8226; La identificación es debe tener como mínimo 5 dígitos<br>"
			}
			if(document.getElementById('direccion').value== "")
			{
				mensaje += "&#8226; La dirección no puede ser vacía<br>"
			}
			if(document.getElementById('telefono').value.length < 5)
			{
				mensaje += "&#8226; El número telefónico debe tener como mínimo 5 dígitos<br>"
			}
			
            var address = document.getElementById('correo').value;
            var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

            if (re.test(address) == false)
			{
				mensaje += "&#8226; El correo no es válido<br>"
			}
			if(document.getElementById('username').value.length < 4 && document.getElementById('username').value != "")
			{
				mensaje += "&#8226; Si se ingresa un nombre de usuario debe tener 4 caracteres como mínimo<br>"
			}
			if(document.getElementById('salariobase').value = 0)
			{
				mensaje += "&#8226; El salario no puede ser 0, ingresar el valor<br>"
			}
															

			if (mensaje == "") 
			{
				// See: src\js\framework\base\app.js
				KTApp.progress(btn);
				//KTApp.block(formEl);

				// See: http://malsup.com/jquery/form/#ajaxSubmit
				formEl.ajaxSubmit({
					success: function() {
						KTApp.unprogress(btn);
						//KTApp.unblock(formEl);

						swal.fire({
							"title": "",
							"text": "Se ha enviado el formulario",
							"type": "success",
							"confirmButtonClass": "btn btn-secondary"
						});
					}
				});
			}
			else
			{
				KTApp.unprogress(btn);
				swal.fire({
						"title": "",
						"html": mensaje,
						"type": "error",
						"buttonStyling": false,
						"confirmButtonClass": "btn btn-brand btn-sm btn-bold"
					});
			}
			
		});
	}

	var initUserForm = function() {
		avatar = new KTAvatar('kt_user_add_avatar');
	}

	return {
		// public functions
		init: function() {
			formEl = $('#kt_user_add_form');

			initWizard();
			initValidation();
			initSubmit();
			initUserForm();
		}
	};
}();

jQuery(document).ready(function() {
	KTUserAdd.init();
});
