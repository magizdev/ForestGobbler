
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

app.get('/', routes.index);
app.get('/rank', function(req, res) {
  fs.readFile('./form.html', function(error, content) {
    if(error) {
      res.writeHead(500);
      res.end();
    }
    else {
      res.writeHead(200, { 'Content-Type': 'text/html' });
      res.end(content, 'utf-8');
    }
  });
});

app.get('/rankList', function(req, res) {
  Rank.listScore(1, function(error, ranks){
    res.writeHead(200, {'Content-Type':'text/html'});
    res.end(ranks, 'utf-8');
  });
});

app.post('/rank', function(req, res) {
  var username=req.body.username;
  var score=req.body.score;
  var mode=req.body.mode
  Rank.addScore(username, score, mode, function(err, user) {
    if(err) throw err;
    res.redirect('/rank');
  });
});

app.listen(3000);
console.log("Express server listening on port %d in %s mode", app.address().port, app.settings.env);
