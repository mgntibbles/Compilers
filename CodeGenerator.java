/*
  Created by: Megan Tibbles
  File Name: ShowTreeVisitor.java
  Purpose: traverse the nodes of the tree to output
*/
  
import java.util.HashMap;

import absyn.*;

public class CodeGenerator implements AbsynVisitor {
  int mainEntry = -1, globalOffset = -1, inputEntry, outputEntry;
  int pc = 7, gp = 6, fp = 5, ac = 0, ac1 = 1;
  int ofpFO = 0, retFO = -1, initFO = -2;
  int emitLoc = 0;
  int highEmitLoc = 0;
  int global = 0;

  int returnOffset = 0;
  public HashMap<String, Dec> declarations = new HashMap<String, Dec>();

  public void emitRO(String op, int r, int s, int t, String comment){
    System.out.println(emitLoc+":    "+op+" "+r+","+s+","+t + "  *"+comment);
    ++emitLoc;
    if(emitLoc>highEmitLoc){
        highEmitLoc = emitLoc;
    }
  }

  public void emitRM(String op, int r, int d, int s, String comment){
    System.out.println(emitLoc+":    "+op+" "+r+","+d+"("+s + ")  *"+comment);
    ++emitLoc;
    if(emitLoc>highEmitLoc){
        highEmitLoc = emitLoc;
    }
  }

  public void emitRM_Abs(String op, int r, int a, String comment){
    System.out.println(emitLoc+":    "+op+" "+r+","+(a-(emitLoc+1))+"("+pc + ")  *"+comment);
    ++emitLoc;
    if(emitLoc>highEmitLoc){
        highEmitLoc = emitLoc;
    }
  }


  int emitSkip(int distance){
    int i = emitLoc;
    emitLoc += distance;
    if (emitLoc>highEmitLoc){
        highEmitLoc = emitLoc;
    }
    return i;
  }

  void emitBackup( int loc){
    if (loc > highEmitLoc){
        emitComment("emitBackup not valid");
    }
    emitLoc = loc;
  }

  void emitRestore(){
    emitLoc = highEmitLoc;
  }
  void emitComment(String comment){
    System.out.println("*"+ comment);
  }

  public void visit(Absyn trees){
    //generate the prelude
    System.out.println("* Standard prelude:");
    emitRM("LD",gp, 0, ac, "load gp with maxaddress");
    emitRM("LDA", fp, 0, gp, "copy gp to fp");
    emitRM("ST", ac, 0, ac, "clear location 0");
    //generate the i/o routines
    int savedLoc = emitSkip(1);
    //code for input
    System.out.println("* Jump around i/o routine:");
    System.out.println("* Code for input routine:");
    inputEntry = emitLoc;
    emitRM("ST", 0, -1, fp, "store return");
    emitRO("IN", 0, 0, 0, "clear location 0");
    emitRM("LD", pc, -1, fp, "return to caller");
    //code for output
    outputEntry = emitLoc;
    System.out.println("* Code for output routine:");
    emitRM("ST", 0, -1, fp, "store return");
    emitRM("LD", 0, -2, fp, "load output value");
    emitRO("OUT", 0,0,0,"");
    emitRM("LD", pc, -1, fp, "return to caller");
    int savedLoc2 = emitSkip(0);
    emitBackup(savedLoc);
    emitRM_Abs("LDA", pc, savedLoc2, "");
    emitRestore();
    //savedLoc2 = emitSkip(1);
    System.out.println("* End Standard prelude.");
    //make a request to the visit method for DecList
    trees.accept(this, initFO, false);
    //generale finale
    if (mainEntry == -1){
        System.err.println("Error: Missing main");
        emitRO("HALT",0,0,0,"Error, forced to stop");
    }
    //emitBackup(savedLoc2);
    //emitRM("LDA",pc, highEmitLoc - (emitLoc+1), pc, "jump to finale");
    //emitRestore();
    System.out.println("* Finale" + mainEntry);
    globalOffset -=1;
    emitRM("ST", fp, globalOffset+ofpFO, fp, "push ofp");
    emitRM("LDA", fp, globalOffset, fp, "push frame");
    emitRM("LDA", ac, 1, pc, "load ac with ret ptr");
    emitRM_Abs("LDA", pc, mainEntry, "jump to main loc");
    emitRM("LD", fp, ofpFO, fp, "pop frame");
    System.out.println("* End of Execution");
    emitRO("HALT",0,0,0,"");
  }

  
  public void visit( ExpList expList, int offset, boolean isAddress ) {
    while( expList != null ) {
      expList.head.accept( this, offset, false );
      expList = expList.tail;
    } 
  }

