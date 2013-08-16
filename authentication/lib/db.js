var mongoose = require('mongoose');
var Schema=mongoose.Schema;

module.exports.mongoose=mongoose;
module.exports.Schema=Schema;

var username="magizdev_user";
var password="qwerasdf";
var address = '@ds041198.mongolab.com:41198/game_rank';
connect();

function connect() {
  var url='mongodb://'+username+':'+password+address;
  mongoose.connect(url);
}
function disconnect() {mongoose.disconnect()}
