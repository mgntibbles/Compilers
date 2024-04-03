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
* processing function: foo
* processing a
* processing b
12:    ST 0,-1(5)  *store return
*return
13:    LD 7,-1(5)  *
14:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,3(7)  *skip function
* processing function: main
16:    ST 0,-1(5)  *store return
* processing y
*assignment
*looking for y
17:    LDA 0,-2(5)  *load address
18:    ST 0,-4(5)  *
19:    LDC 0,9(0)  *load constant
20:    ST 0,-5(5)  *
21:    LD 0,-4(5)  *load rhs
22:    LD 1,-5(5)  *load lhs
23:    ST 0,0(1)  *
24:    ST 1,-2(5)  *storing value
*assignment
*looking for x
25:    LDA 0,-2(6)  *load address
26:    ST 0,-4(5)  *
*function call: input
27:    ST 5,-5(5)  *store current fp
28:    LDA 5,-5(5)  *push new frame
29:    LDA 0,1(7)  *save return in ac
30:    LDA 7,-27(7)  *jump to function entry
31:    LD 5,0(5)  *pop current frame
32:    ST 0,-5(5)  *
33:    LD 0,-4(5)  *load rhs
34:    LD 1,-5(5)  *load lhs
35:    ST 0,0(1)  *
36:    ST 1,-2(6)  *storing value
*if statement
*looking for x
37:    LD 0,-2(6)  *load value
38:    ST 0,-5(5)  *push
39:    LDC 0,1(0)  *load constant
40:    ST 0,-6(5)  *push
*function call: foo
41:    ST 5,-3(5)  *store current fp
42:    LDA 5,-3(5)  *push new frame
43:    LDA 0,1(7)  *save return in ac
44:    LDA 7,-33(7)  *jump to function entry
45:    LD 5,0(5)  *pop current frame
46:    JEQ 0,0(7)  *else part
*done if
*assignment
*looking for y
47:    LDA 0,-2(5)  *load address
48:    ST 0,-4(5)  *
49:    ST 0,-7(5)  *push
*function call: foo
50:    ST 5,-5(5)  *store current fp
51:    LDA 5,-5(5)  *push new frame
52:    LDA 0,1(7)  *save return in ac
53:    LDA 7,-42(7)  *jump to function entry
54:    LD 5,0(5)  *pop current frame
55:    ST 0,-5(5)  *
56:    LD 0,-4(5)  *load rhs
57:    LD 1,-5(5)  *load lhs
58:    ST 0,0(1)  *
59:    ST 1,-2(5)  *storing value
*assignment
*looking for b
60:    LDA 0,-3(5)  *load address
61:    ST 0,-4(5)  *
*looking for y
62:    LD 0,-2(5)  *load value
63:    ST 0,-5(5)  *
64:    LD 0,-4(5)  *load rhs
65:    LD 1,-5(5)  *load lhs
66:    ST 0,0(1)  *
67:    ST 1,0(6)  *storing value
68:    LD 7,-1(5)  *return back to the caller
15:    LDA 7,53(7)  *skip function
* Finale16
69:    ST 5,-3(5)  *push ofp
70:    LDA 5,-3(5)  *push frame
71:    LDA 0,1(7)  *load ac with ret ptr
72:    LDA 7,-57(7)  *jump to main loc
73:    LD 5,0(5)  *pop frame
* End of Execution
74:    HALT 0,0,0  *
