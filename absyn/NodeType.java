package absyn;

public class NodeType extends Absyn{
    public Dec def;
    public int level;
    public String name;

    public NodeType( Dec def, int level, String name ) {
        this.def = def;
        this.level = level;
        this.name = name;
      }
    
      public void accept( AbsynVisitor visitor, int level ) {
        visitor.visit( this, level );
      }

}
