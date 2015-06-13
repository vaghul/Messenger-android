var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');

var val=[];
var generate= function (req, res) {
	var phone= req.param('phone');
	

	var otp=Math.floor(Math.random()*9000) + 1000;
	console.log(otp);
	var queryString="select otp from otp where otp='"+otp+"'";
	mysqlimpli.execute(queryString,function(err,result) {
	if(err)
	{
	console.log(err);
	res.send({otp:null});
	}
	else
	{
	if(result.length==0){
	var insertString="insert into otp  (phone,otp,valid) values ('"+phone+"','"+otp+"',30)";
	mysqlimpli.execute(insertString,function(err,result) {
	if(err)
	console.log(err);
	});
	var accountSid = 'accountSid';  // The tokens should be replaced here
	var authToken = 'token'; 
 
	//require the Twilio module and create a REST client 
	//var client = require('twilio')(accountSid, authToken); 
	/*
	client.messages.create({  
		to:'+919600092296', // change mobile number here
		from: "+13082254930",  
		body: "Enna da nadakudhu inga"
	}, function(err, message) { 
		console.log(message.sid); 
	});
	*/
	/* Timer function */
	var tt=setInterval(function(){startTime(phone,otp)},60*1000);
	
	function startTime(phone,otp){
	var queryString="select valid from otp where phone='"+phone+"'";

	mysqlimpli.execute(queryString,function(err,result) {
	if(err)
	console.log(err);
	else
	{
	var valid=parseInt(result[0].valid)-1;
	//console.log('New valid'+ valid);
	var validString="update otp set valid='"+valid+"' where phone='"+phone+"'";

	mysqlimpli.execute(validString,function(err,result) {
	if(err)
	console.log(err);
	});
	if(valid==0)
	{
	
	clearInterval(tt);
	var deleteString="delete from otp where otp='"+otp+"' and phone='"+phone+"'";

	mysqlimpli.execute(deleteString,function(err,result) {
	if(err)
	console.log('error'+err);
	});
	
	}
	}
	});
	}
		/* Timer function */
	}
	//pass ph no ,valid
	
	res.send({otp:otp});
	}
	});
	
};


var checkOtp= function (req, res) {
	//console.log("Hit");
	var otp= req.param('otp');
	var phone= req.param('phone');
	//console.log(otp+'   '+phone);
	var checkString="select otp from otp where otp='"+otp+"' and phone='"+phone+"'";
	mysqlimpli.execute(checkString,function(err,result) {
	if(err)
	console.log(err);
	else
	{
	//console.log(result);
	if(result.length==0)
	{
	console.log("Otp wrong");
	res.send("false");
	}
	else
	{
	console.log("Otp verified");
	//var deleteString="delete from otp where otp='"+otp+"' and phone='"+phone+"'";

	/*
	mysqlimpli.execute(deleteString,function(err,result) {
	if(err)
	console.log(err);
	});
	*/
	res.send("true");
	}
	}
	});
	
};


var test= function (req, res) {
	console.log('name');
	console.log(req.param('name'));
	res.send("Received successfully");
};
exports.generate=generate;
exports.checkOtp=checkOtp;
exports.test=test;