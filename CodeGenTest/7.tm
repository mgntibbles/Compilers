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
* processing a
12:    ST 0,-1(5)  *store return
*return
*looking for a
13:    LD 0,-2(5)  *load value
14:    LD 7,-1(5)  *
15:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,4(7)  *skip function
* processing function: main
17:    ST 0,-1(5)  *store return
* processing x
* processing b
*assignment
*looking for x
18:    LDA 0,-2(5)  *load address
19:    ST 0,-4(5)  *
20:    LDC 0,9(0)  *load constant
21:    ST 0,-5(5)  *
22:    LD 0,-4(5)  *load rhs
23:    LD 1,-5(5)  *load lhs
24:    ST 0,0(1)  *
25:    ST 1,-2(5)  *storing value
*if statement
*looking for x
26:    LD 0,-2(5)  *load value
27:    ST 0,-4(5)  *store lhs
28:    LDC 0,8(0)  *load constant
29:    ST 0,-5(5)  *store rhs
30:    LD 0,-4(5)  *
31:    LD 1,-5(5)  *
32:    ADD 0,0,1  *adding
*assignment
*looking for b
34:    LDA 0,-3(5)  *load address
35:    ST 0,-4(5)  *
*looking for x
36:    LD 0,-2(5)  *load value
37:    ST 0,-7(5)  *push
*function call: add
38:    ST 5,-5(5)  *store current fp
39:    LDA 5,-5(5)  *push new frame
40:    LDA 0,1(7)  *save return in ac
41:    LDA 7,-30(7)  *jump to function entry
42:    LD 5,0(5)  *pop current frame
43:    ST 0,-5(5)  *
44:    LD 0,-4(5)  *load rhs
45:    LD 1,-5(5)  *load lhs
46:    ST 0,0(1)  *
47:    ST 1,0(6)  *storing value
*assignment
*looking for x
48:    LDA 0,-2(5)  *load address
49:    ST 0,-4(5)  *
*looking for x
50:    LD 0,-2(5)  *load value
51:    ST 0,-6(5)  *store lhs
52:    LDC 0,1(0)  *load constant
53:    ST 0,-7(5)  *store rhs
54:    LD 0,-6(5)  *
55:    LD 1,-7(5)  *
56:    ADD 0,0,1  *adding
57:    ST 0,-5(5)  *
58:    LD 0,-4(5)  *load rhs
59:    LD 1,-5(5)  *load lhs
60:    ST 0,0(1)  *
61:    ST 1,0(6)  *storing value
33:    JEQ 0,28(7)  *else part
*done if
*looking for x
62:    LD 0,-2(5)  *load value
63:    ST 0,-5(5)  *push
*function call: output
64:    ST 5,-3(5)  *store current fp
65:    LDA 5,-3(5)  *push new frame
66:    LDA 0,1(7)  *save return in ac
67:    LDA 7,-61(7)  *jump to function entry
68:    LD 5,0(5)  *pop current frame
69:    LD 7,-1(5)  *return back to the caller
16:    LDA 7,53(7)  *skip function
* Finale17
70:    ST 5,-3(5)  *push ofp
71:    LDA 5,-3(5)  *push frame
72:    LDA 0,1(7)  *load ac with ret ptr
73:    LDA 7,-57(7)  *jump to main loc
74:    LD 5,0(5)  *pop frame
* End of Execution
75:    HALT 0,0,0  *
