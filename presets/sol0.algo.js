function philosopher(i) {
  while (true) {
    think();
    takeBothForks(i);
    eat();
    putBothForks(i);
  }
}

function takeBothForks(i) {
  p(mutex);
  takeFork( i );
  takeFork( (i-1+N)%N );
}

function putBothForks(i) {
  putFork( i );
  putFork( (i-1+N)%N );
  v(mutex);
}
