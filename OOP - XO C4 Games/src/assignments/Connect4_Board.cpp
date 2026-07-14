#include <iostream>
#include <random>
#include <iomanip>
#include <algorithm>
#include "../include/BoardGame_Classes.hpp"
using namespace std;

// Set the board
Connect4_Board::Connect4_Board () {
    n_rows = 6;
    n_cols = 7;
    board = new char*[n_rows];
    for (int i = 0; i < n_rows; i++) {
        board [i] = new char[n_cols];
        for (int j = 0; j < n_cols; j++)
            board[i][j] = 0;
    }
}

bool Connect4_Board::update_board(int x, int y, char symbol){
    if(!(y < 0 || y > 6)){
        for(int i = x; i >= 0; i--){
            if(board[i][y] == 0){
                board[i][y] = toupper(symbol);
                n_moves++;
                return true;
            }
        }
    }
    return false;
}

void Connect4_Board::display_board(){
    for (int i = 0; i < n_cols; i++){
        cout << "     " << i ;
    }
    for (int i = 0; i < n_rows; i++) {
        cout << "\n";
        cout << "  |  ";
        for (int j = 0; j < n_cols; j++) {
            (board[i][j] == 0)? cout <<  " ":  cout << board[i][j];
            cout << "  |  ";
        }
        cout << "\n-----------------------------------------------";
   }
   cout << endl;
}

bool Connect4_Board::is_winner(){
    int Xc, Oc;
    //Checking rows
    for (int i = 0; i < n_rows; i++) {
        Xc = Oc = 0;
        for (int j = 0; j < n_cols; j++){
            if(Xc < 4 && Oc < 4){
                if (board[i][j] == 'X'){
                    Oc = 0;
                    Xc++;
                }
                else if (board[i][j] == 'O') {
                    Xc = 0;
                    Oc++;
                }
                else{
                    Xc = Oc = 0;
                }
            }
        }
        if (Xc == 4 || Oc == 4) return true;
   }
   //Checking columns
    for (int i = 0; i < n_cols; i++) {
        Xc = Oc = 0;
        for (int j = 0; j < n_rows; j++){
            if(Xc < 4 && Oc < 4){
                if (board[j][i] == 'X'){
                    Oc = 0;
                    Xc++;
                }
                else if (board[j][i] == 'O') {
                    Xc = 0;
                    Oc++;
                }
                else{
                    Xc = Oc = 0;
                }
            }
        }
        if (Xc == 4 || Oc == 4) return true;
   }
   // Check from bottom-left to top-right diagonals
    for (int i = 0; i < n_rows - 3; i++) {
        for (int j = 0; j < n_cols - 3; j++) {
            if (board[i][j] != 0 &&
                board[i][j] == board[i + 1][j + 1] &&
                board[i][j] == board[i + 2][j + 2] &&
                board[i][j] == board[i + 3][j + 3]) {
                return true;
            }
        }
    }

    // Check from bottom-right to top-left diagonals
    for (int i = 0; i < n_rows - 3; i++) {
        for (int j = 3; j < n_cols; j++) {
            if (board[i][j] != 0 &&
                board[i][j] == board[i + 1][j - 1] &&
                board[i][j] == board[i + 2][j - 2] &&
                board[i][j] == board[i + 3][j - 3]) {
                return true;
            }
        }
    }

   return false;
}

bool Connect4_Board::is_draw(){
    return (n_moves == 42 && !is_winner());
}

bool Connect4_Board::game_is_over(){
    return n_moves >= 42;
}

