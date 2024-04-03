/*
  Created by: Megan Tibbles
  File Name: ShowTreeVisitor.java
  Purpose: traverse the nodes of the tree to output
*/
  
import absyn.*;

public class ShowTreeVisitor implements AbsynVisitor {

  final static int SPACES = 4;

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }
  
  public void visit( ExpList expList, int level, boolean flag ) {
    while( expList != null ) {
      expList.head.accept( this, level, flag);
      expList = expList.tail;
    } 
  }

  public void visit( DecList decList, int level, boolean flag ) {
    while( decList != null ) {
      decList.head.accept( this, level, flag);
      decList = decList.tail;
    } 
  }

  public void visit( VarDecList varDecList, int level, boolean flag ) {
    //int count = 0;
    while( varDecList != null ) {
      //System.out.println(count);
      varDecList.head.accept( this, level, flag);
      varDecList = varDecList.tail;
      //count++;
    } 
  }
  
  public void visit( AssignExp exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "AssignExp:" );
    level++;
    exp.lhs.accept( this, level, flag);
    exp.rhs.accept( this, level, flag);
  }

  public void visit( IfExp exp, int level, boolean flag ) {
    indent( level );
    level++;
    if (exp.test != null){
      System.out.println( "IfExp:" );
      exp.test.accept( this, level, flag);
      exp.thenpart.accept( this, level, flag);
      NilExp i = new NilExp(0, 0);
      if (exp.elsepart.getClass() != i.getClass()){
        indent( level );
        System.out.println( "Else:" );
        exp.elsepart.accept( this, level, flag);
      }
    }
  }

  public void visit( IntExp exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "IntExp: " + exp.value ); 
  }

  public void visit( BoolExp exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "BoolExp: " + exp.value ); 
  }

  public void visit( VarExp exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "VarExp: "); 
    exp.name.accept( this, ++level, flag);
  }

  public void visit( IndexVar exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "IndexVar: " + exp.name); 
    if (exp.index!=null)
      exp.index.accept( this, ++level, flag);
  }

  public void visit( CallExp exp, int level, boolean flag ) {
    indent( level );
    System.out.println( "CallExp: " + exp.func ); 
    if (exp.args!=null)
      exp.args.accept( this, ++level, flag);
  }

  public void visit( WhileExp exp, int level, boolean flag ){
    indent( level );
    System.out.println( "WhileExp: ");
    level++;
    if (exp.test!=null)
      exp.test.accept( this, level, flag);
    if (exp.body!=null)
      exp.body.accept( this, level, flag);
  }

  public void visit( CompoundExp exp, int level, boolean flag  ){
    indent(level);
    System.out.println("CompoundExp: ");
    level++;
    if (exp.decs!=null){
      exp.decs.accept( this, level, flag);
    }
    if (exp.exps!= null){
      exp.exps.accept( this, level, flag);
    }
    if (exp.decs == null && exp.exps == null){
      indent(level);
      System.out.println("Empty Function");
    }
  }

  public void visit( SimpleVar exp, int level, boolean flag ){
    indent(level);
    System.out.println("SimpleVar: " + exp.name);
  }

  public void visit( NilExp exp, int level, boolean flag ){

  }

  public void visit( FunctionDec exp, int level, boolean flag ){
    indent(level);
    System.out.println("FunctionDec: " + exp.func);
    level++;
    exp.result.accept( this, level, flag);
    if ( exp.params != null)
      exp.params.accept( this, level, flag);
    if (exp.body.getClass().toString() != "NilExp")
      exp.body.accept( this, level, flag);
  }

  public void visit( SimpleDec exp, int level, boolean flag ){
    indent(level);
    System.out.println("SimpleDec: " + exp.name);
    exp.typ.accept( this, ++level, flag);
  }

  public void visit( ArrayDec exp, int level, boolean flag ){
    indent(level);
    if (exp.size!=-1){
      System.out.println("ArrayDec: " + exp.name + " Size: "+exp.size);
      exp.typ.accept( this, ++level, flag);
    }
  }

  public void visit( ReturnExp retExp, int level, boolean flag ){
    indent(level);
    System.out.println("ReturnExp: ");
    retExp.exp.accept( this, ++level, flag);
  }


  public void visit (NameTy exp, int level, boolean flag ){
    indent( level );
    System.out.print("Type: ");
    switch( exp.typ ){
    case NameTy.BOOL:
      System.out.println(" bool ");
      break;
    case NameTy.INT:
      System.out.println(" int ");
      break;
    case NameTy.VOID:
      System.out.println(" void ");
      break;
    default:
      System.out.println("Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
  }
  
  public void visit( OpExp exp, int level, boolean flag ) {
    indent( level );
    System.out.print( "OpExp:" ); 
    switch( exp.op ) {
      case OpExp.PLUS:
        System.out.println( " + " );
        break;
      case OpExp.MINUS:
        System.out.println( " - " );
        break;
      case OpExp.TIMES:
        System.out.println( " * " );
        break;
      case OpExp.OVER:
        System.out.println( " / " );
        break;
      case OpExp.EQ:
        System.out.println( " = " );
        break;
      case OpExp.LT:
        System.out.println( " < " );
        break;
      case OpExp.GT:
        System.out.println( " > " );
        break;
      case OpExp.UMINUS:
        System.out.println( " - " );
        break;
      case OpExp.NEQ:
        System.out.println( " != ");
        break;
      case OpExp.NOT:
        System.out.println( " ~ ");
        break;
      case OpExp.AND:
        System.out.println( " && ");
        break;
      case OpExp.OR:
        System.out.println( " || ");
        break;
      case OpExp.GTE:
        System.out.println( " >= ");
        break;
      case OpExp.LTE:
        System.out.println( " <= ");
        break;
      default:
        System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
    if (exp.left.getClass().toString() != "NilExp")
       exp.left.accept( this, level, flag);
    if (exp.right != null)
       exp.right.accept( this, level, flag);
  }

  public void visit( NodeType exp, int level, boolean flag ) {

  }

}
