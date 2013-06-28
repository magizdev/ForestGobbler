var mongoose = require('mongoose');
var Schema=mongoose.Schema;

module.exports.mongoose=mongoose;
module.exports.Schema=Schema;

var username="magizdev_user";
var password="qwerasdf";
var address = '@ds031978.mongolab.com:31978/magizdev_test';
connect();

function connect() {
  var url='mongodb://localhost/test';  //+username+':'+password+address;
  console.log(url);
  mongoose.connect(url);
  console.log("conneted");
}
function disconnect() {mongoose.disconnect()}
