/*
  Created by: Megan Tibbles
  File Name: VarExp.java
*/
package absyn;

public class VarExp extends Exp {
  public Var name;

  public VarExp( int row, int col, Var name ) {
    this.row = row;
    this.col = col;
    this.name = name;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
