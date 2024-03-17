import java.util.*;
import absyn.*;

public class SemanticAnalyzer implements AbsynVisitor {
    final static int SPACES = 4;
    public Integer level = 0;
    public ArrayList<Integer> scopeTracker = new ArrayList<Integer>();
    public HashMap<String, ArrayList<NodeType>> table = new HashMap<String, ArrayList<NodeType>>();

  public void insert(String key, Dec def, int level, String name){
    NodeType nt = new NodeType(def, level, name);
    if (table.containsKey(key)){
        ArrayList<NodeType> list = table.get(key);
        list.add(nt);
        table.put(key, list);
    } else {
        ArrayList<NodeType> list = new ArrayList<NodeType>();
        list.add(nt);
        table.put(key, list);
    }
  }

  public Boolean lookup(String key, int level){
    if (table.containsKey(Integer.toString(level))){
      ArrayList<NodeType> list = table.get(Integer.toString(level));
      for (NodeType n : list){
        if (n.name.equals(key)){
          return true;
        }
      }
    }
    return false;
  }

  public int lookupAll(String key, int level){
    for(String i: table.keySet()){
      if(lookup(key, Integer.parseInt(i))){
        return Integer.parseInt(i);
      }
    }
    return -1;
  }

  public NodeType findNodeType(String name, ArrayList<NodeType> list){
    for (NodeType n : list){
      if (n.name.equals(name)){
        return n;
      }
    }
    return null;
  }

  public void delete(String currLevel){
    if (table.containsKey(currLevel)){
        table.remove(currLevel);
    }
  }

  public void deleteSpecific(String currLevel, String name){
    if (table.containsKey(currLevel)){
      ArrayList<NodeType> list = table.get(currLevel);
      NodeType temp = new NodeType(null, 0, "temp");
      for (NodeType n : list){
        if (n.name.equals(name)){
          temp = n;
        }
      }
      if (!temp.name.equals("temp")){
        list.remove(temp);
        table.remove(currLevel);
        table.put(currLevel, list);
      }
    }

  }

  public Boolean isInteger(Dec def, String decType){
    if (decType == "simple"){
      SimpleDec temp = (SimpleDec) def;
      if (temp.typ.typ == NameTy.INT){
        return true;
      }
    } else if (decType == "func"){
      FunctionDec temp = (FunctionDec) def;
      if (temp.result.typ == NameTy.INT){
        return true;
      }
    } else if (decType == "array"){
      ArrayDec temp = (ArrayDec) def;
      if (temp.typ.typ == NameTy.INT){
        return true;
      }
    }
    return false;
  }

  public Boolean isBoolean(Dec def, String decType){
    if (decType == "simple"){
      SimpleDec temp = (SimpleDec) def;
      if (temp.typ.typ == NameTy.BOOL){
        return true;
      }
    } else if (decType == "func"){
      FunctionDec temp = (FunctionDec) def;
      if (temp.result.typ == NameTy.BOOL){
        return true;
      }
    } else if (decType == "array"){
      ArrayDec temp = (ArrayDec) def;
      if (temp.typ.typ == NameTy.BOOL){
        return true;
      }
    }
    return false;
  }

  public Boolean isVoid(Dec def, String decType){
    if (decType == "simple"){
      SimpleDec temp = (SimpleDec) def;
      if (temp.typ.typ == NameTy.VOID){
        return true;
      }
    } else if (decType == "func"){
      FunctionDec temp = (FunctionDec) def;
      if (temp.result.typ == NameTy.VOID){
        return true;
      }
    } else if (decType == "array"){
      ArrayDec temp = (ArrayDec) def;
      if (temp.typ.typ == NameTy.VOID){
        return true;
      }
    }
    return false;
  }
/* 
  public String messageFuncDec( FunctionDec temp){
      int resultType = temp.result.typ;
      if (temp.params!=null){
        String paramTypes = "";
        VarDec
        for (VarDec p : temp.params.head){
          String decType = getDecType(p);
          if (decType == "simple"){
            SimpleDec temp2 = (SimpleDec) p;
            if (isInteger(temp2, decType)){
              paramTypes+="int";
            } else if  (isBoolean(temp2, decType)){
              paramTypes+="bool";
            } else if (isVoid(temp2, decType)){
              paramTypes+="void";
            }
          } else if (decType == "array"){
            ArrayDec temp2 = (ArrayDec) p;
            if (isInteger(temp2, decType)){
              paramTypes+="int";
            } else if  (isBoolean(temp2, decType)){
              paramTypes+="bool";
            } else if (isVoid(temp2, decType)){
              paramTypes+="void";
            }
          }
          paramTypes+=",";
        }
        paramTypes = paramTypes.substring(0, paramTypes.length-1);
        System.out.println(n.name+ ": (" + resultType+") -> "+paramTypes);
      } else {
        System.out.println(n.name+ ": "+ resultType);
      }
  }
*/
  public String getDecType(Dec temp){
    SimpleDec simp = new SimpleDec(0, 0, null, null);
    ArrayDec arr = new ArrayDec(0, 0, null, null, 0);
    FunctionDec fun = new FunctionDec(0, 0, null, null, null, null);
    if (temp.getClass()== simp.getClass()){
      return "simple";
    } else if (temp.getClass() == fun.getClass()) {
      return "func";
    } else if (temp.getClass() == arr.getClass()){
      return "array";
    }
    return null;
  }

