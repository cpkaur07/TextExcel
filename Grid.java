// Charanpreet Kaur
// Period 4
// Text Excel Project


//Current issues with isCellAddress or conditions checking cell validity
//Problem with numberCell class and empty string being provided in methods that are printing: mainly truncate
//SAVE IN A DOC BEFORE CHANGES!




import java.io.*;
import java.util.*;








/*
* The Grid class will hold all the cells. It allows access to the cells via the
* public methods. It will create a display String for the whole grid and process
* many commands that update the cells. These command will include
* sorting a range of cells and saving the grid contents to a file.
*
*/
public class Grid extends GridBase {








 // These are called instance fields.
 // They are scoped to the instance of this Grid object.
 // Use them to keep track of the count of columns, rows and cell width.
 // They are initialized to the prescribed default values.
 // Notice that there is no "static" here.
 private int colCount = 7;
 private int rowCount = 10;
 private int cellWidth = 9;
 private Cell[][] matrix;








 public Grid() {
     createMatrix();
 }


 public void createMatrix() {
     matrix = new Cell[rowCount][colCount];
     for (int row=0 ; row<rowCount ; row++) {
         for (int col=0 ; col<colCount ; col++) {
             matrix[row][col] = new Cell();
         }
     }
 }


 public int[] cellAddress(String address) {
   if (address == null || address.length() < 2)
       return null;
  
   char colN = (address.trim().toUpperCase().charAt(0));
   String rowN = address.trim().substring(1);
   if (colN < 'A' || colN > 'Z')
       return null;
  
   int rowNum;
   try {
       rowNum = Integer.parseInt(rowN) - 1;
   }
   catch (NumberFormatException e) {
       return null;
   }
   int colNum = colN - 'A';
   if (rowNum < 0 || rowNum >= getRows())
       return null;
  
   if (colNum < 0 || colNum >= getCols())
       return null;
  
   int[] cellAddress = {rowNum, colNum};
   return cellAddress;
 }


 public int getRows() {
     return this.rowCount;
 }


 public void setRows (int input) {
     this.rowCount = input;
 }


 public int getCols() {
     return this.colCount;}
  public void setCols (int input) {
     colCount = input;
 }


 public int getWidth() {
     return this.cellWidth;
 }


  public void setWidth (int input) {
      cellWidth = input;
  }


 public String truncate(Cell cell) {
  String truncated = String.valueOf(cell);
  //int row = Integer.parseInt(address.substring(1).trim());
  if (truncated.length() > getWidth()) {
      truncated = truncated.substring(0, getWidth());
  }
  else {
      int spaces = cellWidth - truncated.length();
      for(int i=1 ; i<=spaces ; i++) {
          truncated =  " " + truncated;
      }
  }
  return truncated;
 }


 public int getColValue(String address) {
     char c = address.charAt(0);
     int colNumber = c;
     colNumber = c-65;
     return colNumber;
 }


 public int getRowValue(String address) {
       int rowNumber = Integer.parseInt(address.substring(1))-1;
       return rowNumber;
 }


 public String renderMatrix() {
     String displayGrid;
     displayGrid = printColHeading();
     displayGrid = displayGrid + separator();
     int r = 0;
     for (int row = 0, col=0 ; row < getRows() ; row++, col++) {
       //for (int col = 0 ; col < getCols()-1 ; col++){
           displayGrid = displayGrid + printRow(r+1, truncate(matrix[row][col]));
           r++;
           displayGrid = displayGrid + separator();
       //}
   }
     return displayGrid;
 }


 public String printColHeading() {
     String space = " ";
     String colHeader;
     colHeader = String.format("%4s|", space);
     for (int i=1 ; i<=colCount ; i++) {
         char a = (char) (i+64);
         String alphabet = "" + a;
         if (cellWidth%2 != 0)
         colHeader = colHeader + String.format("%" + cellWidth/2 + "s%s%" + cellWidth/2 + "s|", space, alphabet, space);
         else
         colHeader = colHeader + String.format("%" + cellWidth/2 + "s%s%" + ((cellWidth/2)-1) + "s|", space, alphabet, space);
     }
     colHeader = colHeader + "\n";
     return colHeader;
 }


