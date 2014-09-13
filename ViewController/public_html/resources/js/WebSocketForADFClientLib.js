var wsUri = "ws://" + "127.0.0.1:7101/PushyOnSockets-ViewController-context-root"+ "/mediatorendpoint";
var websocket = new WebSocket(wsUri);

websocket.onerror = function(evt) { onError(evt) };

function onError(evt) {
    writeToScreen('ERROR:  ' + evt.data);
}

websocket.onopen = function(evt) { onOpen(evt) };

function writeToScreen(message) {
  var msgOutput =  AdfPage.PAGE.findComponentByAbsoluteId("messageMonitor");
  msgOutput.setValue(message);
}

function onOpen() {
    writeToScreen("Connected to " + wsUri);
}

websocket.onmessage = function(evt) { onMessage(evt) };

function onMessage(evt) {
    console.log("received: " + evt.data);
    writeToScreen(evt.data);
    handleMessage(evt.data);
}

function handleMessage(message) {
  var msgOutput =  AdfPage.PAGE.findComponentByAbsoluteId("messageMonitor");    
  if (message.indexOf('IMAGE=')>-1) {
   /* an image was selected */
    var image = message.substring(6);
    msgOutput.setValue('an image was selected: '+image)
    showPopup(image);
   }
}
function sendText(json) {
    websocket.send(json);
}
                
