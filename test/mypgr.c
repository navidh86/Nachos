/* halt.c
 *	Simple program to test whether running a user program works.
 *	
 *	Just do a "syscall" that shuts down the OS.
 *
 * 	NOTE: for some reason, user programs with global data structures 
 *	sometimes haven't worked in the Nachos environment.  So be careful
 *	out there!  One option is to allocate data structures as 
 * 	automatics within a procedure, but if you do this, you have to
 *	be careful to allocate a big enough stack to hold the automatics!
 */

#include "syscall.h"

void main()
{
    char b[10];
    char *execArgs[256];
    int status1, processID, processID1, processID2, status2, k;

    printf("\n\n********************************** mypgr Program Loading-test **********************************\n\n");

    printf("****** testing fork and join *****\n");
    
    printf("mypgr forking testArgs.coff and joining... \n");
    char* testArgs[3] = {"solaiman", "avijit", "navid"};
    processID = exec("testArgs.coff", 3,  testArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);

    printf("mypgr forking echo.coff and joining... \n");
    processID = exec("echo.coff", 0,  execArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);
    
    printf("mypgr forking halt.coff and joining... \n");
    processID = exec("halt.coff", 1,  execArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);
    
    //printf("mypr forking testArgs.coff, halt.coff and joining... \n");
    //processID1 = exec("testArgs.coff", 2,  testArgs);
    //processID2 = exec("halt.coff", 3,  execArgs);
    //int l = join(processID1, &status1);
    //int m = join(processID2, &status2);
    //printf("*********   Join On Process %d Finished\nStatus Value:  %d   ***************\n", processID1, status1);
    //printf("*********   Join On Process %d Finished\nStatus Value:  %d   ***************\n", processID2, status2);

    printf("\n------------CHECKING ABNORMAL EXITS--------------\n");
    printf("mypgr forking bad.coff and joining... \n");
    processID = exec("bad.coff", 0,  execArgs);
    k = join(processID, &status1);
    printf("********* Join On Process %d Finished\nStatus Value:  %d    ***************\n", processID, status1);

    printf("\n------------CHECKING INVALID JOIN CALLS--------------\n");
    int num = join(2, &num);
    printf("Return for join on pid 2 : %d\n", num);
    num = join(3, &num);
    printf("Return for join on pid 3 : %d\n", num);
    printf("\n------------END CHECKING INVALID JOIN CALLS--------------\n");
    
    halt();
    /* not reached */
    printf("This line cannot be reached\n");
}