 public String separator() {
     String separation;
     String hyphen = "-";
     separation = String.format("%4s+", hyphen);
     for (int i=1 ; i<=colCount ; i++) {
         separation = separation +String.format("%" + cellWidth + "s+", hyphen);
     }
     separation = separation.replace(" ", "-");
     separation = separation +"\n";
     return separation;
 }
  public String printRow(int row, String truncated) {
     String space = " ";
     String displayRow;
  
     displayRow = String.format("%3d%s|", row, space);
     for (int i = 0 ; i < getCols() ; i++) {
         //displayRow = displayRow + String.format("%" + cellWidth + "s|", space);
         displayRow = displayRow + String.format(truncated + "|");
     }
     displayRow = displayRow +"\n";
     return displayRow;/*
     System.out.print("|" + center(row + 1 + "", cellWidth));
     for (int col = 0; col < colCount; col++)
     {
         String value = (matrix[row][col] == null) ? "" : matrix[row][col].toString();
         System.out.print("|" + center(value, cellWidth));
     }
     System.out.println("|");*/
 }




 //Checkpoint 2
 public boolean isAddress(String address) {
     address.trim().toUpperCase();
     //String address = command.substring(0, command.indexOf(" ")).trim().toUpperCase();
     if(address == null || address.length() <= 1 || address.length() > 5 ) {
         return false;
     }
     else if(address.charAt(0) < 'A' || address.charAt(0) > 'Z') {
         return false;
     }
     else {
         try {
             int rowNumber = Integer.parseInt(address.substring(1));
             if (rowNumber <= 0 || rowNumber >= rowCount+1) {
               return false;
             }
             else {
               return true;
             }
         } catch (NumberFormatException ex) {
             return false;
         }
     }
 }


 public void setCellValue(String address, String cellValue) {
   if (isAddress(address)) {
       TextCell textCell;
       NumberCell numberCell;
       if (cellValue.contains("\"")) {
           textCell = new TextCell(cellValue);
           matrix[getRowValue(address)][getColValue(address)] = textCell;
       }
       else {
           numberCell = new NumberCell(cellValue);
           matrix[getRowValue(address)][getColValue(address)] = numberCell;
       }
   }
   else {
       cellValue = null;
   }
}


 public String expression(String address) {
     try{
         if (isAddress(address)) {
         int row = getRowValue(address);
         int col = getColValue(address);
         if (matrix[row][col] != null) {
             return matrix[row][col].getExpression();
       }
       return "";
     }
     else {
         return "address " + address;
     }
 }
 catch (Exception ex) {
     return "invalid address " + address;
 }
 }


 public String display(String address) {
     try {
         Cell cell = matrix[getRowValue(address)][getColValue(address)];
     String value;
     if (cell != null) {
         value = String.valueOf(cell);
     }
     else {
         value = "<empty>";
     }
     return address + " = " + value;
 }
 catch (NumberFormatException ex) {
     return "invalid address " + address;
 }
 }


 public String toString(Cell cellValue) {
     return "" + cellValue;
 }


 public void clear() {
     for (int row=0 ; row<=rowCount ; row++) {
         for (int col=0 ; col<colCount ; col++) {
             matrix[row][col] = null;
         }
     }
 }




