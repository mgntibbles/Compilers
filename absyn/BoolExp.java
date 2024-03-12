/*
  Created by: Megan Tibbles
  File Name: BoolExp.java
*/
package absyn;

public class BoolExp extends Exp {
  public Boolean value;

  public BoolExp( int row, int col, Boolean value ) {
    this.row = row;
    this.col = col;
    this.value = value;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
