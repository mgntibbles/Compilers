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
* processing fac
*assignment
*looking for x
13:    LDA 0,-2(5)  *load address
14:    ST 0,-4(5)  *
*function call: input
15:    ST 5,-5(5)  *store current fp
16:    LDA 5,-5(5)  *push new frame
17:    LDA 0,1(7)  *save return in ac
18:    LDA 7,-15(7)  *jump to function entry
19:    LD 5,0(5)  *pop current frame
20:    ST 0,-5(5)  *
21:    LD 0,-4(5)  *load rhs
22:    LD 1,-5(5)  *load lhs
23:    ST 0,0(1)  *
24:    ST 1,-2(5)  *storing value
*assignment
*looking for fac
25:    LDA 0,-2(6)  *load address
26:    ST 0,-4(5)  *
27:    LDC 0,1(0)  *load constant
28:    ST 0,-5(5)  *
29:    LD 0,-4(5)  *load rhs
30:    LD 1,-5(5)  *load lhs
31:    ST 0,0(1)  *
32:    ST 1,-2(6)  *storing value
*while
*looking for x
33:    LD 0,-2(5)  *load value
34:    ST 0,-4(5)  *store lhs
35:    LDC 0,1(0)  *load constant
36:    ST 0,-5(5)  *store rhs
37:    LD 0,-4(5)  *
38:    LD 1,-5(5)  *
39:    SUB 0,1,0  *op: >
40:    JLT 0,2(7)  *
41:    LDC 0,0(0)  *false case
42:    LDA 7,1(7)  *unconditional jump
43:    LDC 0,1(0)  *true case
*assignment
*looking for fac
45:    LDA 0,-2(6)  *load address
46:    ST 0,-4(5)  *
*looking for fac
47:    LD 0,-2(6)  *load value
48:    ST 0,-6(5)  *store lhs
*looking for x
49:    LD 0,-2(5)  *load value
50:    ST 0,-7(5)  *store rhs
51:    LD 0,-6(5)  *
52:    LD 1,-7(5)  *
53:    MUL 0,0,1  *multiplying
54:    ST 0,-5(5)  *
55:    LD 0,-4(5)  *load rhs
56:    LD 1,-5(5)  *load lhs
57:    ST 0,0(1)  *
58:    ST 1,-2(6)  *storing value
*assignment
*looking for x
59:    LDA 0,-2(5)  *load address
60:    ST 0,-4(5)  *
*looking for x
61:    LD 0,-2(5)  *load value
62:    ST 0,-6(5)  *store lhs
63:    LDC 0,1(0)  *load constant
64:    ST 0,-7(5)  *store rhs
65:    LD 0,-6(5)  *
66:    LD 1,-7(5)  *
67:    SUB 0,0,1  *subtracting
68:    ST 0,-5(5)  *
69:    LD 0,-4(5)  *load rhs
70:    LD 1,-5(5)  *load lhs
71:    ST 0,0(1)  *
72:    ST 1,-2(5)  *storing value
73:    LDA 7,-41(7)  *move to test
44:    JEQ 0,29(7)  *exit loop
*looking for fac
74:    LD 0,-2(6)  *load value
75:    ST 0,-5(5)  *push
*function call: output
76:    ST 5,-3(5)  *store current fp
77:    LDA 5,-3(5)  *push new frame
78:    LDA 0,1(7)  *save return in ac
79:    LDA 7,-73(7)  *jump to function entry
80:    LD 5,0(5)  *pop current frame
81:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,70(7)  *jump to finale
* Finale
82:    ST 5,-6(5)  *push ofp
83:    LDA 5,-6(5)  *push frame
84:    LDA 0,1(7)  *load ac with ret ptr
85:    LDA 7,-74(7)  *jump to main loc
86:    LD 5,0(5)  *pop frame
* End of Execution
87:    HALT 0,0,0  *
