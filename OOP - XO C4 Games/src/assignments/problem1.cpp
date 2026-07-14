#include <iostream>
#include <string>
#include <fstream>
using namespace std;

//Specifications
class LabelGenerator {
protected:
    string prefix;
    int initSequence, seq;
public:
    LabelGenerator(string p, int s);
    virtual string nextLabel();
};

//Implementation
LabelGenerator::LabelGenerator(string p, int s) : prefix(p), initSequence(s), seq(s) {}

string LabelGenerator::nextLabel() {
    string label = prefix + to_string(seq);
    seq++;
    return label;
}
//====================================================

//Specifications
class FileLabelGenerator : public LabelGenerator {
private:
    string fileName;
public:
    FileLabelGenerator(string p, int s, string fn);
    string nextLabel() override;
};

//Implementation
FileLabelGenerator::FileLabelGenerator(string p, int s, string fn) : LabelGenerator(p, s), fileName(fn) {}

string FileLabelGenerator::nextLabel() {
    string caption;
    ifstream myFile(fileName);
    int lineNum = seq-initSequence;
    while (lineNum >= 0) {
        if(!myFile.good()){
            caption = "";
            break;
        }
        getline(myFile, caption);
        lineNum--;
    }
    string newLabel = LabelGenerator::nextLabel();
    newLabel.push_back(' ');
    newLabel += caption;
    return newLabel;
}
//====================================================

int main() {
    LabelGenerator figureNumbers("Figure ", 1);
    LabelGenerator pointNumbers("P", 0);
    cout << "Figure numbers: ";
    for (int i = 0; i < 3; i++) {
        cout << figureNumbers.nextLabel() << ", ";
    }
    cout << endl << "Point numbers: ";
    for (int i = 0; i < 5; i++) {
        cout << pointNumbers.nextLabel() << ", ";
    }
    cout << endl << "More figures: ";
    for (int i = 0; i < 3; i++) {
        cout << figureNumbers.nextLabel() << ", ";
    }
    cout << "\n=================================================\n";
    FileLabelGenerator figureLabels ("Figure ", 1, "labels.txt");
    cout << "Figure labels: \n";
    for (int i = 0; i < 2; i++) {
        cout << figureLabels.nextLabel() << endl;
    }
    return 0;
}
#include <iostream>
#include <string>
#include <fstream>
using namespace std;

//Specifications
class LabelGenerator {
protected:
    string prefix;
    int initSequence, seq;
public:
    LabelGenerator(string p, int s);
    virtual string nextLabel();
};

//Implementation
LabelGenerator::LabelGenerator(string p, int s) : prefix(p), initSequence(s), seq(s) {}

string LabelGenerator::nextLabel() {
    string label = prefix + to_string(seq);
    seq++;
    return label;
}
//====================================================

//Specifications
class FileLabelGenerator : public LabelGenerator {
private:
    string fileName;
public:
    FileLabelGenerator(string p, int s, string fn);
    string nextLabel() override;
};

//Implementation
FileLabelGenerator::FileLabelGenerator(string p, int s, string fn) : LabelGenerator(p, s), fileName(fn) {}

string FileLabelGenerator::nextLabel() {
    string caption;
    ifstream myFile(fileName);
    int lineNum = seq-initSequence;
    while (lineNum >= 0) {
        if(!myFile.good()){
            caption = "";
            break;
        }
        getline(myFile, caption);
        lineNum--;
    }
    string newLabel = LabelGenerator::nextLabel();
    newLabel.push_back(' ');
    newLabel += caption;
    return newLabel;
}
//====================================================

int main() {
    LabelGenerator figureNumbers("Figure ", 1);
    LabelGenerator pointNumbers("P", 0);
    cout << "Figure numbers: ";
    for (int i = 0; i < 3; i++) {
        cout << figureNumbers.nextLabel() << ", ";
    }
    cout << endl << "Point numbers: ";
    for (int i = 0; i < 5; i++) {
        cout << pointNumbers.nextLabel() << ", ";
    }
    cout << endl << "More figures: ";
    for (int i = 0; i < 3; i++) {
        cout << figureNumbers.nextLabel() << ", ";
    }
    cout << "\n=================================================\n";
    FileLabelGenerator figureLabels ("Figure ", 1, "labels.txt");
    cout << "Figure labels: \n";
    for (int i = 0; i < 2; i++) {
        cout << figureLabels.nextLabel() << endl;
    }
    return 0;
}