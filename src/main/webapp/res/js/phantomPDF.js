var webPage = require('webpage');
var system = require('system');
var page = webPage.create();
var url = system.args[1];
var outPath = system.args[2];

function waitFor(testFx, onReady, timeOutMillis) {
    var maxtimeOutMillis = timeOutMillis ? timeOutMillis : 30000, //< Default Max Timout is 30s
        start = new Date().getTime(),
        condition = false,
        interval = setInterval(function() {
            if ( (new Date().getTime() - start < maxtimeOutMillis) && !condition ) {
                // If not time-out yet and condition not yet fulfilled
                condition = (typeof(testFx) === "string" ? eval(testFx) : testFx()); //< defensive code
            } else {
                if(!condition) {
                     phantom.exit(1);              
                } else {
                    typeof(onReady) === "string" ? eval(onReady) : onReady(); //< Do what it's supposed to do once the condition is fulfilled
                    clearInterval(interval); //< Stop this interval
                }
            }
        }, 1000); //< repeat check every 1000ms
};

page.open(url, function (status) {
	if (status !== 'success') {
        console.log('fail to load Url');
        phantom.exit();
    } else {
    	var param;
	    waitFor(function() {
	        // Check in the page if a specific element is now visible
	    	param = page.evaluate(function() {
	    		var printStatus = document.body.getAttribute("printStatus");
	        	if(printStatus=="true"){
	        		var arr = new Array();
	        		arr.push(true);
	        		arr.push(document.body.scrollWidth);
	        		arr.push(document.body.scrollHeight);
	        		return arr;
	        	}else{
	        		var arr = new Array();
	        		arr.push(false);
	        		return arr;
	        	}
	        });
	    	return param[0];
	    }, function() {
	    	//console.log("width:" + param[1]);
	    	//console.log("height:" + param[2]);
	    	page.viewportSize = {
	    			width :300,
	    			height : 300
	    	};
	    	if (system.args.length > 3) {
	    	    size = system.args[3].split('*');
	    	    page.paperSize = size.length === 2 ? { width: size[0], height: size[1], margin: '0px' }
	    	                                       : { format: system.args[3], orientation: 'portrait', margin: '0cm' };
	    	}else{
	    		page.paperSize = {
				    	width : param[1],
				    	height :param[2]*0.75
		    	};
	    	}
	    	page.render(outPath);
	    	page.close();
	    	console.log('handle success');
    		phantom.exit();
	    });
    }
});


