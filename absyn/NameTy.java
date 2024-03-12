/*
  Created by: Megan Tibbles
  File Name: NameTy.java
*/
package absyn;

public class NameTy extends Absyn {
  public final static int BOOL  = 1;
  public final static int INT  = 2;
  public final static int VOID  = 3;

  public int typ;

  public NameTy( int row, int col, int typ ) {
    this.row = row;
    this.col = col;
    this.typ = typ;
  }

  public void accept( AbsynVisitor visitor, int level ) {
    visitor.visit( this, level );
  }
}
