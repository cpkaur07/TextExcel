//Charanpreet Kaur
//Period 4
//Text Excel 

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/*
* The Grid class will hold all the cells. It allows access to the cells via the
* public methods. It will create a display String for the whole grid and process
* many processCommands that update the cells. These processCommand will include
* sorting a range of cells and saving the grid contents to a file.
*
*/

public class Grid extends GridBase {
    // These are called instance fields.
    // They are scoped to the instance of this Grid object.
    // Use them to keep track of the count of columns, rows and cell width.
    // They are initialized to the prescribed default values.
    // Notice that there is no "static" here.

    private int colCount = 7;      // Default column count
    private int rowCount = 10;     // Default row count
    private int cellWidth = 9;     // Default cell width
    private Cell[][] matrix;       // Holds the cell values in the grid

    public Grid() {
        createMatrix();
    }


    private void createMatrix() {
        //Sets the size of the matrix based on the number of rows and columns
        matrix = new Cell[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0;col < colCount; col++) {
                matrix[row][col] = new Cell();
            }
        }
    }

    //Returns the header of the grid based on the number of columns
    private String colHeading() {
        String header = "    |";
        for (int i = 0; i < colCount; i++) {
            //Stores the column label as an alphabet
            char colHeader = (char) ('A' + i);
            String colLetter = String.valueOf(colHeader);
            //Sets the spacing around the label based on the width of the column
            int emptySpace = cellWidth - colLetter.length();
            int right = emptySpace / 2;
            int left = emptySpace - right;
            for ( int space=1 ; space<=left ; space++) {
                header += " ";
            }
            header += colLetter;
            for ( int space=1 ; space<=right ; space++) {
                header += " ";
            }
            header += "|";
        }
        header += "\n";
        return header;
    }
    
    //Returns the separating line between the column header and the rows
    private String separator() {
        String separation = "----+";
        for ( int i =1 ; i<= colCount ; i++) {
            for (int j=1 ; j<=cellWidth ; j++) {
                separation += "-";
            }
            separation += "+";
        }
        separation += "\n";
        return separation;
    }
    
    //returns the rows with the cell elements inside of them
    private String printRow() {
        String row = "";
        for (int i = 0; i < rowCount; i++) {
            String rowNum = String.valueOf(i + 1);   //Row label
            if (rowNum.trim().length()>1) {
                //Adds required space and row label for multi-digit row labels
                row += " " + rowNum + " |";
            }
            else 
            {
                //Adds required space and row label for single-digit row labels
                row += "  " + rowNum + " |";
            }
            for (int j = 0; j < colCount; j++) {
                //Adds the element that is supposed to be in that particular cell
                row += displayCell(matrix[i][j]) + "|";
            }
            //Adds the separating line between consecutive rows
            row += "\n----+";
            for ( int col =1 ; col<= colCount ; col++) {
                for (int width=1 ; width<=cellWidth ; width++) {
                    row += "-";
                }
                row += "+";
            }
            row += "\n";
        }
        return row;
    }
    
    //Combines the column header, separating line and the rows to print the complete grid
    private String printMatrix() {
        return colHeading() + separator() + printRow();
    }
    
    //Returns the cell address of the matrix element as column number and row number in an integer array 
    private int[] cellAddress(String address) {
        //Checks for null or invalid cell address
        if (address == null || address.length() < 2) {
            return null;
        }
        //Extracts the column label
        char colChar = Character.toUpperCase(address.charAt(0));
        //Checks for invalid column label
        if (colChar < 'A' || colChar > 'Z') {
            return null;
        }
        //Extracts the row label
        String rowStr = address.substring(1);
        int rowNum;
        try {
            //Converts the row label to row number
            rowNum = Integer.parseInt(rowStr) - 1;
        }
        catch (NumberFormatException e) {
            return null;
        }
        //Converts the column label to column number
        int colNum = colChar - 'A';
        ///Checks for invalid column numbers and row numbers
        if (rowNum < 0 || rowNum >= rowCount || colNum < 0 || colNum >= colCount) {
            return null;
        }
        //Stores and returns the row number and column number in an integer array
        return new int[] {rowNum, colNum};
    }
    
    //Clears the single cell given in the command
    private void clearCell(String address) {
        int colNum = getColumnValue(address);
        int rowNum = getRowValue(address);
        matrix[rowNum][colNum] = new Cell();
    }
    
    //Sets the padding and truncates the cell value based on the cell width and length of cell value
    private String displayCell(Cell cellValue) {
        String cellVal = String.valueOf(cellValue);
        String finalValue = "";
        int spaces = cellWidth - cellVal.length();
        if (spaces < 0) {
            //truncates the cell value based on the cell width
            finalValue += cellVal.substring(0, cellWidth);
        }
        else 
        {
            for ( int i=1 ; i<=spaces ; i++) {
                //Adds required padding before the cell value
                finalValue += " ";
            }
            finalValue += cellVal;
        }
        return finalValue;
    }
    
    //Returns the column number in the given cell address
    private int getColumnValue(String cellLocation) {
        String location = cellLocation.toLowerCase();
        char column = location.charAt(0);
        int columnValue = Character.toLowerCase(column) - 'a';
        return columnValue;
    }
    
    //Returns the row number in the given cell address
    private int getRowValue(String cellLocation) {
        //Checks for invalid cell address
        if(cellLocation == null || cellLocation.length()<2) {
            throw new IllegalArgumentException("invalid cell location: " + cellLocation);
        }
        String row = cellLocation.substring(1);
        try {
            int rowValue = Integer.parseInt(row) - 1;
            return rowValue;
        }
        //Catches invalid row number
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid row number in: " + cellLocation);
        }
    }
    
    //Calculates the sum or average of a range of cells
    private String calculateSumOrAvg(String command) {
        //Removes all the parentheses in the command
        command =  command.replace("(", "");
        command = command.replace(")", "");
        //Extracts the expression
        command = command.substring(command.indexOf("=")+1).trim();
        //Splits the expression into beginning cell address and ending cell address of the range
        String[] tokens = command.split(" ");
        //Checks for invalid command
        if (tokens.length < 4) {
            return "malformed sum or avg command";
        }
        String first = tokens[1];             //Beginning cell address
        String second = tokens[3];            //Ending cell address
        double sum = 0;                       //stores the sum
        int cells = 0;                        //stores the numbers of cells in the range
        int startRow = getRowValue(first);                    //Beginning row number
        int endRow = getRowValue(second);                     //Ending row number
        int startCol = getColumnValue(first);                 //Beginning column number
        int endCol = getColumnValue(second);                  //Ending column number
        //Extracts the cell value of all cells in the range
        for(int row = startRow; row <= endRow; row++) {
            for (int col = startCol ; col <= endCol ; col++) {
                String value = matrix[row][col].toString();
                if (!value.equals("")) {
                    //Adds the cell values of all cells in the range
                    sum += Double.parseDouble(value);
                }
                //Adds the number of cells in the range
                cells++;
            }
        }
        //Returns sum based on the command
        if(tokens[0].equals("sum")) {
            return "" + sum;
        } 
        //Returns average based on the command
        else {
            return "" + (sum/cells);
        }
    }
    
    //Sorts numbers, dates, text values or mixed types in ascending order
    private void sorta(String command) {
        //Extracts the range of cells to be sorted
        String range = command.substring(5).trim();
        range = range.toLowerCase();
        String[] addresses = range.split("-");
        int col1 = addresses[0].trim().charAt(0) - 'a';                        //Beginning column number
        int row1 = Integer.parseInt(addresses[0].trim().substring(1))-1;       //Beginning row number
        int col2 = addresses[1].trim().charAt(0) - 'a';                        //Ending column number
        int row2 = Integer.parseInt(addresses[1].trim().substring(1))-1;       //Ending row number

        ArrayList<TextCell> text = new ArrayList<>();      //Stores all text values to be sorted 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                Cell cell = matrix[row][col];
                //Checks if the cell element is of type TextCell
                if (cell instanceof TextCell) {
                    //Adds text values to the list
                    text.add((TextCell)cell);
                }
            }
        }
        //Sorts the list containing text values in ascending order
        Collections.sort(text, Comparator.comparing(TextCell::toString, String.CASE_INSENSITIVE_ORDER));
        //Checks if all cell elements in the range are text values
        boolean allText = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof TextCell) ){
                    allText = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are text values
        if (allText) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = text.get(p);
                    p++;
                }
            }
        }
        
        ArrayList<DateCell> dates = new ArrayList<>();             //Stores all dates of type DateCell to be sorted 
        ArrayList<Date> parseDates = new ArrayList<>();            //Stores dates parsed into type Date 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                Cell cell = matrix[row][col];
                //Checks if the cell element is of type DateCell
                if (cell instanceof DateCell) {
                    DateCell dc = (DateCell)cell;
                    String express = dc.getExpression();
                    String originalPattern = express.substring(express.lastIndexOf("/") + 1).trim();
                    if (originalPattern.length() == 4) {
                        originalPattern = "MM/dd/yyyy";
                    }
                    else {
                        originalPattern = "MM/dd/yy";
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(originalPattern);
                    try {
                        //Parses the dates into type Date using SimpleDateFormat so they can be sorted
                        Date parsedDate = sdf.parse(express);
                        //Adds date values to the lists
                        dates.add(dc);
                        parseDates.add(parsedDate);
                    }
                    catch(ParseException e) {
                    }
                }
            }
        }
        for (int i=0 ; i < parseDates.size() - 1 ; i++) {
            for (int j = i+1 ; j < parseDates.size() ; j++) {
                //Checks the dates for the earliest ones
                if (parseDates.get(i).after(parseDates.get(j))) {
                    //Swaps the parsed later dates by the parsed earlier ones
                    Date tempDate = parseDates.get(i);
                    parseDates.set(i, parseDates.get(j));
                    parseDates.set(j, tempDate);
                    //Swaps the later dates of type DateCell by the earlier ones of type DateCell
                    DateCell tempCell = dates.get(i);
                    dates.set(i, dates.get(j));
                    dates.set(j, tempCell);
                }
            }
        }
        //Checks if all cell elements in the range are date values
        boolean allDates = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof DateCell)) {
                    allDates = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are date values
        if (allDates) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = dates.get(p);
                    p++;
                }
            }
        }
        
        ArrayList<NumberCell> nums = new ArrayList<>();      //Stores all number values to be sorted 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                //Checks if the cell element is of type NumberCell
                if (matrix[row][col] instanceof NumberCell) {
                    //Adds number values to the list
                    nums.add((NumberCell)matrix[row][col]);
                }
            }
        }
        //Sorts the number list into ascending order
        Collections.sort(nums, new Comparator<NumberCell>() {
            public int compare(NumberCell a, NumberCell b) {
                double valA = Double.parseDouble(a.toString());
                double valB = Double.parseDouble(b.toString());
                return Double.compare(valA, valB);
            }
        });
        //Checks is all cell elements in the range are number values
        boolean allNum = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof NumberCell)) {
                    allNum = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are number values
        if (allNum) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = nums.get(p);
                    p++;
                }
            }
        }
        
        //Stores all the empty cell values in the range
        ArrayList<Cell> empty = new ArrayList<>();
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                //Checks if the cell elements are empty
                if (!(matrix[row][col] instanceof NumberCell) && !(matrix[row][col] instanceof TextCell) && !(matrix[row][col] instanceof DateCell)) {
                    empty.add(matrix[row][col]);
                }
            }
        }
        //Sorts the sorted lists in ascending order by the order of empty cells, numbers, 
        //dates and then text values
        ArrayList<Cell> sorted = new ArrayList<>();
        sorted.addAll(empty);
        sorted.addAll(nums);
        sorted.addAll(dates);
        sorted.addAll(text);
        //Sets the sorted list in the grid if all cell elements in the range are of mixed types
        int f=0;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                matrix[row][col] = sorted.get(f);
                f++;
            }
        }
    }
    
    //Sorts numbers, dates and text values or mixed types in descending order
    private void sortd(String command) {
        //Extracts the range of cells to be sorted
        String range = command.substring(5).trim();
        range = range.toLowerCase();
        String[] addresses = range.split("-");
        int col1 = addresses[0].trim().charAt(0) - 'a';                        //Beginning column number
        int row1 = Integer.parseInt(addresses[0].trim().substring(1))-1;       //Beginning row number
        int col2 = addresses[1].trim().charAt(0) - 'a';                        //Ending column number
        int row2 = Integer.parseInt(addresses[1].trim().substring(1))-1;       //Ending row number

        ArrayList<TextCell> text = new ArrayList<>();         //Stores all text values to be sorted 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                Cell cell = matrix[row][col];
                //Checks if the cell element is of type TextCell
                if (cell instanceof TextCell) {
                    //Adds text values to the list
                    text.add((TextCell)cell);
                }
            }
        }
        //Sorts the list containing text values in descending order
        text.sort(Comparator.comparing(TextCell::toString, String.CASE_INSENSITIVE_ORDER).reversed());
        //Checks if all cell elements in the range are text values
        boolean allText = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof TextCell)) {
                    allText = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are text values
        if (allText) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = text.get(p);
                    p++;
                }
            }
        }
        
        ArrayList<DateCell> dates = new ArrayList<>();             //Stores all dates of type DateCell to be sorted 
        ArrayList<Date> parseDates = new ArrayList<>();            //Stores dates parsed into type Date 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                Cell cell = matrix[row][col];
                //Checks if the cell element is of type DateCell
                if (cell instanceof DateCell) {
                    DateCell dc = (DateCell)cell;
                    String express = dc.getExpression();
                    String originalPattern = express.substring(express.lastIndexOf("/") + 1).trim();
                    if (originalPattern.length() == 4) {
                        originalPattern = "MM/dd/yyyy";
                    }
                    else {
                        originalPattern = "MM/dd/yy";
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(originalPattern);
                    try {
                        //Parses the dates into type Date using SimpleDateFormat so they can be sorted
                        Date parsedDate = sdf.parse(express);
                        //Adds date values to the lists
                        dates.add(dc);
                        parseDates.add(parsedDate);
                    }
                    catch(ParseException e) {
                    }
                }
            }
        }
        for (int i=0 ; i < parseDates.size() - 1 ; i++) {
            for (int j = i+1 ; j < parseDates.size() ; j++) {
                //Checks the dates for the later ones
                if (parseDates.get(i).before(parseDates.get(j))) {
                    //Swaps the parsed earlier dates by the parsed later ones
                    Date tempDate = parseDates.get(i);
                    parseDates.set(i, parseDates.get(j));
                    parseDates.set(j, tempDate);
                    //Swaps the earlier dates of type DateCell by the later ones of type DateCell
                    DateCell tempCell = dates.get(i);
                    dates.set(i, dates.get(j));
                    dates.set(j, tempCell);
                }
            }
        }
        //Checks if all cell elements in the range are date values
        boolean allDates = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof DateCell)) {
                    allDates = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are date values
        if (allDates) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = dates.get(p);
                    p++;
                }
            }
        }
        
        ArrayList<NumberCell> nums = new ArrayList<>();           //Stores all number values to be sorted 
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                //Checks if the cell element is of type NumberCell
                if (matrix[row][col] instanceof NumberCell) {
                    //Adds number values to the list
                    nums.add((NumberCell)matrix[row][col]);
                }
            }
        }
        //Sorts the number list into descending order
        Collections.sort(nums, new Comparator<NumberCell>() {
            public int compare(NumberCell a, NumberCell b) {
                double valA = Double.parseDouble(a.toString());
                double valB = Double.parseDouble(b.toString());
                return Double.compare(valB, valA);
            }
        });
        //Checks if all cell elements in the range are number values 
        boolean allNum = false;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                if (!(matrix[row][col] instanceof NumberCell)) {
                    allNum = false;
                }
            }
        }
        //Sets the sorted list in the grid if all cell elements in the range are number values
        if (allNum) {
            int p=0;
            for (int row = row1 ; row <= row2 ; row++) {
                for (int col = col1 ; col <= col2 ; col++) {
                    matrix[row][col] = nums.get(p);
                    p++;
                }
            }
        }
        
        //Stores all the empty cell values in the range
        ArrayList<Cell> empty = new ArrayList<>();
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                //Checks if the cell elements are empty
                if (!(matrix[row][col] instanceof NumberCell) && !(matrix[row][col] instanceof TextCell) && !(matrix[row][col] instanceof DateCell)) {
                    empty.add(matrix[row][col]);
                }
            }
        }
        //Sorts the sorted lists in ascending order by the order of empty cells, numbers, 
        //dates and then text values
        ArrayList<Cell> sorted = new ArrayList<>();
        sorted.addAll(empty);
        sorted.addAll(nums);
        sorted.addAll(dates);
        sorted.addAll(text);
        //Sets the sorted list in the grid if all cell elements in the range are of mixed types
        int f=0;
        for (int row = row1 ; row <= row2 ; row++) {
            for (int col = col1 ; col <= col2 ; col++) {
                matrix[row][col] = sorted.get(f);
                f++;
            }
        }
    }
    

    private int sqrt(String command) {
        command =  command.replace("(", "");
        command = command.replace(")", "");
        String address = command.substring(0, command.indexOf("=")).trim();
        int col = address.charAt(0) - 'a';
        int row = Integer.parseInt(address.substring(1).trim())-1;
        String expression = command.substring(command.indexOf("=")+1).trim();
        double value = Double.parseDouble(expression.substring(4).trim());
        Cell cell;
        cell = new NumberCell();
        cell.setExpression("" + (int) Math.sqrt(value));
        matrix[row][col] = cell;
        return (int) Math.sqrt(value);
    }
    

    private int power(String command) {
        command =  command.replace("(", "");
        command = command.replace(")", "");
        String address = command.substring(0, command.indexOf("=")).trim();
        int col = address.charAt(0) - 'a';
        int row = Integer.parseInt(address.substring(1).trim())-1;
        String expression = command.substring(command.indexOf("=")+1).trim();
        int base = Integer.parseInt(expression.substring(0, expression.indexOf("^")).trim());
        int power = Integer.parseInt(expression.substring(expression.indexOf("^") + 1).trim());
        Cell cell;
        cell = new NumberCell();
        cell.setExpression("" + (int) Math.pow(base, power));
        matrix[row][col] = cell;
        return (int) Math.pow(base, power);
    }
    

    private double log(String command) {
        command =  command.replace("(", "");
        command = command.replace(")", "");
        String address = command.substring(0, command.indexOf("=")).trim();
        int col = address.charAt(0) - 'a';
        int row = Integer.parseInt(address.substring(1).trim())-1;
        String expression = command.substring(command.indexOf("=")+1).trim();
        int value = Integer.parseInt(expression.substring(3).trim());
        Cell cell;
        cell = new NumberCell();
        cell.setExpression("" + Math.log(value));
        matrix[row][col] = cell;
        return Math.log(value);
    }


   /*
    * This method processes a user command.
    *
    * Checkpoint #1 commands are:
    * print : render a text based version of the matrix
    * width = [value] : set the cell width
    * width : get the cell width
    * rows = [value] : set the row count
    * cols = [value] : set the column count
    * rows : get the row count
    * cols : get the column count
    *
    * Checkpoint #2 commands are:
    * [cell] = [expression] : set the cell's expression
    * [cell] : get the cell's expression
    * value [cell] : get the cell value
    * expr [cell] : get the cell's expression
    * display [cell] : get the string for how the cell wants to display itself
    * clear : empty out the entire matrix
    * save [file] : saves to a file all the commands necessary to regenerate the grid's contents
    */
    public String processCommand(String command) {
        String result = null;
        String express = command.substring(command.indexOf("=") + 1);
        Cell newCell = new Cell();
        newCell.setExpression(express);
        command = command.trim();
        if (command.contains("print")) {
            result = printMatrix();
        }

        else if (command.contains("sum") || command.contains("avg")) {
            int eq=0;
            for (int i=0 ; i<command.length() ; i++) {
                if (command.charAt(i) == '=') {
                    eq++;
                }
            }
            if (eq>1) {
                return "invalid input: cannot recognize";
            }
            result = calculateSumOrAvg(command);
            if (command.contains("=")){
                String[] separated = command.split(" = ", 2);
                String address = separated[0].trim();
                String expression = separated[1].trim();
                int[] position = cellAddress(address);
                Cell cell;
                cell = new NumberCell();
                cell.setExpression(expression);
                matrix[position[0]][position[1]] = cell;
            }
        }
        
        else if (command.equals("rows")) {
            result = String.valueOf(rowCount);
        }
        
        else if (command.contains("rows = ")) {
            int newRow = Integer.parseInt(command.split(" = ")[1]);
            if (newRow < 1 || newRow > 49) {
                return "Invalid row count not between 1 and 49.";
            }
            rowCount = newRow;
            createMatrix();
            result = String.valueOf(rowCount);
        }
        
        else if(command.contains("sorta")) {
            sorta(command);
            result = "";
        }
        
        else if(command.contains("sortd")) {
            sortd(command);
            result = "";
        }
        
        else if(command.contains("sqrt")) {
            sqrt(command);
            result = "" + sqrt(command);
        }
        
        else if(command.contains("^")) {
            power(command);
            result = "" + power(command);
        }
        
        else if(command.contains("log")) {
            log(command);
            result = "" + log(command);
        }
        
        else if (command.equals("cols")) {
            result = String.valueOf(colCount);
        }
        
        else if (command.contains("cols = ")) {
            int newCol = Integer.parseInt(command.split(" = ")[1]);
            if (newCol < 1 || newCol > 26) {
                return "Invalid column count not between 1 and 26.";
            }
            colCount = newCol;
            createMatrix();
            result = String.valueOf(colCount);
        }
        else if (command.equals("width")) {
            result = String.valueOf(cellWidth);
        }
        
        else if (command.contains("width = ")) {
            int newWidth = Integer.parseInt(command.split(" = ")[1]);
            if (newWidth < 3 || newWidth > 29) {
                return "Invalid cell width not between 3 and 29.";
            }
            cellWidth = newWidth;
            result = String.valueOf(cellWidth);
        }
        
        
        else if (command.contains("value ")) {
            String cellLocation = command.substring(6).trim();
            int[] indices = cellAddress(cellLocation);
            if (indices == null) {
                return "Invalid cell location " + cellLocation;
            }
            result = String.valueOf(matrix[indices[0]][indices[1]].getValue());
        }
        
        else if (command.contains("expr ")) {
            String cellLocation = command.substring(5).trim();
            int[] indices = cellAddress(cellLocation);
            if (indices == null) {
                return "Invalid cell location " + cellLocation;
            }
            result = matrix[indices[0]][indices[1]].getExpression();
        }
        
        else if (command.contains("display ")) {
            String cellLocation = command.substring(8).trim();
            int[] indices = cellAddress(cellLocation);
            if (indices == null) {
                return "Invalid cell location " + cellLocation;
            }
            result = matrix[indices[0]][indices[1]].toString();
        }
        
        else if (command.equals("clear")) {
            createMatrix();
            result = "";
        }
        
        else if (command.startsWith("clear")) {
            String address = command.trim().substring(command.indexOf(" ")).trim();
            clearCell(address);
            result = "";
        }
        
        else if (command.startsWith("save ")) {
            String file= command.substring(5).trim();
            result = saveToFile(file);
        }
        
        else if (command.contains(" = ")) {
            String[] separated = command.split(" = ", 2);
            String address = separated[0].trim();
            String expression = separated[1].trim();
            int[] position = cellAddress(address);
            int slashes=0;
            for(int i=0 ; i<expression.length(); i++) {
                String comparable = "";
                comparable += expression.charAt(i);
                if (comparable.equals("/")) {
                    slashes++;
                }
            }
            if (position == null) {
                return "Invalid cell location " + address;
            }
            if (!address.matches("[a-zA-Z]+\\d+")) {
                return "Invalid cell location " + address;
            }
            Cell cell;
            if (expression.startsWith("\"") && expression.endsWith("\"")) {
                cell = new TextCell();
                cell.setExpression(expression);
            }
            else if (slashes == 2 && (expression.length()<=10)) {
                cell = new DateCell();
                cell.setExpression(expression);
            }
            else {
                try {
                    cell = new NumberCell();
                    cell.setExpression(expression);
                }
                catch (NumberFormatException e) {
                    return "invalid input: cannot recognize";
                }
            }
            cell.setExpression(expression);
            matrix[position[0]][position[1]] = cell;
            result = "";
        }
        
        else {
            int[] position = cellAddress(command);
            if (position != null) {
                result = matrix[position[0]][position[1]].getExpression();
            }
            else {
                result = "unknown or malformed command: " + command;
            }
        }
        return result;
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
        File outputFile = new File(filename);
        String status = "Saved to file: " + outputFile.getAbsolutePath();
        try {
            PrintStream outputStream = new PrintStream(outputFile);
            saveGrid(outputStream);
        } 
        catch (FileNotFoundException e) {
            status = "Cannot write to the file: " + outputFile.getAbsolutePath();
        }
        return status;
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
    public void saveGrid(PrintStream outputStream) {
        // Save configuration
        outputStream.println("rows = " + rowCount);
        outputStream.println("cols = " + colCount);
        outputStream.println("width = " + cellWidth);
        
        // Save non-empty cells
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                String cellContent = matrix[i][j].getExpression();
                if (cellContent != null && !cellContent.isEmpty()) {
                    char colLabel = (char)('A' + j);
                    int rowLabel = i + 1;
                    outputStream.println("" + colLabel + rowLabel + " = " + cellContent);
                }
            }
        }
    }
}