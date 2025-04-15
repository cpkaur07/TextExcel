//Charanpreet Kaur
//Period 4
//Text Excel

/*
* In Checkpoint #1: this class is ignored.
* In Checkpoint #2: the NumberCell will hold a number only.
*    Example:  a1 = 5
* In Checkpoint #3: the NumberCell may hold an expression.
*    Example:  a1 = ( 2 + a2 / a3 * a4 )
* In Final Submission: the NumberCell may hold functions.
*    Example:  a1 = ( sum b1 - b5 )
*/

import java.util.*;
public class NumberCell extends Cell {
  
  /*
   * This will return the number for this cell.
   */

    public double parseExpression(String value) {
      if(Character.isLetter(value.charAt(0))) {
        String cellValue = GridBase.grid.processCommand("value " + value);
        if(cellValue == null || cellValue.isEmpty()) {
          return 0.0;
        }
        return Double.parseDouble(cellValue);
      }
      else {
        return Double.parseDouble(value);
      }
    }
    
    public double operations(double num, double answer, String operator) {
      if(operator.equals("-")) {
        return answer - num;
      }
      else if(operator.equals("+")) {
        return answer + num;
      }
      else if(operator.equals("*")) {
        return answer * num;
      }
      else {
        return answer / num;
      }
    }
    
    public double getValue() {
      if(getExpression().contains("sum") || getExpression().contains("avg")) {
        return Double.parseDouble(GridBase.grid.processCommand(getExpression()));
      }
      String express = getExpression();
      double value;
      while (express.contains("(")) {
        int close = express.indexOf(")");
        int open = express.lastIndexOf("(", close);
        String inner = express.substring(open+1, close);
        value = evaluateExpression(inner);
        express = express.substring(0, open) + value + express.substring(close+1);
      }
      return evaluateExpression(express);
    }
    
    public double evaluateExpression(String express) {
      String expr = express.replace("(","").replace(")","");
      String[] separated = GridBase.smartSplit(expr);
      ArrayList<String> exp = new ArrayList<>();
      for (int i = 0 ; i < separated.length ; i++) {
        exp.add(separated[i]);
      }
      for (int i = 0 ; i < exp.size() ; ) {
        if(exp.get(i).equals("*") || exp.get(i).equals("/")) {
          double n1 = parseExpression(exp.get(i-1));
          double n2 = parseExpression(exp.get(i+1));
          String operator = exp.get(i);
          exp.set(i-1, String.valueOf(operations(n2, n1, operator)));
          exp.remove(i);
          exp.remove(i);
        }
        else {
          i++;
        }
      }
      String[] sep = new String[exp.size()];
      for (int i=0 ; i < exp.size() ; i++) {
        sep[i] = exp.get(i);
      }
      double answer = parseExpression(sep[0]);
      for(int i = 1; i < sep.length; i+=2) {
        String operator = sep[i];
        double num = parseExpression(sep[i+1]);
        answer = operations(num, answer, operator);
      }
      return answer;
    }
    
    public String toString() {
      return getValue() + "";
    }
}
 
 