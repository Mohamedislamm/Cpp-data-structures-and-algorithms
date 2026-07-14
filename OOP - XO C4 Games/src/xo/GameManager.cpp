// Class definition for XO_GameManager class
// Author:  Mohammad El-Ramly
// Date:    10/10/2022
// Version: 1

#include <iostream>
#include <string>

class Player {
public:
    Player(const std::string& name, char symbol) : name(name), symbol(symbol) {}

    void get_move(int& x, int& y) {
        std::cout << name << " (" << symbol << ") enter row and column: ";
        std::cin >> x >> y;
    }

    char get_symbol() const { return symbol; }
    const std::string& get_name() const { return name; }

private:
    std::string name;
    char symbol;
};

class Board {
public:
    Board() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                cells[i][j] = ' ';
            }
        }
    }

    void display() const {
        std::cout << "\n";
        for (int i = 0; i < 3; ++i) {
            std::cout << "| ";
            for (int j = 0; j < 3; ++j) {
                std::cout << cells[i][j];
                if (j < 2) std::cout << " | ";
            }
            std::cout << " |\n";
        }
        std::cout << "\n";
    }

    bool update(int x, int y, char symbol) {
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || cells[x][y] != ' ') {
            return false;
        }
        cells[x][y] = symbol;
        ++moves;
        return true;
    }

    bool is_winner() const {
        for (int i = 0; i < 3; ++i) {
            if (cells[i][0] != ' ' && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2]) {
                return true;
            }
            if (cells[0][i] != ' ' && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i]) {
                return true;
            }
        }

        if (cells[0][0] != ' ' && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2]) {
            return true;
        }
        if (cells[0][2] != ' ' && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0]) {
            return true;
        }
        return false;
    }

    bool is_draw() const {
        return moves == 9 && !is_winner();
    }

    bool is_over() const {
        return is_winner() || is_draw();
    }

private:
    char cells[3][3];
    int moves = 0;
};

int main() {
    Board board;
    Player player1("Player 1", 'X');
    Player player2("Player 2", 'O');

    Player* current = &player1;
    Player* other = &player2;

    while (!board.is_over()) {
        board.display();
        int x = -1;
        int y = -1;

        current->get_move(x, y);
        while (!board.update(x, y, current->get_symbol())) {
            std::cout << "Invalid move. Try again.\n";
            current->get_move(x, y);
        }

        if (board.is_winner()) {
            board.display();
            std::cout << current->get_name() << " wins!\n";
            return 0;
        }

        if (board.is_draw()) {
            board.display();
            std::cout << "Draw!\n";
            return 0;
        }

        Player* temp = current;
        current = other;
        other = temp;
    }

    return 0;
}
