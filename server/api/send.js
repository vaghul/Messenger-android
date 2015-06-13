var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');
var http = require('http');
var async = require('async');  

var sendmessage= function (req, res) {

var reg,message,from,to,name,erroringcm,fromname;
message=req.param('message');
from=req.param('fromphone');
fromname=req.param('name');
to=req.param('tophone');
console.log(from);	
console.log(to);	
console.log(message);	
var queryString="select name,reg_id from user_details where phone='"+to+"'";
mysqlimpli.execute(queryString,function(err,result)
{
if(err)
console.log(err);
else
{
name=result[0].name;
reg=result[0].reg_id;
console.log(name);
//start of hit gcm

var data = {
  "collapseKey":"applice",
  "delayWhileIdle":true,
  "timeToLive":3,
  "data":{
    "message":message ,"fromphone":from,"tophone":to,"username":fromname
    },
  "registration_ids":[reg]
};
// cghae the reg id
var dataString =  JSON.stringify(data);
var headers = {
  'Authorization' : 'key=',   //Gcm key
  'Content-Type' : 'application/json',
  'Content-Length' : dataString.length
};

var options = {
  host: 'android.googleapis.com',
  port: 80,
  path: '/gcm/send',
  method: 'POST',
  headers: headers
};
async.waterfall([
    function(callback) {
        callback(null, 'one', 'two');
    },
    function(arg1, arg2, callback) {
      // arg1 now equals 'one' and arg2 now equals 'two'

	//Setup the request 
	var req = http.request(options, function(result) {
	result.setEncoding('utf-8');

  var responseString = '';

  result.on('data', function(data) {
    responseString += data;
  });

  result.on('end', function() {
    var resultObject = JSON.parse(responseString);
 //   console.log(responseString);
    console.log(resultObject);
 //console.log(resultObject.success); 
 callback(null, resultObject.success);
  });
  //console.log('STATUS: ' + res.statusCode);
//  console.log('HEADERS: ' + JSON.stringify(res.headers)); 
});
req.on('error', function(e) {
  console.log('error : ' + e.message + e.code);
	});

	req.write(dataString);
	req.end();

    },
	function(success, callback) {
		//console.log(success);
		//if(success==1)
		callback(null, success);
	}
], function (err, result) {
	console.log(result);
	console.log('end of async');
    // result now equals 'done'
	if(result==1)
	{
	var insertString="insert into conversation (`from`,`to`,`message`) values ('"+from+"','"+to+"','"+message+"')";
	mysqlimpli.execute(insertString,function(err,result) {
	if(err)
	{
	console.log(err);
	//res.send({otp:null});
	}
	else
	{
	console.log('convo inserted');
	//console.log(name);
	//console.log(reg);
	//console.log(phone);
	//console.log(message);
	}
	});
	res.send('done');
	}
	else
	res.send('error');
	
});
//console.log("success"+erroringcm)
//end of hit gcm	
}



});



};

exports.send=sendmessage;