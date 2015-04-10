'use strict';

var assert = require('assert')
  , should = require('should');

var exchangeData = {};

suite('exchange', function() {    
  test('sell should produce trades', function(done) {
  exchangeData = exchange.sell(40, 75, exchangeData);[1]
  exchangeData.trades[0].price.should.eql(40);[2]
  exchangeData.trades[0].volume.should.eql(75);[3]
  exchangeData.buys.volumes[40].should.eql(25);[4]
  exchangeData.sells.volumes[41].should.eql(200);[5]
  done();      
  });
  
  test('sell should add a SELL nockmarket order', function(done) {
  exchangeData = exchange.sell(41, 200, exchangeData);[1]
  exchangeData.sells.volumes['41'].should.eql(200);[2]
  done();
  });
  
  test('buy should add a BUY nockmarket order', function(done) {
  exchangeData = exchange.buy(40, 100, exchangeData);[1]
  exchangeData.buys.volumes[40].should.eql(100);[2]
  done();[3]
  });
  
}); 