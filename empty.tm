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
* processing function: main
12:    ST 0,-1(5)  *store return
* processing x
*assignment
*looking for x
13:    LDA 0,-2(5)  *load address
14:    ST 0,-4(5)  *
15:    LDC 0,1(0)  *load constant
16:    ST 0,-5(5)  *
17:    LD 0,-4(5)  *load rhs
18:    LD 1,-5(5)  *load lhs
19:    ST 0,0(1)  *
20:    ST 1,-2(5)  *storing value
*while
*looking for x
21:    LD 0,-2(5)  *load value
22:    ST 0,-4(5)  *store lhs
23:    LDC 0,3(0)  *load constant
24:    ST 0,-5(5)  *store rhs
25:    LD 0,-4(5)  *
26:    LD 1,-5(5)  *
27:    SUB 0,1,0  *op: <
28:    JGT 0,2(7)  *
29:    LDC 0,0(0)  *false case
30:    LDA 7,1(7)  *unconditional jump
31:    LDC 0,1(0)  *true case
*looking for x
33:    LD 0,-2(5)  *load value
34:    ST 0,-5(5)  *push
*function call: output
35:    ST 5,-3(5)  *store current fp
36:    LDA 5,-3(5)  *push new frame
37:    LDA 0,1(7)  *save return in ac
38:    LDA 7,-32(7)  *jump to function entry
39:    LD 5,0(5)  *pop current frame
*assignment
*looking for x
40:    LDA 0,-2(5)  *load address
41:    ST 0,-4(5)  *
*looking for x
42:    LD 0,-2(5)  *load value
43:    ST 0,-6(5)  *store lhs
44:    LDC 0,1(0)  *load constant
45:    ST 0,-7(5)  *store rhs
46:    LD 0,-6(5)  *
47:    LD 1,-7(5)  *
48:    ADD 0,0,1  *adding
49:    ST 0,-5(5)  *
50:    LD 0,-4(5)  *load rhs
51:    LD 1,-5(5)  *load lhs
52:    ST 0,0(1)  *
53:    ST 1,-2(5)  *storing value
54:    LDA 7,-34(7)  *move to test
32:    JEQ 0,22(7)  *exit loop
55:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,44(7)  *jump to finale
* Finale
56:    ST 5,-5(5)  *push ofp
57:    LDA 5,-5(5)  *push frame
58:    LDA 0,1(7)  *load ac with ret ptr
59:    LDA 7,-48(7)  *jump to main loc
60:    LD 5,0(5)  *pop frame
* End of Execution
61:    HALT 0,0,0  *
