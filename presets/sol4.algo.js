function philosopher(i) {
  while (true) {
    think();
    takeBothForks(i);
    eat();
    putBothForks(i);
  }
}

function takeBothForks(i) {
  p(s);
  p(fourch[i]);
  takeFork(i);
  p(fourch[ (i-1+N)%N ]);
  takeFork( (i-1+N)%N );
  v(s);
}

function putBothForks(i) {
  v(fourch[ i ]);
  putFork( i );
  v(fourch[ (i-1+N)%N ]);
  putFork( (i-1+N)%N );
}
