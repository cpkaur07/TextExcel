//Charanpreet Kaur
//Period 4
//Text Excel

// This class highly leverages inheritance and requires very little code.
public class TextCell extends Cell {
    
   /*
    *  This will return the string for how a TextCell wants to display
    *  itself. All it needs to do is remove the bounding quotes from
    *  the expression.
    */
    
    public String toString() {
        return getExpression().replace("\"", "");
    }
}