 /*
  * This method processes a user command.
  *
  * Checkpoint #1 commands are: print : render a text based version of the matrix
  * width = [value] : set the cell width width : get the cell width rows =
  * [value] : set the row count cols = [value] : set the column count rows : get
  * the row count cols : get the column count
  *
  *
  * Checkpoint #2 commands are: [cell] = [expression] : set the cell's
  * expression, for checkpoint # expressions may be... - a value such as 5.
  * Example: a2 = 5 - a string such as "hello". Example: a3 = "hello" [cell] :
  * get the cell's expression, NOT the cell's value value [cell] : get the cell
  * value expr [cell] : get the cell's expression, NOT the cell's value display
  * [cell] : get the string for how the cell wants to display itself clear :
  * empty out the entire matrix save [file] : saves to a file all the commands
  * necessary to regenerate the grid's contents
  *
  * Checkpoint #3 commands are: [cell] = [expression] : where the expression is a
  * complicated formula. Example: a1 = ( 3.141 * b3 + b1 - c2 / 4 )
  *
  * Final commands are: [cell] = [expression] : where the expression may contain
  * a single function, sum or avg: Example: a1 = ( sum a1 - a3 ) Example: b1 = (
  * avg a1 - d1 ) clear [cell] : empty out a single cell. Example: clear a1 sorta
  * [range] : sort the range in ascending order. Example: sorta a1 - a5 sortd
  * [range] : sort the range in descending order. Example: sortd b1 - e1
  *
  *
  *
  * Parameters: command : The command to be processed. Returns : The results of
  * the command as a string to be printed by the infrastructure.
  */
  public String processCommand(String command) {
     /*String result = null;
     result = command;
     String address = result.substring(0, result.indexOf(" ")).trim().toUpperCase();
     String cellValue = result.substring(result.indexOf("=")+1).trim();
     //if (result == null) {
     //    result = "unknown or malformed command: " + command;
     //}
     //else
     String finalize;
     if (result.contains("rows")) {
         if (result.startsWith("rows =")) {
             int rows = Integer.parseInt(result.substring(result.indexOf("=")+1).trim());
             setRows(rows);
             finalize = "" + rows;
             return finalize;
         }
         else {
             finalize = "" + getRows();
             return finalize;
         }
     }
     else if (result.contains("cols")) {
         if (result.startsWith("cols =")) {
             int cols = Integer.parseInt(result.substring(result.indexOf("=")+1).trim());
             setCols(cols);
             finalize = "" + cols;
             return finalize;
         }
         else {
             finalize = "" + getCols();
             return finalize;
         }
     }
     else if (result.contains("width")) {
         if (result.startsWith("width =")) {
             int width = Integer.parseInt(result.substring(result.indexOf("=")+1).trim());
             setWidth(width);
             finalize = "" + width;
             return finalize;
         }
         else {
             finalize = "" + getWidth();
             return finalize;
         }
     }
     else if(result.contains("value")) {
         //System.out.println(value(command.substring(0, command.indexOf(" ")).trim().toUpperCase()).toString());
         return String.valueOf(value(address));
     }
     else if (result.contains("display")) {
         return display(address);
     }
     else if (result.contains("expr")) {
         return expression(address);
     }
     else if (command.equalsIgnoreCase("print")){
         return renderMatrix();
     }
     else {
     return "unknown or malformed command " + command;
     }*/
     /*command = command.trim();
     if (command.equalsIgnoreCase("print")) {
         return renderMatrix();
     } else if (command.equalsIgnoreCase("clear")) {
         clear();
         return "";
     } else if (command.contains("=")) {
         setCellValue(command);
         return "";
     } else if (command.startsWith("value ")) {
         return String.valueOf(value(command.substring(6).trim().toUpperCase()));
     } else if (command.startsWith("display ")) {
         return display(command.substring(8).trim().toUpperCase());
     } else if (command.startsWith("expr ")) {
         return expression(command.substring(5).trim().toUpperCase());
     } else if (isAddress(command)) {
         return expression(command.trim().toUpperCase());
     } else if (command.startsWith("rows")) {
         if (command.startsWith("rows =")) {
             int rows = Integer.parseInt(command.substring(command.indexOf("=") + 1).trim());
             setRows(rows);
             return "" + rows;
         } else {
             return "" + getRows();
         }
     } else if (command.startsWith("cols")) {
         if (command.startsWith("cols =")) {
             int cols = Integer.parseInt(command.substring(command.indexOf("=") + 1).trim());
             setCols(cols);
             return "" + cols;
         } else {
             return "" + getCols();
         }
     } else if (command.startsWith("width")) {
         if (command.startsWith("width =")) {
             int width = Integer.parseInt(command.substring(command.indexOf("=") + 1).trim());
             setWidth(width);
             return "" + width;
         } else {
             return "" + getWidth();
         }
     } else {
         return "unknown or malformed command " + command;
     }*/


     String finalize;
     String result = null;
     //String address = result.substring(0, result.indexOf(" ")).trim().toUpperCase();
     //String cellValue = result.substring(result.indexOf("=")+1).trim();
     if (command.contains("value ")) {
         String address = command.substring(6).trim();
         if (isAddress(address) && address!=null &&  !(address.length()<2))  {
          result = String.valueOf(matrix[getRowValue(address)][getColValue(address)]);
          return String.valueOf(Double.parseDouble(result));
         }
         else {
          return "Invalid cell address" + address;
         }
     }
     else if (command.contains("display ")) {
      String address = command.substring(6).trim();
      if (isAddress(address) && address!=null &&  !(address.length()<2)) {
       result = String.valueOf(matrix[getRowValue(address)][getColValue(address)]);
       return String.valueOf(Double.parseDouble(result)).replace("\"", "");
      }
      else {
       return "Invalid cell address" + address;
      }
     }
     else if (command.contains("expr ")) {
      String address = command.substring(6).trim();
      if (isAddress(address) && address!=null && !(address.length() >2)) {
       //result = String.valueOf(matrix[getColValue(address)][getRowValue(address)]);
       result = String.valueOf(matrix[getRowValue(address)][getColValue(address)].getExpression());
       return result;
      }
      else {
       return "Invalid cell address" + address;
      }
     }
     else if (command.contains("clear")) {
         createMatrix();
         result = "";
     }
     else if (command.contains("rows")) {
         if (command.startsWith("rows =")) {
             int rows = Integer.parseInt(command.substring(command.indexOf("=")+1).trim());
             setRows(rows);
             finalize = "" + rows;
             return finalize;
         }
         else {
             finalize = "" + getRows();
             return finalize;
         }
     }
     else if (command.contains("cols")) {
         if (command.startsWith("cols =")) {
             int cols = Integer.parseInt(command.substring(command.indexOf("=")+1).trim());
             setCols(cols);
             finalize = "" + cols;
             return finalize;
         }
         else {
             finalize = "" + getCols();
             return finalize;
         }
     }
     else if (command.contains("width")) {
         if (command.startsWith("width =")) {
             int width = Integer.parseInt(command.substring(command.indexOf("=")+1).trim());
             setWidth(width);
             finalize = "" + width;
             return finalize;
         }
         else {
             finalize = "" + cellWidth;
             return finalize;
         }
     }
     else if (command.contains("=")) {
         String address = command.substring(0, command.indexOf(" ")).trim().toUpperCase();
         String cellValue = command.substring(command.indexOf("=")+1).trim();
         setCellValue(address, cellValue);
         return "";
     }
     else if(command.contains("print")) {
         return renderMatrix();
     }
     else if (command.trim().length() == 2) {
         //int[] address = cellAddress(command.trim());
         return expression(command.trim());
     }
     return "";
 }


