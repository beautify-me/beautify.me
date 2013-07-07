	
navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia || navigator.mozGetUserMedia || navigator.msGetUserMedia;

var constraints = {audio: false, video: true};

function successCallback(stream) {
	window.stream = stream; // stream available to console
	
	try {
		v.src = window.URL.createObjectURL(stream);
	} catch (e){
		v.src = stream;
	}  
	v.play();
}

function errorCallback(error){
  console.log("navigator.getUserMedia error: ", error);
}

navigator.getUserMedia(constraints, successCallback, errorCallback);