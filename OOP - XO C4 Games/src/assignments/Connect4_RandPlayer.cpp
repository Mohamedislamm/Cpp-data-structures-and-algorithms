#include<iostream>
#include<random>
#include<iomanip>
#include<algorithm>
#include"../include/BoardGame_Classes.hpp"
using namespace std;

Connect4_RandPlayer::Connect4_RandPlayer(char symbol) : Player(symbol){
    this->name = "Random Computer Player";
    cout << "My names is " << name << endl;
}

void Connect4_RandPlayer::get_move(int& x, int& y){
    srand(time(0));
    x = 5;
    y = rand()%7;
}
