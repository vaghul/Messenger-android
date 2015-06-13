var body = require('body-parser')
var express = require('express');
var app = express();
var fs =require('fs'),
path = require('path');
routescan = require('express-routescan');
var mysqlimpli=require('Mysqlimpli');

app.use(body.json({limit: '50mb'}));
app.use(body.urlencoded({limit: '50mb', extended: true}));
app.use(body.urlencoded({ extended: false }))
app.use(body.json());
app.set('view engine', 'ejs');
app.use(express.static(path.join(__dirname, 'public')));

app.set('ip', process.env.OPENSHIFT_NODEJS_IP || "192.168.43.177");

//console.log(__dirname);
  routescan(app, {
      directory: [
          './routes'
      ]
  });

var server = app.listen(3000, function () {

  var host = app.get('ip');
  var port = server.address().port

  console.log('RESTFul Web Service loaded. Server Listening At http://%s:%s', host, port)
  });
