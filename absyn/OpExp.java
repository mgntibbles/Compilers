/*
  Created by: Megan Tibbles
  File Name: OpExp.java
*/
package absyn;

public class OpExp extends Exp {
  public final static int PLUS   = 0;
  public final static int MINUS  = 1;
  public final static int TIMES  = 2;
  public final static int OVER   = 3;
  public final static int EQ     = 4;
  public final static int LT     = 5;
  public final static int GT     = 6;
  public final static int UMINUS = 7;
  public final static int NEQ    = 8;
  public final static int NOT    = 9;
  public final static int AND    = 10;
  public final static int OR     = 11;
  public final static int GTE    = 12;
  public final static int LTE    = 13;

  public Exp left;
  public int op;
  public Exp right;

  public OpExp( int row, int col, Exp left, int op, Exp right ) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.op = op;
    this.right = right;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
