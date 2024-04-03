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
* processing x
* processing y
* processing function: check
* processing v
12:    ST 0,-1(5)  *store return
*if statement
*looking for v
13:    LD 0,-2(5)  *load value
14:    ST 0,-2(5)  *store lhs
15:    LDC 0,10(0)  *load constant
16:    ST 0,-3(5)  *store rhs
17:    LD 0,-2(5)  *
18:    LD 1,-3(5)  *
19:    SUB 0,1,0  *op: <=
20:    JGE 0,2(7)  *
21:    LDC 0,0(0)  *false case
22:    LDA 7,1(7)  *unconditional jump
23:    LDC 0,1(0)  *true case
*return
25:    LDC 0,10(0)  *load constant
26:    LD 7,-1(5)  *
24:    JEQ 0,2(7)  *else part
*return
*looking for v
27:    LD 0,-2(5)  *load value
28:    ST 0,-2(5)  *store lhs
*looking for v
29:    LD 0,-2(5)  *load value
30:    ST 0,-3(5)  *store rhs
31:    LD 0,-2(5)  *
32:    LD 1,-3(5)  *
33:    MUL 0,0,1  *multiplying
34:    LD 7,-1(5)  *
*done if
35:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,24(7)  *skip function
* processing function: main
37:    ST 0,-1(5)  *store return
*assignment
*looking for x
38:    LDA 0,-2(6)  *load address
39:    ST 0,-3(5)  *
*function call: input
40:    ST 5,-4(5)  *store current fp
41:    LDA 5,-4(5)  *push new frame
42:    LDA 0,1(7)  *save return in ac
43:    LDA 7,-40(7)  *jump to function entry
44:    LD 5,0(5)  *pop current frame
45:    ST 0,-4(5)  *
46:    LD 0,-3(5)  *load rhs
47:    LD 1,-4(5)  *load lhs
48:    ST 0,0(1)  *
49:    ST 1,-2(6)  *storing value
*assignment
*looking for y
50:    LDA 0,-3(6)  *load address
51:    ST 0,-3(5)  *
*looking for x
52:    LD 0,-2(6)  *load value
53:    ST 0,-6(5)  *push
*function call: check
54:    ST 5,-4(5)  *store current fp
55:    LDA 5,-4(5)  *push new frame
56:    LDA 0,1(7)  *save return in ac
57:    LDA 7,-46(7)  *jump to function entry
58:    LD 5,0(5)  *pop current frame
59:    ST 0,-4(5)  *
60:    LD 0,-3(5)  *load rhs
61:    LD 1,-4(5)  *load lhs
62:    ST 0,0(1)  *
63:    ST 1,-3(6)  *storing value
*looking for y
64:    LD 0,-3(6)  *load value
65:    ST 0,-4(5)  *push
*function call: output
66:    ST 5,-2(5)  *store current fp
67:    LDA 5,-2(5)  *push new frame
68:    LDA 0,1(7)  *save return in ac
69:    LDA 7,-63(7)  *jump to function entry
70:    LD 5,0(5)  *pop current frame
71:    LD 7,-1(5)  *return back to the caller
36:    LDA 7,35(7)  *skip function
* Finale37
72:    ST 5,-3(5)  *push ofp
73:    LDA 5,-3(5)  *push frame
74:    LDA 0,1(7)  *load ac with ret ptr
75:    LDA 7,-39(7)  *jump to main loc
76:    LD 5,0(5)  *pop frame
* End of Execution
77:    HALT 0,0,0  *
