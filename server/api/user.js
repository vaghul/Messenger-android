var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');

var createuser= function (req, res) {
console.log("create user");
var queryString="Insert into user_details(name,phone,reg_id,status,prof_pic) values('vaghul',9597333280,'72822','my bird is sleepy! will send her later',null)";
mysqlimpli.execute(queryString,function(err,result)
{
if(err)
{
res.send('error');
console.log(err);
}
else
res.send('inserted');
});
};

exports.createuser=createuser;