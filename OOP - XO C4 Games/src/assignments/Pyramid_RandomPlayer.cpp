#include<iostream>
#include<random>
#include<iomanip>
#include<algorithm>
#include"../include/BoardGame_Classes.hpp"
using namespace std;

Pyramid_RandomPlayer::Pyramid_RandomPlayer(char symbol) : Player(symbol){
    this->name = "Random Computer Player";
    cout << "My names is " << name << endl;
}

void Pyramid_RandomPlayer::get_move(int& x, int& y){
    srand(time(0));
    x = rand()%3;
    if(x == 0)
        y = 0;
    else if(x == 1){
        y = rand()%3;
    }
    else{
        y = rand()%5;
    }
}
