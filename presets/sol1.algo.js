function philosophe(i) {
  while (true) {
    penser();
    prendreLesFourchettes(i);
    manger();
    poserLesFourchettes(i);
  }
}

function prendreLesFourchettes(i) {
  p(fourch[i]);
  prendreFourchette(i);
  p(fourch[ (i-1+N)%N ]);
  prendreFourchette( (i-1+N)%N );
}

function poserLesFourchettes(i) {
  v(fourch[ i ]);
  poserFourchette( i );
  v(fourch[ (i-1+N)%N ]);
  poserFourchette( (i-1+N)%N );
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
