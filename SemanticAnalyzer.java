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

  public void setup(){

    insert(Integer.toString(0), new FunctionDec(0,0, new NameTy(0,0,NameTy.INT), "input", null, null), 0, "input");
    SimpleDec simp = new SimpleDec(0,0,new NameTy(0,0,NameTy.INT), null);
    VarDecList list = new VarDecList(simp, null);
    insert(Integer.toString(0), new FunctionDec(0,0, new NameTy(0,0,NameTy.VOID), "output", list, null), 0, "output");
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
      if(Integer.parseInt(i)<=level){
        if(lookup(key, Integer.parseInt(i))){
          return Integer.parseInt(i);
        }
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

  public int findType(Dec def, String decType){
    if (isBoolean(def, decType)){
      return NameTy.BOOL;
    } else if (isInteger(def, decType)){
      return NameTy.INT;
    } else {
      return NameTy.VOID;
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
    } 
    else if (decType == "func"){
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

  public String getDecType(Dec temp){
    if (temp!=null){
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
    }
    return null;
  }

  public void printArrayList(ArrayList<NodeType> list, int l){
    if (list != null){
      for (NodeType n : list){
        String decType = getDecType(n.def);
        indent(l+1);
        String type;
        if (isInteger(n.def, decType)){
          type = "int";
        } else if (isBoolean(n.def, decType)){
          type = "bool";
        } else {
          type = "void";
        }
        if (decType == "simple"){
          System.out.println(n.name+ ": "+ type);
        }  else if (decType == "func"){
          FunctionDec temp = (FunctionDec)n.def;
          VarDecList decList = temp.params;
          ArrayList<Integer> paramList = getParamTypes(decList);
          System.out.print(n.name+ ": (");
          if (paramList.size() !=0){
            for (int i : paramList){
              if (i == NameTy.INT){
                System.out.print("int,");
              } else if (i == NameTy.BOOL){
                System.out.print("bool,");
              } else if (i == NameTy.VOID){
                System.out.print("void,");
              }
            }
          }
          System.out.println(") -> "+type);
        } else if (decType == "array"){
          System.out.println(n.name+ ": "+ type + "*");
        }
      }
    }
  }

  public ArrayList<Integer> getParamTypes(VarDecList list){
    ArrayList<Integer> types = new ArrayList<Integer>();
    while (list!=null){
      String decType = getDecType(list.head);
      int type = findType(list.head, decType);
      types.add(type);
      list = list.tail;
    }
    return types;
  }

  public ArrayList<Integer> getArgTypes(ExpList list){
    ArrayList<Integer> types = new ArrayList<Integer>();
    while (list!=null){
      String decType = getDecType(list.head.dtype);
      int type = findType(list.head.dtype, decType);
      types.add(type);
      list = list.tail;
    }
    return types;
  }

  public void printSymTable(){
    System.out.println(table+ " "+level);
    for(String i: table.keySet()){
      ArrayList<NodeType> list = table.get(i);
      printArrayList(list, Integer.parseInt(i));
    }
  }

  public void printGlobal(){
    deleteSpecific("0", "input");
    deleteSpecific("0", "output");
    if (table.containsKey(Integer.toString(0))){
      ArrayList<NodeType> list = table.get(Integer.toString(0));
      printArrayList(list, 0);
      NodeType n = list.get(list.size()-1);
      if (!n.name.equals("main")){
        System.out.println("Main function is missing or not the final function.");
      }
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
    if (index != -1){
      exp.lhs.accept( this, level );
      exp.rhs.accept( this, level );
      ArrayList<NodeType> list = table.get(Integer.toString(index));
      NodeType n = findNodeType(exp.lhs.name, list);
      int lhsType = findType(n.def, getDecType(n.def));
      int rhsType = findType(exp.rhs.dtype, getDecType(exp.rhs.dtype)); 
      if (lhsType == rhsType){
        exp.dtype = n.def;
      } else {
        System.out.println("Error, type mismatch for variable "+n.name +", changing "+n.name+ " type to match. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
        deleteSpecific(Integer.toString(index), n.name);
        if (rhsType==NameTy.INT){
          SimpleDec temp = new SimpleDec(0,0, new NameTy(0,0,NameTy.INT), null);
          insert(Integer.toString(index), temp, n.level, n.name);
        } else if (rhsType == NameTy.BOOL){
          SimpleDec temp = new SimpleDec(0,0, new NameTy(0,0,NameTy.BOOL), null);
          insert(Integer.toString(index), temp, n.level, n.name);
        } else {
          SimpleDec temp = new SimpleDec(0,0, new NameTy(0,0,NameTy.VOID), null);
          insert(Integer.toString(index), temp, n.level, n.name);
        }
        ArrayList<NodeType> list2 = table.get(Integer.toString(index));
        printArrayList(list2, index);
      }
    } else {
      SimpleDec newExp = new SimpleDec(exp.lhs.row, exp.lhs.col, new NameTy(exp.row, exp.col, NameTy.INT), exp.lhs.name);
      exp.dtype = newExp;
      insert(Integer.toString(level), newExp, index, exp.lhs.name);
      System.out.println("Error, variable undefined. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    } 
  }

  public void visit( IfExp exp, int level ) {
    level++;
    indent(level);
    System.out.println("Entering the scope for an if block:");
    if (exp.test != null){
      exp.test.accept( this, level );
      String testDecType = getDecType(exp.test.dtype);
      int testType = findType(exp.test.dtype, testDecType);
      if (testType == NameTy.BOOL){
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
      } else {
        System.out.println("Error, test condition is not Boolean. Row "+(exp.row+1)+" col "+(exp.col+1));
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
    int index = lookupAll(exp.name.name, level);
    exp.name.accept( this, ++level );
    if (index != -1){
      ArrayList<NodeType> list = table.get(Integer.toString(index));
      NodeType n = findNodeType(exp.name.name, list);
      String type = getDecType(n.def);
      if (isInteger(n.def, type)){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.INT), null);
      } else if (isBoolean(n.def, type)){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.BOOL), null);
      } else {
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.VOID), null);
      }
    } else {
      SimpleDec newExp = new SimpleDec(exp.row, exp.col, new NameTy(exp.row, exp.col, NameTy.INT), exp.name.name);
      exp.dtype = newExp;
      insert(Integer.toString(index), newExp, index, exp.name.name);
      System.out.println("Error, variable is undeclared. Adding declaration as int. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    }
  }

  public void visit( IndexVar exp, int level ) {
    if (exp.index!=null)
      exp.index.accept( this, ++level );
      String type = getDecType(exp.index.dtype);
      if (isBoolean(exp.index.dtype, type) || isVoid(exp.index.dtype, type)){
        System.out.println("Error, index is not Integer. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
      }
  }

  public void visit( CallExp exp, int level ) {
    int index = lookupAll(exp.func, level);
    if (index!=-1){
      ArrayList<NodeType> list = table.get(Integer.toString(index));
      NodeType n = findNodeType(exp.func, list);
      String type = getDecType(n.def);
      if (isInteger(n.def, type)){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.INT), null);
      } else if (isBoolean(n.def, type)){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.BOOL), null);
      } else {
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0,NameTy.VOID), null);
      }
      if (exp.args!=null){
        exp.args.accept( this, ++level );
        FunctionDec temp = (FunctionDec) n.def;
        VarDecList decList = temp.params;
        ArrayList<Integer> paramList = getParamTypes(decList);
        ArrayList<Integer> argList = getArgTypes(exp.args);
        if (!paramList.equals(argList)){
          System.out.println("Error, arguments do not match function declaration. Row "+(exp.row+1)+", col "+(exp.col+1));
        }
      }
    } else {
      System.out.println("Error, function is undeclared. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    }
      
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
    delete(Integer.toString(level));
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
    //check if the function has been declared before
    int index = lookupAll(exp.func, level);
    //if not, add it
    if (index==-1){
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
    //if already declared - check if it was just the prototype (if there was no body)
    } else {
      ArrayList<NodeType> list1 = table.get(Integer.toString(index));
      NodeType n = findNodeType(exp.func, list1);
      FunctionDec temp = (FunctionDec)n.def;
      if (temp.body.getClass().toString().equals("class absyn.NilExp")){
        deleteSpecific(Integer.toString(level), exp.func);
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

      //if there was a body, function is being redefined
      } else {
        System.out.println("Error, function of this name is already declared in this scope. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
      }
    }
  }

  public void visit( SimpleDec exp, int level ){
    if (lookup(exp.name, level) == false){
      int type = findType(exp, "simple");
      if (type == NameTy.VOID){
        System.out.println("Error, variable "+ exp.name+" declared as void, changed to int. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
        exp = new SimpleDec(exp.row, exp.col, new NameTy(0,0,NameTy.INT), exp.name);
      }
      insert(Integer.toString(level), exp, level, exp.name);
    } else {
      System.out.println("Error, variable "+ exp.name+" of this name already exists in this scope. Not added. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    }
    //printSymTable();
    //exp.typ.accept( this, ++level );
  }

  public void visit( ArrayDec exp, int level ){
    if (lookup(exp.name, level) == false){
      int type = findType(exp, "array");
      if (type == NameTy.VOID){
        System.out.println("Error, array "+ exp.name+" declared as void, changed to int. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
        exp = new ArrayDec(exp.row, exp.col, new NameTy(0,0,NameTy.INT), exp.name, exp.size);
      }
      insert(Integer.toString(level), exp, level, exp.name);
    } else {
      System.out.println("Error, variable "+ exp.name+" of this name already exists in this scope. Not added. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    }
    //printSymTable();
    //exp.typ.accept( this, ++level );
  }

  public void visit( ReturnExp retExp, int level ){
    retExp.exp.accept( this, ++level );
    ArrayList<NodeType> list = table.get(Integer.toString(level-2));
    NodeType n = list.get(list.size()-1);
    String decType = getDecType(n.def);
    int returnType = findType(n.def, decType);
    decType = getDecType(retExp.exp.dtype);
    int type = findType(retExp.exp.dtype, decType);
    if (returnType == type){
      retExp.dtype = new SimpleDec(0,0, new NameTy(0,0, type), null);
    } else {
      System.out.println("Error, incompatible return type, Row: " + (retExp.row+1) + ", col: "+ (retExp.col+1));
    }
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
    level++;
    int rhs = -1;
    int lhs = -1;
    if (exp.right != null)
      exp.right.accept( this, level );
      String rhsDecType = getDecType(exp.right.dtype);
      rhs = findType(exp.right.dtype, rhsDecType);
    if (exp.left.getClass().toString() != "NilExp")
      exp.left.accept( this, level );
      String lhsDecType = getDecType(exp.left.dtype);
      lhs = findType(exp.left.dtype, lhsDecType);
    if (lhs == rhs){
      if (exp.op == OpExp.PLUS || exp.op == OpExp.MINUS || exp.op == OpExp.TIMES || exp.op == OpExp.OVER){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0, lhs), null);
      } 
      if (exp.op == OpExp.EQ || exp.op == OpExp.LT || exp.op == OpExp.GT || exp.op == OpExp.NEQ || exp.op == OpExp.NOT || exp.op == OpExp.AND || exp.op == OpExp.OR || exp.op == OpExp.GTE || exp.op == OpExp.LTE){
        exp.dtype = new SimpleDec(0,0, new NameTy(0,0, NameTy.BOOL), null);
      }
    } else if (lhs == -1 && rhs!=-1){
      exp.dtype = new SimpleDec(0,0, new NameTy(0,0, lhs), null);
    } else {
      System.out.println("Error, incompatible data types. Row: " + (exp.row+1) + ", col: "+ (exp.col+1));
    }

  }

  public void visit( NodeType exp, int level ) {

  }
}