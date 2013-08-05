var mongoose = require('mongoose');
var Schema=mongoose.Schema;

module.exports.mongoose=mongoose;
module.exports.Schema=Schema;

var username="magizdev_user";
var password="qwerasdf";
var address = '@ds031978.mongolab.com:31978/magizdev_test';
connect();

function connect() {
  var url='mongodb://localhost'//+username+':'+password+address;
  mongoose.connect(url);
}
function disconnect() {mongoose.disconnect()}