  public void visit( DecList decList, int offset, boolean isAddress ) {
    while( decList != null ) {
      decList.head.accept( this, offset--, false);
      decList = decList.tail;
    } 
  }

  public void visit( VarDecList varDecList, int offset, boolean isAddress ) {
    while( varDecList != null ) {
      varDecList.head.accept( this, offset--, false);
      varDecList = varDecList.tail;
      returnOffset++;
    } 
  }
  
  public void visit( AssignExp exp, int offset, boolean isAddress ) {
    emitComment("assignment");
    int nestLevel = 0;
    int varOff = 0;
    if (exp.dtype instanceof SimpleDec){
      SimpleDec temp = (SimpleDec)exp.dtype;
      nestLevel = temp.nestLevel;
      varOff = temp.offset;
    }
    if (global==0){
      globalOffset-=1;
    }
    //System.out.println("assign: "+offset);
    exp.lhs.accept( this, offset-1, true);
    if (exp.lhs instanceof SimpleVar){
      emitRM("ST", ac, offset-1, fp, "");
    }
    exp.rhs.accept( this, offset-2, false);
    emitRM("ST", ac, offset-2, fp, "");

    emitRM("LD", ac, offset-1, fp, "load rhs"); 
    emitRM("LD", ac1, offset-2, fp, "load lhs"); 
    //emitRM("LD", ac1, offset-2, fp, "load rhs");
    emitRM("ST", ac, 0, 1, "");
    if (nestLevel == 0){
      emitRM("ST",ac1, varOff, gp, "storing value");
    }else {
      emitRM("ST",ac1, varOff, fp, "storing value");
    }
  }

  public void visit( IfExp exp, int offset, boolean isAddress ) {
    //indent( offset );
    //offset++;
    emitComment("if statement");
    if (exp.test != null){
      //System.out.println( "IfExp:" );
      exp.test.accept( this, offset, false);
      int savedLoc = emitSkip(1);
      exp.thenpart.accept( this, offset, false);
      int savedLoc2 = emitSkip(0);
      emitBackup(savedLoc);
      emitRM_Abs("JEQ", 0, savedLoc2, "else part");
      emitRestore();
      NilExp i = new NilExp(0, 0);
      if (exp.elsepart.getClass() != i.getClass()){
        //indent( offset );
        //System.out.println( "Else:" );
        exp.elsepart.accept( this, offset, false);
      }
      emitComment("done if");
    }
  }

  public void visit( IntExp exp, int offset, boolean isAddress ) {
    //indent( offset );
    //System.out.println( "IntExp: " + exp.value ); 
    emitRM("LDC", ac, exp.value, ac, "load constant");
    //emitRM("ST", ac, offset, fp, "");
  }

  public void visit( BoolExp exp, int offset, boolean isAddress ) {
    //indent( offset );
    //System.out.println( "BoolExp: " + exp.value ); 
  }

  public void visit( VarExp exp, int offset, boolean isAddress ) {
    //indent( offset );
    //System.out.println( "VarExp: "); 
    exp.name.accept( this, offset, false);
  }

  public void visit( IndexVar exp, int offset, boolean isAddress ) {
    emitComment("looking for "+exp.name);
    Dec temp = declarations.get(exp.name);
    if (temp!=null){
      VarDec varDtype = (VarDec)temp;
      if (exp.index!=null)
        if (varDtype.nestLevel == 0){
          emitRM("LD", ac, varDtype.offset, gp, "load address");
          emitRM("ST", ac, offset--, gp, "stor array");
        } else {
          emitRM("LD", ac, varDtype.offset, fp, "load address");
          emitRM("ST", ac, offset--, fp, "store array");
        }
        exp.index.accept( this, offset, false);
          //emitRM("ST", ac, offset, fp, "");
      } else {
        System.err.println("Undefined variable");
        emitRO("HALT",0,0,0," Error, forced ending");
      }
  }

