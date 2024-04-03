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
* processing function: main
12:    ST 0,-1(5)  *store return
*assignment
*looking for x
13:    LDA 0,-2(6)  *load address
14:    ST 0,-3(5)  *
*function call: input
15:    ST 5,-4(5)  *store current fp
16:    LDA 5,-4(5)  *push new frame
17:    LDA 0,1(7)  *save return in ac
18:    LDA 7,-15(7)  *jump to function entry
19:    LD 5,0(5)  *pop current frame
20:    ST 0,-4(5)  *
21:    LD 0,-3(5)  *load rhs
22:    LD 1,-4(5)  *load lhs
23:    ST 0,0(1)  *
24:    ST 1,-2(6)  *storing value
*assignment
*looking for y
25:    LDA 0,-3(6)  *load address
26:    ST 0,-3(5)  *
*looking for x
27:    LD 0,-2(6)  *load value
28:    ST 0,-5(5)  *store lhs
29:    LDC 0,1(0)  *load constant
30:    ST 0,-6(5)  *store rhs
31:    LD 0,-5(5)  *
32:    LD 1,-6(5)  *
33:    ADD 0,0,1  *adding
34:    ST 0,-4(5)  *
35:    LD 0,-3(5)  *load rhs
36:    LD 1,-4(5)  *load lhs
37:    ST 0,0(1)  *
38:    ST 1,-3(6)  *storing value
*looking for y
39:    LD 0,-3(6)  *load value
40:    ST 0,-4(5)  *push
*function call: output
41:    ST 5,-2(5)  *store current fp
42:    LDA 5,-2(5)  *push new frame
43:    LDA 0,1(7)  *save return in ac
44:    LDA 7,-38(7)  *jump to function entry
45:    LD 5,0(5)  *pop current frame
46:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,35(7)  *skip function
* Finale12
47:    ST 5,-3(5)  *push ofp
48:    LDA 5,-3(5)  *push frame
49:    LDA 0,1(7)  *load ac with ret ptr
50:    LDA 7,-39(7)  *jump to main loc
51:    LD 5,0(5)  *pop frame
* End of Execution
52:    HALT 0,0,0  *
