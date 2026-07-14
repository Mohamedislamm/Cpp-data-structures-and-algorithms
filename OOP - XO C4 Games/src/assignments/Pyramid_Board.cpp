#include <iostream>
#include <random>
#include <iomanip>
#include <algorithm>
#include "../include/BoardGame_Classes.hpp"

Pyramid_Board::Pyramid_Board(){
    n_rows = 3;
    n_cols = 1;
    board = new char*[n_rows];
    for (int i = 0; i < n_rows; i++) {
        board[i] = new char[n_cols];
        for (int j = 0; j < n_cols; j++){
            board[i][j] = 0;
        }
        n_cols += 2;
    }
}



bool Pyramid_Board::update_board(int x, int y, char symbol){
    if((board[x][y]==0) && ((x==0 && y==0)
    || (x==1 && !( y < 0 || y > 2))
    || (x==2 && !( y < 0 || y > 4)))){
        board[x][y] = toupper(symbol);
        n_moves++;
        return true;
    }
    return false;
}


void Pyramid_Board::display_board(){
    n_cols = 1;
    for (int i = 0; i < n_rows; i++) {
        string spaces(n_rows-i-1, '\t');
        cout << "\n";

        cout << spaces << "| ";
        for (int j = 0; j < n_cols; j++) {
            cout << "(" << i << "," << j << ")";
            cout << setw(2) << board [i][j] << "| ";
        }
        cout << "\n-----------------------------------------";
        n_cols += 2;
   }
   cout << endl;
}



/*

void PyrBoard::display_board(){
    for (int i = 0; i < n_rows; i++) {
        n_cols = 1;
        cout << "\n";
        for(int k = n_rows-i-1; k > 0; k--){
            cout << '\t';
        }
        cout << " | ";
        n_cols += i*2;
        for (int j = 0; j < n_cols; j++) {
            cout << "(" << i << "," << j << ")";
            cout << setw(2) << board [i][j] << " | ";
        }
        cout << "\n-----------------------------";
   }
   cout << endl;
}
*/




bool Pyramid_Board::is_winner(){
    //Checking rows
    n_cols = 1;
    int Xc, Oc;
    for (int i = 0; i < n_rows; i++) {
        Xc = Oc = 0;
        for (int j = 0; j < n_cols; j++){
            if(Xc < 3 && Oc <3){
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
        if (Xc == 3 || Oc == 3) return true;
        n_cols += 2;
   }

   //Checking the column and diagonals
   if(n_moves > 4)
      if((board[0][0] == board[1][1] && board[1][1] == board[2][2])
         ||(board[0][0] == board[1][0] && board[1][0] == board[2][0])
         || (board[0][0] == board[1][2] && board[1][2] == board[2][4])){
            return true;
      }
   return false;
}

bool Pyramid_Board::is_draw(){
    return (n_moves == 9 && !is_winner());
}

bool Pyramid_Board::game_is_over(){
    return n_moves >= 9;
}

