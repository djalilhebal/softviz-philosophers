// injected: _i, _activity

var Semaphore = Java.type("java.util.concurrent.Semaphore");
var Thread = Java.type("java.lang.Thread");
var SimControl = Java.type("me.djalil.philo.SimControl");

function askMaster(what, param) {
  if (param == undefined) {
    param = -1;
  }
  SimControl.act(_i, what, param);
}

function sleepSecs(n) {
  Thread.sleep(n * 1000);
}

function Sem(n) {
  return new Semaphore(n, true);
}

function p(x) {
  x.acquire();
}

function v(x) {
  x.release();
}

function getRandomInt(min, max) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function takeFork(j) {
  askMaster("setOwner", j);
}

function putFork(j) {
  askMaster("unsetOwner", j);
}

function think() {
  _activity = "THINKING";
  sleepSecs(getRandomInt(5, 10));
  //_activity = "WORKING";
}

function eat() {
  _activity = "EATING";
  sleepSecs(getRandomInt(5, 10));
  askMaster("fillHealth");
  //_activity = "WORKING";
}

// Translate to French
var manger = eat;
var penser = think;
var prendreFourchette = takeFork;
var poserFourchette = putFork;
// (typeof philosopher == 'function' ? philosopher : philosophe)(i);