  public void printArrayList(ArrayList<NodeType> list, int l){
    for (NodeType n : list){
      String decType = getDecType(n.def);
      indent(l+1);
      String type;
      if (isInteger(n.def, decType)){
        type = "int";
      } else if (isBoolean(n.def, decType)){
        type = "bool";
      } else {
        if (decType == "simple" || decType == "array"){
          System.out.println("Error, variable cannot be void, changed to int.");
          type = "int";
        } else {
          type = "void";
        }
      }
      if (decType == "simple"){
        System.out.println(n.name+ ": "+ type);
      }  else if (decType == "func"){
        System.out.println(n.name+ ": ("+ type+")");
      } else if (decType == "array"){
        System.out.println(n.name+ ": "+ type + "*");
      }
    }
  }

  public void printSymTable(){
    System.out.println(table+ " "+level);
    for(String i: table.keySet()){
      ArrayList<NodeType> list = table.get(i);
      printArrayList(list, Integer.parseInt(i));
    }
  }

  public void printGlobal(){
    if (table.containsKey(Integer.toString(0))){
      ArrayList<NodeType> list = table.get(Integer.toString(0));
      printArrayList(list, 0);
    }
  }

  private void indent( int level ) {
    for( int i = 0; i < level * SPACES; i++ ) System.out.print( " " );
  }

  public void visit( ExpList expList, int level ) {
    while( expList != null ) {
        expList.head.accept( this, level );
        expList = expList.tail;
      } 
  }

  public void visit( DecList decList, int level ) {
    while( decList != null ) {
        decList.head.accept( this, level );
        decList = decList.tail;
      } 
  }

  public void visit( VarDecList varDecList, int level ) {
    while( varDecList != null ) {
        varDecList.head.accept( this, level );
        varDecList = varDecList.tail;
      } 
  }
  
  public void visit( AssignExp exp, int level ) {
    int index = lookupAll(exp.lhs.name, level);
    String lhsDecType;
    String lhsType;
    if (index != -1){
      ArrayList<NodeType> list = table.get(Integer.toString(index));
      NodeType n = findNodeType(exp.lhs.name, list);
      lhsType = getDecType(n.def);
      if (isBoolean(n.def, lhsType)){
        lhsType = "class absyn.BoolExp";
      } else if (isVoid(n.def, lhsType)){
        lhsType = "void";
      } else {
        lhsType = "class absyn.IntExp";
      }
      String rhsType = exp.rhs.getClass().toString();
      if(rhsType.equals(lhsType)){
        System.out.println("here");
        exp.dtype = n.def;
      } else {
        System.out.println("Type mismatch for variable "+n.name +" changing to "+rhsType);
        if (rhsType.equals("class absyn.IntExp")){
          SimpleDec temp = new SimpleDec(0,0, new NameTy(0,0,NameTy.INT), null);
          deleteSpecific(Integer.toString(index), n.name);
          insert(Integer.toString(index), temp, n.level, n.name);
        }
      }
      exp.lhs.accept( this, level );
      exp.rhs.accept( this, level );
    } else {
      System.out.println("Error, variable undefined.");
    } 
  }

