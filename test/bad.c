#include "stdio.h"
#include "stdlib.h"

int main(int argc, char** argv) {
    printf("********* Inside bad.c *************\n");
    int x = 0;
    int y = 10;
    printf("Trying to divide by zero\n");
    y = y / x;
    printf("after divide by zero: %d\n", y);

    return 0;
}
