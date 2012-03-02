#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#pragma comment(lib, "ws2_32.lib")
#define port 6667

#include "mainreseau.h"
#include <QtGui/QApplication>
#include "mainwindow.h"

using namespace std;

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    MainReseau mr;

    mr.connecterServeur();
    //mr.communiquerServeur();
    //mr.deconnecterServeur();
    
    return a.exec();
}
