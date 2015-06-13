var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');

var accounts= function (req, res) {
console.log("Status");
console.log(req.param('phone'));
console.log(req.param('name'));
console.log(req.param('status'));

var insertString="update user_details set name='"+req.param('name')+"',status='"+req.param('status')+"' where phone='"+req.param('phone')+"'";
	mysqlimpli.execute(insertString,function(err,result) {
	if(err)
	{
	console.log(err);
	res.send({otp:null});
	}
	else
	console.log('registered');
	});
res.send("status updated");

};

exports.accounts=accounts;