#include<iostream>
#include<random>
#include<iomanip>
#include<algorithm>
#include"../include/BoardGame_Classes.hpp"
using namespace std;

Pyramid_Player::Pyramid_Player (char symbol) : Player(symbol){}

Pyramid_Player::Pyramid_Player (int order, char symbol) : Player(order, symbol){}

void Pyramid_Player::get_move(int& x, int& y){
    cout << "\nEnter your move x and y from the available moves shown on the screen (separated by spaces): ";
    cin >> x >> y;
}
