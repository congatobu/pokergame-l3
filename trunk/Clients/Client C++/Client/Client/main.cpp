#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>
#include <errno.h>
#pragma comment(lib, "ws2_32.lib")
#define port 6667

#include "MainReseau.h"

using namespace std;

int main()
{
	
MainReseau mr;

mr.connecterServeur();
mr.communiquerServeur();
mr.deconnecterServeur();

return 0;
}