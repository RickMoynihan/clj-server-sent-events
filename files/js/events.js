
var source = new EventSource('/event-source');
source.onmessage = function (event) {
  alert(event.data);
};