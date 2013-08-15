var db=require('../lib/db.js');

var RankSchema=new db.Schema({
   username: {type:String}
  ,score: Number 
  ,mode: Number
  ,imei: {type:String}
  ,game: {type:String}
})

var iRank=db.mongoose.model('rank', RankSchema);

module.exports.addScore = addScore;
module.exports.listScore = listScore;

function addScore(username, score, mode, imei, game, callback) {
  if(mode!=1 && mode!=2 && mode!=3) {callback();}
  if(game!="forestgobbler") {callback();}
  var instance=new iRank();
  instance.username = username;
  instance.score = score;
  instance.mode = mode;
  instance.imei = imei;
  instance.game = game;

  iRank.findOne({mode:mode, game:game, imei:imei}, function(err, doc) {
    if(!err) {
        if(doc){
        if(doc.score < score){
            doc.score = score;
            doc.save(callback);
        }
        callback(null, doc);
        }else{
        instance.save(function(err,doc){
        if(!err){
            iRank.count({mode:mode, game:game}, function(err, count){
            if(count > 100) {
                iRank.findOneAndRemove({mode:mode, game:game},{sort:'score'},function(err){});
            }else{
                callback(null, instance);
            }});

        }else{
            callback(err);
        }});
        }
    }else{
      callback(err);
    }
  });
}

function listScore(mode, game, callback) {
  if(mode!=1 && mode!=2 && mode!=3) {callback();}
  var totalRanks = "{ranks:[";
  iRank.find({mode:mode, game:game}).sort("-score").exec(function(err, ranks) {
    for(var i=0;i<ranks.length;i++){
      totalRanks+="{";
      totalRanks+="username:" + ranks[i].username;
      totalRanks+=",";
      totalRanks+="imei:" + ranks[i].imei;
      totalRanks+=",";
      totalRanks+="score:" + ranks[i].score;
      totalRanks+="}";
      if(i< ranks.length-1) totalRanks+=",";
    }
    totalRanks+="]}";
    console.log(totalRanks);
    callback(null, totalRanks);
  });
}
