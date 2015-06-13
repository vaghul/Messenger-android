var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');

var reg= function (req, res) {
console.log("this is reg");
console.log(req.param('regid'));
console.log(req.param('phone'));
console.log(req.param('name'));

var insertString="insert into user_details (name,phone,reg_id,status, prof_pic) values ('"+req.param('name')+"','"+req.param('phone')+"','"+req.param('regid')+"','I am using Pura !',null)";
	mysqlimpli.execute(insertString,function(err,result) {
	if(err)
	{
	console.log(err);
	res.send({otp:null});
	}
	else
	console.log('registered');
	});
res.send("done");

};

exports.reg=reg;