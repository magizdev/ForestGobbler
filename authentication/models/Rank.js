var db=require('../lib/db.js');

var RankSchema=new db.Schema({
   username: {type:String}
  ,score: Number 
  ,mode: Number
})

var iRank=db.mongoose.model('rank', RankSchema);

module.exports.addScore = addScore;
module.exports.listScore = listScore;

function addScore(username, score, mode, callback) {
  if(mode!=1 && mode!=2 && mode!=3) {callback();}
  var instance=new iRank();
  instance.username = username;
  instance.score = score;
  instance.mode = mode;

  iRank.find({mode:mode}).count(function(err, count) {
    console.log("1");
    if(!err) {
      console.log("%d", count);
      if(count < 10) {
        instance.save(function (err) {});
        callback(null, instance);
      }else{
        iRank.find({mode:mode}).where('score').lt(score).count(function(err, count){
          if(!err && count>0){
            instance.save(function (err) {});
            iRank.findOneAndRemove({mode:mode},{sort:'score'},function(err){});
          }
          callback(null, instance);
        });
      }
    }else{
      console.log("2");
      callback(err);
    }
  });
}

function listScore(mode, callback) {
  if(mode!=1 && mode!=2 && mode!=3) {callback();}
  var totalRanks = "[";
  iRank.find({mode:mode}).exec(function(err, ranks) {
    for(var i=0;i<ranks.length;i++){
      totalRanks+="{";
      totalRanks+="username:" + ranks[i].username;
      totalRanks+=",";
      totalRanks+="score:" + ranks[i].score;
      totalRanks+="}";
      if(i< ranks.length-1) totalRanks+=",";
    }
    totalRanks+="]";
    callback(null, totalRanks);
  });
}
