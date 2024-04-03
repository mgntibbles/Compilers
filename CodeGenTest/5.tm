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
* processing j
*assignment
*looking for j
13:    LDA 0,-2(5)  *load address
14:    ST 0,-4(5)  *
15:    LDC 0,1(0)  *load constant
16:    ST 0,-5(5)  *
17:    LD 0,-4(5)  *load rhs
18:    LD 1,-5(5)  *load lhs
19:    ST 0,0(1)  *
20:    ST 1,0(6)  *storing value
*looking for x
21:    HALT 0,0,0  * Error, forced ending
22:    ST 0,-5(5)  *push
*function call: output
23:    ST 5,-3(5)  *store current fp
24:    LDA 5,-3(5)  *push new frame
25:    LDA 0,1(7)  *save return in ac
26:    LDA 7,-20(7)  *jump to function entry
27:    LD 5,0(5)  *pop current frame
28:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,17(7)  *skip function
* Finale12
29:    ST 5,-3(5)  *push ofp
30:    LDA 5,-3(5)  *push frame
31:    LDA 0,1(7)  *load ac with ret ptr
32:    LDA 7,-21(7)  *jump to main loc
33:    LD 5,0(5)  *pop frame
* End of Execution
34:    HALT 0,0,0  *
