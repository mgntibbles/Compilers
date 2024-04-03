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
* processing function: main
12:    ST 0,-1(5)  *store return
* processing x
*assignment
*looking for x
13:    LDA 0,-2(5)  *load address
14:    ST 0,-4(5)  *
15:    LDC 0,9(0)  *load constant
16:    ST 0,-5(5)  *
17:    LD 0,-4(5)  *load rhs
18:    LD 1,-5(5)  *load lhs
19:    ST 0,0(1)  *
20:    ST 1,-2(5)  *storing value
*if statement
*looking for x
21:    LD 0,-2(5)  *load value
22:    ST 0,-4(5)  *store lhs
23:    LDC 0,9(0)  *load constant
24:    ST 0,-5(5)  *store rhs
25:    LD 0,-4(5)  *
26:    LD 1,-5(5)  *
27:    SUB 0,1,0  *op: ==
28:    JEQ 0,2(7)  *
29:    LDC 0,0(0)  *false case
30:    LDA 7,1(7)  *unconditional jump
31:    LDC 0,1(0)  *true case
33:    HALT 0,0,0  *Error, forced ending
*function call: foo
34:    ST 5,-3(5)  *store current fp
35:    LDA 5,-3(5)  *push new frame
36:    LDA 0,1(7)  *save return in ac
37:    LDA 7,-38(7)  *jump to function entry
38:    LD 5,0(5)  *pop current frame
32:    JEQ 0,6(7)  *else part
*done if
39:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,28(7)  *skip function
* Finale12
40:    ST 5,-3(5)  *push ofp
41:    LDA 5,-3(5)  *push frame
42:    LDA 0,1(7)  *load ac with ret ptr
43:    LDA 7,-32(7)  *jump to main loc
44:    LD 5,0(5)  *pop frame
* End of Execution
45:    HALT 0,0,0  *
