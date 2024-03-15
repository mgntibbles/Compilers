/*
  Created by: Fei Song
  File Name: Main.java
  To Build: 
  After the Scanner.java, tiny.flex, and tiny.cup have been processed, do:
    javac Main.java
  
  To Run: 
    java -classpath /usr/share/java/cup.jar:. Main gcd.tiny

  where gcd.tiny is an test input file for the tiny language.
*/
   
import java.io.*;
import absyn.*;
   
class Main {
  public static boolean SHOW_TREE = true;
  public static boolean SHOW_TABLE = true;
  static public void main(String argv[]) { 
    String output = " ";
    for (int i = 0; i < argv.length; i++){
      if (argv[i].equals("-a")){
        output = argv[i+1];
        SHOW_TREE = false;
        //System.setOut(new FileOutputStream(output));
      }
      if (argv[i].equals("-s")){
        output = argv[i+1];
        SHOW_TREE = false;
        //System.setOut(new FileOutputStream(output));
      }
    } 
    System.out.println(output);
    /* Start the parser */
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value);      
      if (SHOW_TREE && result != null) {
         System.out.println("The abstract syntax tree is:");
         AbsynVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0); 
      }
      if (SHOW_TABLE && result != null){
        System.out.println("The symbol table is:");
        SemanticAnalyzer visitor2 = new SemanticAnalyzer();
        System.out.println("Entering the global Scope: ");
        result.accept(visitor2, 0); 
        visitor2.printGlobal();
        System.out.println("Leaving the global scope");
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