  public void visit( CallExp exp, int offset, boolean isAddress ) {
    //emitRM("ST", ac, retFO, fp, "store return address");
    int localOff = offset + initFO;
    if (exp.args!=null){
      ExpList args = exp.args;
      while( args != null ) {
        args.head.accept( this, localOff, false );
        emitRM("ST", ac, localOff, fp, "push");
        //emitRO("OUT",0,0,0,"");
        localOff--;
        args = args.tail;
      } 
    }
    int entry = 0;
    if (exp.func.equals("output")){
      entry = outputEntry;
    } else if (exp.func.equals("input")){
      entry = inputEntry;
    } else {
      Dec temp = declarations.get(exp.func);
      FunctionDec varDtype = (FunctionDec)temp;
      if (varDtype == null){
        System.err.println("Using undefined function");
        emitRO("HALT",0,0,0,"Error, forced ending");
      } else {
        entry = varDtype.funaddr;
      }
    }
    emitComment("function call: "+exp.func);
    emitRM("ST", fp, offset+ofpFO, fp, "store current fp");
    emitRM("LDA", fp, offset, fp, "push new frame");
    emitRM("LDA",ac, 1, pc, "save return in ac");
    emitRM_Abs("LDA", pc, entry, "jump to function entry");
    emitRM("LD", fp, 0, fp, "pop current frame");
    //emitRM("ST", ac, offset, fp, "save return value to caller's stack frame");

    //emitRM("LD", pc, retFO, fp, "return to caller");
  }

  public void visit( WhileExp exp, int offset, boolean isAddress ){
    emitComment("while");
    int savedLoc = emitSkip(0);
    exp.test.accept( this, offset, false);
    int savedLoc2 = emitSkip(1);
    exp.body.accept( this, offset, false);
    emitRM_Abs("LDA", pc, savedLoc, "move to test");
    int savedLoc3 = emitSkip(0);
    emitBackup(savedLoc2);
    emitRM_Abs("JEQ", 0, savedLoc3, "exit loop");
    emitRestore();
  }

  public void visit( CompoundExp exp, int offset, boolean isAddress ){
    //indent(offset);
    //System.out.println("CompoundExp: ");
    //offset++;
    if (exp.decs!=null){
      exp.decs.accept( this, offset--, false);
    }
    if (exp.exps!= null){
      exp.exps.accept( this, offset--, false);
    }
    if (exp.decs == null && exp.exps == null){
      //indent(offset);
      //System.out.println("Empty Function");
    }
  }

  public void visit( SimpleVar exp, int offset, boolean isAddress ){
    //System.out.println("simplevar:"+offset);
    emitComment("looking for "+exp.name);
    Dec temp = declarations.get(exp.name);
    if (temp != null){
      VarDec varDtype = (VarDec)temp;
      if (isAddress){
          if (varDtype.nestLevel == 0){
            emitRM("LDA", ac, varDtype.offset, gp, "load address");
          } else {
            emitRM("LDA", ac, varDtype.offset, fp, "load address");
          }
          //emitRM("ST", ac, offset, fp, "");
      } else {
        if (varDtype.nestLevel == 0){
          emitRM("LD", ac, varDtype.offset, gp, "load value");
        } else {
          emitRM("LD", ac, varDtype.offset, fp, "load value");
        }
      }
    } else {
      System.err.println("Undefined variable");
      emitRO("HALT",0,0,0," Error, forced ending");
    }
}

  public void visit( NilExp exp, int offset, boolean isAddress ){

  }

  public void visit( FunctionDec exp, int offset, boolean isAddress ){
    emitComment(" processing function: " + exp.func);
    offset = -2;
    returnOffset = offset;
    global=1; // in local
    if (exp.func.equals("main")){
        mainEntry = emitLoc+1;
        globalOffset = offset;
    }
    exp.funaddr = emitLoc+1;
    declarations.put(exp.func, exp);
    exp.result.accept( this, offset, false);
    if ( exp.params != null)
      exp.params.accept( this, offset, false);
      offset = returnOffset;
    if (!(exp.body instanceof NilExp)){
      int savedLoc = emitSkip(1);
      emitRM("ST", ac, retFO, fp, "store return");
      exp.body.accept( this, offset, false);
      emitRM("LD", pc, retFO, fp, "return back to the caller");
      int savedLoc2 = emitSkip(0);
      emitBackup(savedLoc);
      emitRM_Abs("LDA", pc, savedLoc2, "skip function");
      emitRestore();
    } else {
      declarations.put(exp.func, null);
    }
    global = 0;
  }

