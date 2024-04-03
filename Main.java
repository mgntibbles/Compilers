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
  public static boolean SHOW_TREE = false;
  public static boolean SHOW_TABLE = false;
  static public void main(String argv[]) { 
    String output = " ";
    for (int i = 0; i < argv.length; i++){
      if (argv[i].equals("-a")){
        if (i+1 < argv.length){
          output = argv[i+1];
          SHOW_TABLE = false;
          SHOW_TREE = true;
          try{
            File absFile = new File(output);
            System.setOut(new PrintStream(absFile));
          } catch(Exception e){
            System.out.println("could not create file");
          }
        }
      }
      if (argv[i].equals("-s")){
        if (i+1 < argv.length){
          output = argv[i+1];
          SHOW_TREE = false;
          SHOW_TABLE = true;
          try{
            File symFile = new File(output);
            System.setOut(new PrintStream(symFile));
          } catch(Exception e){
            System.out.println("could not create file");
          }
        }
      }
      if (argv[i].equals("-c")){
        output = argv[0];
        int ind = output.lastIndexOf(".");
        output = output.substring(0,ind) + ".tm";
        System.out.println(output);
        SHOW_TREE = false;
        SHOW_TABLE = false;
        //this is commented for now because I need to do semantic analyzer first and direct to null
        //try{
        //  File tmFile = new File(output);
        //  System.setOut(new PrintStream(tmFile));
        //} catch(Exception e){
        //  System.out.println("could not create file");
       // }
      }
    } 
    /* Start the parser */
    try {
      parser p = new parser(new Lexer(new FileReader(argv[0])));
      Absyn result = (Absyn)(p.parse().value); 
      Boolean syntaxCheck = p.return_check();   
      if (SHOW_TREE && result != null) {
         System.out.println("The abstract syntax tree is:");
         AbsynVisitor visitor = new ShowTreeVisitor();
         result.accept(visitor, 0, false); 
      }
      if (SHOW_TABLE && result != null && syntaxCheck){
        System.out.println("The symbol table is:");
        SemanticAnalyzer visitor2 = new SemanticAnalyzer();
        System.out.println("Entering the global Scope: ");
        visitor2.setup();
        result.accept(visitor2, 0, false); 
        visitor2.printGlobal();
        System.out.println("Leaving the global scope");
      }
      if (!SHOW_TABLE && !SHOW_TREE){
        PrintStream outNull = System.out;
        System.setOut(new PrintStream(new FileOutputStream("/dev/null")));
        SemanticAnalyzer visitor2 = new SemanticAnalyzer();
        CodeGenerator visitor3 = new CodeGenerator();
        visitor2.setup();
        result.accept(visitor2, 0, false); 
        try{
          File tmFile = new File(output);
          System.setOut(new PrintStream(tmFile));
        } catch(Exception e){
          System.out.println("could not create file");
        }
        visitor3.visit(result);
        //result.accept(visitor3, 0, false);
      }
    } catch (Exception e) {
      /* do cleanup here -- possibly rethrow e */
      e.printStackTrace();
    }
  }
}
