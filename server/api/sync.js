var fs=require('fs'),
path = require('path');
var mysqlimpli=require('Mysqlimpli');

var val=[];
var syncContact= function (req, res) {
val=[];
//console.log("otp generation");
console.log('syncing');
//console.log(req.param('regid'));
//console.log(req.param('phone'));
//console.log(req.param('name'));
var json=JSON.parse(req.param('contact'));
//fs.writeFile(__dirname+"/"+"contact.json",JSON.stringify(json,null,4));
//console.log(req.param('name'));

var queryString="select name,phone,status from user_details";
mysqlimpli.execute(queryString,function(err,result)
{
if(err)
console.log(err);
else
{
for(i=0;i<json.length;i++)
for(j=0;j<result.length;j++)
{
if(json[i].phone==result[j].phone)
{
var obj={};
obj['name']=json[i].name;
obj['phone']=json[i].phone;
obj['status']=result[j].status;

if(val.length==0)
val.push(obj);
else
{
if(!avail(obj.phone))
val.push(obj);
}}
}
}

console.log("contact using pura");
console.log(val);

res.send(val);

});

};

function avail(number)
{
for(key in val)
{
if(val[key].phone==number)
return true;
}
return false;
};
exports.sync=syncContact;