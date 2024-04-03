* Standard prelude:
0:    LD 6,0(0)  *load gp with maxaddress
1:    LDA 5,0(6)  *copy gp to fp
2:    ST 0,0(0)  *clear location 0
* Jump around i/o routine:
* Code for input routine:
4:    ST 0,-1(5)  *store return
5:    IN 0,0,0  *clear location 0
6:    LD 7,-1(5)  *return to caller
* Code for output routine:
7:    ST 0,-1(5)  *store return
8:    LD 0,-2(5)  *load output value
9:    OUT 0,0,0  *
10:    LD 7,-1(5)  *return to caller
3:    LDA 7,7(7)  *
* End Standard prelude.
* processing function: foo
* processing y
12:    ST 0,-1(5)  *store return
*if statement
*looking for y
13:    LD 0,-2(5)  *load value
*return
15:    LDC 0,2(0)  *load constant
16:    LD 7,-1(5)  *
14:    JEQ 0,2(7)  *else part
*return
*looking for y
17:    LD 0,-2(5)  *load value
18:    LD 7,-1(5)  *
*done if
19:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,8(7)  *skip function
20:    HALT 0,0,0  *Error, forced to stop
* Finale-1
21:    ST 5,-2(5)  *push ofp
22:    LDA 5,-2(5)  *push frame
23:    LDA 0,1(7)  *load ac with ret ptr
24:    LDA 7,-26(7)  *jump to main loc
25:    LD 5,0(5)  *pop frame
* End of Execution
26:    HALT 0,0,0  *
