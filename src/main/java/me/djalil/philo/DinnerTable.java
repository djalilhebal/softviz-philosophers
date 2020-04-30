package me.djalil.philo;

public class DinnerTable {
    int[] forks = {-1, -1, -1, -1, -1};

    public void setOwner(int forkIndex, int philoIndex) {
        forks[forkIndex] = philoIndex;
    }

    // Only unset the fork if philoIndex is actually holding it
    public void unsetOwner(int forkIndex, int philoIndex) {
        if (forks[forkIndex] == philoIndex) {
            forks[forkIndex] = -1;
        }
    }

    char get(int i) {
        int x = forks[i];
        if (x == -1) {
            return '*';
        } else {
            return (char) (x + '0');
        }
    }

}
