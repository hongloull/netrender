function newTab(address){
	window.open(address);
} 


function findParam(param){
     var query = window.location.search.substring(1);     // Get query string  
     var pairs = query.split("&");                 // Break at ampersand  
     for(var i = 0; i < pairs.length; i++) {  
         var pos = pairs[i].indexOf('=');          // Look for "name=value"  
         if (pos == -1) continue;                  // If not found, skip  
         var argname = pairs[i].substring(0,pos);  // Extract the name  
         var value = pairs[i].substring(pos+1);    // Extract the value  
         value = decodeURIComponent(value);        // Decode it, if needed  
         if (argname==param)                        // Choose "Id"    
     		return value;
     }  
     return "0"; 
}
