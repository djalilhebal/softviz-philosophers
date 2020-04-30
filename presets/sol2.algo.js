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
  state[i] = HUNGRY;
  test(i);
  v(mutex);
  p(s[i]);
  takeFork( i );
  takeFork( (i-1+N)%N );
}

function putBothForks(i) {
  p(mutex);
  putFork( i );
  putFork( (i-1+N)%N );
  state[i] = THINKING;
  test( (i+1)%N );
  test( (i-1+N)%N );
  v(mutex);
}

function test(k) {
  if (
    state[k] == HUNGRY &&
    state[ (k-1+N)%N ] != EATING &&
    state[ (k+1)%N ] != EATING
   ) {
      state[k] = EATING;
      v(s[k]);
  }
}