  public void visit( SimpleDec exp, int offset, boolean isAddress ){
    System.out.println("* processing "+ exp.name);
    if (global == 0){
      globalOffset-=1;
    }
    exp.offset = offset;
    //System.out.println(offset+" "+exp.name);
    exp.nestLevel = global;
    exp.typ.accept( this, offset, false);
    declarations.put(exp.name, exp);
    returnOffset=offset;
  }

  public void visit( ArrayDec exp, int offset, boolean isAddress ){
    emitComment("processing "+exp.name);
    if (exp.size!=-1){
      //System.out.println("ArrayDec: " + exp.name + " Size: "+exp.size);
      exp.typ.accept( this, offset, false);
    }
    if (global == 0){
      globalOffset-=exp.size;
    }
    declarations.put(exp.name, exp);
    returnOffset = offset;
  }

  public void visit( ReturnExp retExp, int offset, boolean isAddress ){
    emitComment("return");
    retExp.exp.accept( this, offset, false);
    emitRM("LD", pc, -1, fp, "");
  }


  public void visit (NameTy exp, int offset, boolean isAddress ){
    //indent( offset );
    //System.out.print("Type: ");
    switch( exp.typ ){
    case NameTy.BOOL:
      //System.out.println(" bool ");
      break;
    case NameTy.INT:
      //System.out.println(" int ");
      break;
    case NameTy.VOID:
      //System.out.println(" void ");
      break;
    default:
      //System.out.println("Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
  }
  
  public void visit( OpExp exp, int offset, boolean isAddress ) {
    //System.out.println(offset);
    if (exp.left.getClass().toString() != "NilExp"){
      exp.left.accept( this, offset-1, false);
      if (exp.left instanceof IntExp || exp.left instanceof VarExp || exp.left instanceof OpExp){
        emitRM("ST", ac, offset-1, fp, "store lhs");
      }
    }
    if (exp.right != null){
      exp.right.accept( this, offset-2, false);
      //if (exp.right instanceof IntExp || exp.right instanceof VarExp){
        emitRM("ST", ac, offset-2, fp, "store rhs");
      //}
    }
    emitRM("LD", ac, offset-1, fp, "");
    emitRM("LD", ac1, offset-2, fp, "");
    switch( exp.op ) {
      case OpExp.PLUS:
        emitRO("ADD", ac, ac, ac1, "adding");
        //emitRM("ST", ac, offset, fp, "storing result");
        break;
      case OpExp.MINUS:
        emitRO("SUB", ac, ac, ac1, "subtracting");
        //emitRM("ST", ac, offset, fp, "storing result");
        break;
      case OpExp.TIMES:
        emitRO("MUL", ac, ac, ac1, "multiplying");
        //emitRM("ST", ac, offset, fp, "storing result");
        break;
      case OpExp.OVER:
        emitRO("DIV", ac, ac, ac1, "dividing");
       //emitRM("ST", ac, offset, fp, "storing result");
        break;
      case OpExp.EQ:
        emitRO("SUB", ac, ac1, ac, "op: ==");
        emitRM("JEQ", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " = " );
        break;
      case OpExp.LT:
        emitRO("SUB", ac, ac1, ac, "op: <");
        emitRM("JGT", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " < " );
        break;
      case OpExp.GT:
        emitRO("SUB", ac, ac1, ac, "op: >");
        emitRM("JLT", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " > " );
        break;
      case OpExp.UMINUS:
        //System.out.println( " - " );
        break;
      case OpExp.NEQ:
        emitRO("SUB", ac, ac1, ac, "op: !=");
        emitRM("JNE", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " != ");
        break;
      case OpExp.NOT:
        //System.out.println( " ~ ");
        break;
      case OpExp.AND:
        //System.out.println( " && ");
        break;
      case OpExp.OR:
        //System.out.println( " || ");
        break;
      case OpExp.GTE:
        emitRO("SUB", ac, ac1, ac, "op: >=");
        emitRM("JLE", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " >= ");
        break;
      case OpExp.LTE:
        emitRO("SUB", ac, ac1, ac, "op: <=");
        emitRM("JGE", ac, 2, pc, "");
        emitRM("LDC", ac, 0, 0, "false case");
        emitRM("LDA", pc, 1, pc, "unconditional jump");
        emitRM("LDC", ac, 1, 0, "true case");
        //System.out.println( " <= ");
        break;
      default:
        //System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
  }

  public void visit( NodeType exp,  int offset, boolean isAddress ) {

  }

}
