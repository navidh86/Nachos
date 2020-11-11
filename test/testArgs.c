/** testArgs.c
	This program will print the number of arguments passed and
	also the arguments on different lines.
*/

void main(int argc, char **argv) {
	printf("inside testArgs\n");
	printf("%d arguments passed to testArgs.c\n", argc);
	
	int i;
	for (i = 0; i < argc; i++) {
		char *s = argv[i];
		printf("%d -> %s\n", i, s);
	}
	printf("testArgs ended\n");
	return;
}