 /**
  * saveToFile
  *
  * This method will process the command: "save {filename}"
  * <p>
  * Ask the matrix for all formulas for all non-empty cells. Empty cells should
  * not be saved.
  *
  * Save all properties such as grid size and cell width (if available)
  *
  * @param filename is the name of the file to save
  * @return A message to user about the success/failure of saving to a file.
  */
  private String saveToFile(String filename) {
     File file = new File(filename);
     String result = "Saved to file: " + file.getAbsolutePath();


     try {
         // Get the writer ready
         PrintStream writer = new PrintStream(file);
         saveGrid(writer);
     } catch (FileNotFoundException e) {
         result = "Cannot write to the file: " + file.getAbsolutePath();
     }
     return result;
 }


 /**
  * saveGrid will save the gride to a file.
  *
  * Ask the matrix for all formulas for all non-empty cells. Empty cells should
  * not be saved.
  *
  * Save all properties such as grid size and cell width (if available)
  *
  * @param writer is the PrintStream to print to
  */
 public void saveGrid(PrintStream writer) {
     // save the rows, cols and width
     writer.println("rows = " + rowCount);
     writer.println("cols = " + colCount);
     writer.println("width = " + cellWidth);


     // save the grid formulas, for every cell that is not empty
     for (int row = 0; row < rowCount; row++) {
         for (int col = 0; col < colCount; col++) {
             String formula = matrix[row][col].getExpression();
             if (formula != null && formula.length() > 0) {
                 writer.println("" + (char) ('A' + col) + (row + 1) + " = " + formula);
             }
         }
     }
 }
}

