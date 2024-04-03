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
* processing function: sub
* processing a
* processing b
12:    ST 0,-1(5)  *store return
*looking for a
13:    LD 0,-2(5)  *load value
14:    ST 0,-4(5)  *push
15:    HALT 0,0,0  *Error, forced ending
*function call: add
16:    ST 5,-2(5)  *store current fp
17:    LDA 5,-2(5)  *push new frame
18:    LDA 0,1(7)  *save return in ac
19:    LDA 7,-20(7)  *jump to function entry
20:    LD 5,0(5)  *pop current frame
*return
*looking for a
21:    LD 0,-2(5)  *load value
22:    ST 0,-3(5)  *store lhs
*looking for b
23:    LD 0,-3(5)  *load value
24:    ST 0,-4(5)  *store rhs
25:    LD 0,-3(5)  *
26:    LD 1,-4(5)  *
27:    SUB 0,0,1  *subtracting
28:    LD 7,-1(5)  *
29:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,18(7)  *skip function
* processing function: add
* processing function: main
31:    ST 0,-1(5)  *store return
* processing a
* processing b
*assignment
*looking for a
32:    LDA 0,-2(5)  *load address
33:    ST 0,-4(5)  *
*function call: input
34:    ST 5,-5(5)  *store current fp
35:    LDA 5,-5(5)  *push new frame
36:    LDA 0,1(7)  *save return in ac
37:    LDA 7,-34(7)  *jump to function entry
38:    LD 5,0(5)  *pop current frame
39:    ST 0,-5(5)  *
40:    LD 0,-4(5)  *load rhs
41:    LD 1,-5(5)  *load lhs
42:    ST 0,0(1)  *
43:    ST 1,-2(5)  *storing value
*assignment
*looking for b
44:    LDA 0,-3(5)  *load address
45:    ST 0,-4(5)  *
46:    LDC 0,1(0)  *load constant
47:    ST 0,-7(5)  *push
48:    LDC 0,2(0)  *load constant
49:    ST 0,-8(5)  *push
50:    HALT 0,0,0  *Error, forced ending
*function call: add
51:    ST 5,-5(5)  *store current fp
52:    LDA 5,-5(5)  *push new frame
53:    LDA 0,1(7)  *save return in ac
54:    LDA 7,-55(7)  *jump to function entry
55:    LD 5,0(5)  *pop current frame
56:    ST 0,-5(5)  *
57:    LD 0,-4(5)  *load rhs
58:    LD 1,-5(5)  *load lhs
59:    ST 0,0(1)  *
60:    ST 1,0(6)  *storing value
*looking for a
61:    LD 0,-2(5)  *load value
62:    ST 0,-7(5)  *push
63:    LDC 0,3(0)  *load constant
64:    ST 0,-8(5)  *push
*function call: sub
65:    ST 5,-5(5)  *store current fp
66:    LDA 5,-5(5)  *push new frame
67:    LDA 0,1(7)  *save return in ac
68:    LDA 7,-57(7)  *jump to function entry
69:    LD 5,0(5)  *pop current frame
70:    ST 0,-5(5)  *push
*function call: output
71:    ST 5,-3(5)  *store current fp
72:    LDA 5,-3(5)  *push new frame
73:    LDA 0,1(7)  *save return in ac
74:    LDA 7,-68(7)  *jump to function entry
75:    LD 5,0(5)  *pop current frame
76:    LD 7,-1(5)  *return back to the caller
30:    LDA 7,46(7)  *skip function
* Finale31
77:    ST 5,-3(5)  *push ofp
78:    LDA 5,-3(5)  *push frame
79:    LDA 0,1(7)  *load ac with ret ptr
80:    LDA 7,-50(7)  *jump to main loc
81:    LD 5,0(5)  *pop frame
* End of Execution
82:    HALT 0,0,0  *
