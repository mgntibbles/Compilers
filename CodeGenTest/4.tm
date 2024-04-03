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
12:    ST 0,-1(5)  *store return
13:    LDC 0,1(0)  *load constant
14:    ST 0,-4(5)  *push
*function call: output
15:    ST 5,-2(5)  *store current fp
16:    LDA 5,-2(5)  *push new frame
17:    LDA 0,1(7)  *save return in ac
18:    LDA 7,-12(7)  *jump to function entry
19:    LD 5,0(5)  *pop current frame
20:    LD 7,-1(5)  *return back to the caller
11:    LDA 7,9(7)  *skip function
* processing function: main
22:    ST 0,-1(5)  *store return
*looking for x
23:    LD 0,-2(6)  *load value
24:    ST 0,-4(5)  *push
*function call: foo
25:    ST 5,-2(5)  *store current fp
26:    LDA 5,-2(5)  *push new frame
27:    LDA 0,1(7)  *save return in ac
28:    LDA 7,-17(7)  *jump to function entry
29:    LD 5,0(5)  *pop current frame
30:    LD 7,-1(5)  *return back to the caller
21:    LDA 7,9(7)  *skip function
* Finale22
31:    ST 5,-3(5)  *push ofp
32:    LDA 5,-3(5)  *push frame
33:    LDA 0,1(7)  *load ac with ret ptr
34:    LDA 7,-13(7)  *jump to main loc
35:    LD 5,0(5)  *pop frame
* End of Execution
36:    HALT 0,0,0  *
