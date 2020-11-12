#include "stdio.h"
#include "stdlib.h"

int main(int argc, char** argv)
{
    int i, j, num;
   
    char buf[30];
   
    printf("Hello world\n");
    char *testArgv[3] = {"Mohammad", "solaimanope", "Solaiman"};
    int processID = exec("testArgs.coff", 3, testArgv);
    int status;
    int k = join(processID, status);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status);
   

    printf("\n------------CHECKING INVALID JOIN CALLS--------------\n");
    num = join(2, &num);
    printf("Return for join on pid 2 : %d\n", num);
    num = join(3, &num);
    printf("Return for join on pid 3 : %d\n", num);
    printf("\n------------END CHECKING INVALID JOIN CALLS--------------\n");

    halt();
   
    printf("Halt is not working!!\n");
   
    return 0;
}
