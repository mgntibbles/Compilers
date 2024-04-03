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
* processing function: add
* processing n
12:    ST 0,-1(5)  *store return
*return
*looking for n
13:    LD 0,-2(5)  *load value
14:    ST 0,-2(5)  *store lhs
15:    LDC 0,1(0)  *load constant
16:    ST 0,-3(5)  *store rhs
17:    LD 0,-2(5)  *
18:    LD 1,-3(5)  *
19:    ADD 0,0,1  *adding
20:    LD 7,-1(5)  *
21:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,10(7)  *skip function
* processing function: main
23:    ST 0,-1(5)  *store return
* processing x
*assignment
*looking for x
24:    LDA 0,-2(5)  *load address
25:    ST 0,-4(5)  *
26:    LDC 0,1(0)  *load constant
27:    ST 0,-5(5)  *
28:    LD 0,-4(5)  *load rhs
29:    LD 1,-5(5)  *load lhs
30:    ST 0,0(1)  *
31:    ST 1,-2(5)  *storing value
*while
*looking for x
32:    LD 0,-2(5)  *load value
33:    ST 0,-4(5)  *store lhs
34:    LDC 0,6(0)  *load constant
35:    ST 0,-5(5)  *store rhs
36:    LD 0,-4(5)  *
37:    LD 1,-5(5)  *
38:    SUB 0,1,0  *op: <
39:    JGT 0,2(7)  *
40:    LDC 0,0(0)  *false case
41:    LDA 7,1(7)  *unconditional jump
42:    LDC 0,1(0)  *true case
*looking for x
44:    LD 0,-2(5)  *load value
45:    ST 0,-5(5)  *push
*function call: output
46:    ST 5,-3(5)  *store current fp
47:    LDA 5,-3(5)  *push new frame
48:    LDA 0,1(7)  *save return in ac
49:    LDA 7,-43(7)  *jump to function entry
50:    LD 5,0(5)  *pop current frame
*assignment
*looking for x
51:    LDA 0,-2(5)  *load address
52:    ST 0,-4(5)  *
*looking for x
53:    LD 0,-2(5)  *load value
54:    ST 0,-7(5)  *push
*function call: add
55:    ST 5,-5(5)  *store current fp
56:    LDA 5,-5(5)  *push new frame
57:    LDA 0,1(7)  *save return in ac
58:    LDA 7,-47(7)  *jump to function entry
59:    LD 5,0(5)  *pop current frame
60:    ST 0,-5(5)  *
61:    LD 0,-4(5)  *load rhs
62:    LD 1,-5(5)  *load lhs
63:    ST 0,0(1)  *
64:    ST 1,-2(5)  *storing value
65:    LDA 7,-34(7)  *move to test
43:    JEQ 0,22(7)  *exit loop
66:    LD 7,-1(5)  *return back to the caller
22:    LDA 7,44(7)  *skip function
* Finale23
67:    ST 5,-3(5)  *push ofp
68:    LDA 5,-3(5)  *push frame
69:    LDA 0,1(7)  *load ac with ret ptr
70:    LDA 7,-48(7)  *jump to main loc
71:    LD 5,0(5)  *pop frame
* End of Execution
72:    HALT 0,0,0  *