  public void visit( IfExp exp, int level ) {
    level++;
    indent(level);
    System.out.println("Entering the scope for an if block:");
    if (exp.test != null){
      exp.test.accept( this, level );
      exp.thenpart.accept( this, level );
      ArrayList<NodeType> list = table.get(Integer.toString(level));
      printArrayList(list, level);
      indent(level);
      System.out.println("Leaving the if block");
      delete(Integer.toString(level));
      NilExp i = new NilExp(0, 0);
      if (exp.elsepart.getClass() != i.getClass()){
        indent(level);
        System.out.println("Entering the scope for an else block:");
        exp.elsepart.accept( this, level );
        printArrayList(list, level);
        indent(level);
        System.out.println("Leaving the else block");
        level--;
      }
    }
  }

  public void visit( IntExp exp, int level ) {
    exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.INT), null);
  }

  public void visit( BoolExp exp, int level ) {
    exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.BOOL), null);
}

  public void visit( VarExp exp, int level ) {
    exp.name.accept( this, ++level );
  }

  public void visit( IndexVar exp, int level ) {
    if (exp.index!=null)
      exp.index.accept( this, ++level );
  }

  public void visit( CallExp exp, int level ) {
    if (exp.args!=null)
      exp.args.accept( this, ++level );
  }

  public void visit( WhileExp exp, int level ){
    level++;
    indent(level);
    System.out.println("Entering the scope of a while block:");
    if (exp.test!=null)
      exp.test.accept( this, level );
    if (exp.body!=null)
      exp.body.accept( this, level );
    ArrayList<NodeType> list = table.get(Integer.toString(level));
    printArrayList(list, level);
    indent(level);
    System.out.println("Leaving the while block");
    level--;
  }

  public void visit( CompoundExp exp, int level ){
    if (exp.decs!=null){
      exp.decs.accept( this, level );
    }
    if (exp.exps!= null){
      exp.exps.accept( this, level );
    }
  }

  public void visit( SimpleVar exp, int level ){

}

  public void visit( NilExp exp, int level ){

  }

  public void visit( FunctionDec exp, int level ){
    if (lookup(exp.func, level) == false){
      insert(Integer.toString(level), exp, level, exp.func);
      exp.result.accept( this, level );
      level++;
      indent(level);
      System.out.println("Entering the scope for function "+exp.func);
      if ( exp.params != null)
        exp.params.accept( this, level );
      if (exp.body.getClass().toString() != "NilExp")
        exp.body.accept( this, level );
      ArrayList<NodeType> list = table.get(Integer.toString(level));
      printArrayList(list, level);
      indent(level);
      System.out.println("Leaving the scope for function "+exp.func);
      delete(Integer.toString(level));
      level--;
    } else {
      System.out.println("Error, function of this name is already declared in this scope");
    }
  }

  public void visit( SimpleDec exp, int level ){
    if (lookup(exp.name, level) == false){
      insert(Integer.toString(level), exp, level, exp.name);
    } else {
      System.out.println("Error, variable of this name already exists in this scope.");
    }
    //printSymTable();
    //exp.typ.accept( this, ++level );
  }

  public void visit( ArrayDec exp, int level ){
    if (lookup(exp.name, level) == false){
      insert(Integer.toString(level), exp, level, exp.name);
    } else {
      System.out.println("Error, variable of this name already exists in this scope.");
    }
    //printSymTable();
    //exp.typ.accept( this, ++level );
  }

  public void visit( ReturnExp retExp, int level ){
    retExp.exp.accept( this, ++level );
  }


  public void visit (NameTy exp, int level ){
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
  
  public void visit( OpExp exp, int level ) {
    switch( exp.op ) {
      case OpExp.PLUS:
        //System.out.println( " + " );
        break;
      case OpExp.MINUS:
        //System.out.println( " - " );
        break;
      case OpExp.TIMES:
        //System.out.println( " * " );
        break;
      case OpExp.OVER:
        //System.out.println( " / " );
        break;
      case OpExp.EQ:
        //System.out.println( " = " );
        break;
      case OpExp.LT:
        //System.out.println( " < " );
        break;
      case OpExp.GT:
        //System.out.println( " > " );
        break;
      case OpExp.UMINUS:
        //System.out.println( " - " );
        break;
      case OpExp.NEQ:
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
        //System.out.println( " >= ");
        break;
      case OpExp.LTE:
        //System.out.println( " <= ");
        break;
      default:
        //System.out.println( "Unrecognized operator at line " + exp.row + " and column " + exp.col);
    }
    level++;
    if (exp.left.getClass().toString() != "NilExp")
       exp.left.accept( this, level );
    if (exp.right != null)
       exp.right.accept( this, level );
  }

  public void visit( NodeType exp, int level ) {

  }
}