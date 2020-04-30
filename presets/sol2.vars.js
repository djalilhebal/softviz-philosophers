// "state enum" sorta
THINKING = 0;
HUNGRY   = 1;
EATING   = 2;

// All philosophers start THINKING
state = [ THINKING, THINKING, THINKING, THINKING, THINKING ];

mutex = Sem(1);

s = [ Sem(0), Sem(0), Sem(0), Sem(0), Sem(0) ];
