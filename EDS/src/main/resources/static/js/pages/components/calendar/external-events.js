"use strict";

var KTCalendarExternalEvents = function() {

    var initExternalEvents = function() {
        $('#kt_calendar_external_events .fc-draggable-handle').each(function() {
            // store data so the calendar knows to render an event upon drop
            $(this).data('event', {
                title: $.trim($(this).text()), // use the element's text as the event title
                stick: true, // maintain when user navigates (see docs on the renderEvent method)
                classNames: [$(this).data('color')],
                description: 'Lorem ipsum dolor eius mod tempor labore'
            });
        });
    }

    var initCalendar = function() {
        var todayDate = moment().startOf('day');
        var YM = todayDate.format('YYYY-MM');
        var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
        var TODAY = todayDate.format('YYYY-MM-DD');
        var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');

        var calendarEl = document.getElementById('kt_calendar');
        var containerEl = document.getElementById('kt_calendar_external_events');
        var Draggable = FullCalendarInteraction.Draggable;
        
        var href = window.location.href;
        const segments = new URL(href).pathname.split('/');
		const idTrabajador = segments.pop() || segments.pop(); // Handle potential trailing slash

        new Draggable(containerEl, {
            itemSelector: '.fc-draggable-handle',
            eventData: function(eventEl) {
                return $(eventEl).data('event');
            }   
        });

        var calendar = new FullCalendar.Calendar(calendarEl, {
            plugins: [ 'interaction', 'dayGrid', 'timeGrid', 'list' ],
            isRTL: KTUtil.isRTL(),
            header: {
                left: 'prev,next,today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
             buttonText: 
             {
    			today: 'Hoy',
  			 },
  			 
            height: 800,
            contentHeight: 780,
            aspectRatio: 3,  // see: https://fullcalendar.io/docs/aspectRatio

            nowIndicator: true,
            allDaySlot: false,
           // now: TODAY, 
           // just for demo

            views: {
                timeGridWeek: { buttonText: 'Semana' },
                timeGridDay: { buttonText: 'Día' },
                dayGridMonth: { buttonText: 'Mes' },
            },
 			locale: 'es',
            defaultView: 'timeGridWeek',
            defaultDate: TODAY,

            droppable: false, // this allows things to be dropped onto the calendar
            editable: false,
            eventLimit: false, // allow "more" link when too many events
            navLinks: true,
            events: "/registrosTurnos/" + idTrabajador,
			
            drop: function(arg) {
                // is the "remove after drop" checkbox checked?
                if ($('#kt_calendar_external_events_remove').is(':checked')) {
                    // if so, remove the element from the "Draggable Events" list
                    $(arg.draggedEl).remove();
                }
            },

            eventRender: function(info) {
                var element = $(info.el);

                if (info.event.extendedProps && info.event.extendedProps.description) {
                    if (element.hasClass('fc-day-grid-event')) {
                        element.data('content', info.event.extendedProps.description);
                        element.data('placement', 'top');
                        KTApp.initPopover(element);
                    } else if (element.hasClass('fc-time-grid-event')) {
                        element.find('.fc-title').append('<div class="fc-description">' + info.event.extendedProps.description + '</div>'); 
                    } else if (element.find('.fc-list-item-title').lenght !== 0) {
                        element.find('.fc-list-item-title').append('<div class="fc-description">' + info.event.extendedProps.description + '</div>'); 
                    }
                } 
            }
        });

        calendar.render();        
    }

    return {
        //main function to initiate the module
        init: function() {
            initExternalEvents();
            initCalendar(); 
        }
    };
}();

jQuery(document).ready(function() {
    KTCalendarExternalEvents.init();
});