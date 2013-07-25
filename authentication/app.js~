
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , fs=require('fs')
  , Rank = require('./models/Rank.js');

var app = module.exports = express.createServer();

// Configuration

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true }));
});

app.configure('production', function(){
  app.use(express.errorHandler());
});

// Routes
app.get('/ranklist1', function(req, res) {
  Rank.listScore(1, function(error, ranks){
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end(ranks, 'utf-8');
  });
});

app.post('/rankadd', function(req, res) {
  var username=req.body.username;
  var score=req.body.score;
  var mode=req.body.mode
  Rank.addScore(username, score, mode, function(err, user) {
    if(err) throw err;
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end('', 'utf-8');
  });
});

app.get('/ranklist2', function(req, res) {
  Rank.listScore(2, function(error, ranks){
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end(ranks, 'utf-8');
  });
});
app.get('/ranklist3', function(req, res) {
  Rank.listScore(3, function(error, ranks){
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end(ranks, 'utf-8');
  });
});

app.get('/ranklist', function(req, res) {
  var level=req.query.level;
  Rank.listScore(level, function(error, ranks){
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end(ranks, 'utf-8');
  });
});

app.get('/rankadd', function(req, res) {
  var username=req.query.username;
  var score=req.query.score;
  var mode=req.query.mode
  Rank.addScore(username, score, mode, function(err, user) {
    if(err) throw err;
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end('', 'utf-8');
  });
});

app.listen(80);
