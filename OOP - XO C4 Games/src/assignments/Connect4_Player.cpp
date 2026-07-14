#include<iostream>
#include<random>
#include<iomanip>
#include<algorithm>
#include"../include/BoardGame_Classes.hpp"
using namespace std;

Connect4_Player::Connect4_Player (char symbol) : Player(symbol){}

Connect4_Player::Connect4_Player (int order, char symbol) : Player(order, symbol){}

void Connect4_Player::get_move(int& x, int& y){
    cout << "\nEnter the column in which you want to play your move: ";
    x = 5;
    cin >> y;
}
