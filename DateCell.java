//Charanpreet Kaur
//Period 4
//Text Excel

/*
 * This is a stub class that will be completed for the Final Submission.
 *
 * For the Final submission:
 *   Students should leverage the SimpleDateFormat and/or DateFormat.
 *   Do NOT implement from scratch. Instead, search the internet for help
 *   on how to use SimpleDateFormat/DateFormat to accomplish the desired
 *   behavior.
 *
 *   A properly implemented class can have as few as ~10 lines of code.
 */

 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.text.ParseException;

public class DateCell extends Cell {
   
   public String toString() {
      String expr = getExpression();
      String originalPattern;
      if(expr.substring(expr.lastIndexOf("/") + 1).trim().length()==4) {
         originalPattern = "MM/dd/yyyy";
      }
      else {
         originalPattern = "MM/dd/yy";
      }
      String targetPattern = "MMM d, yyyy";
      SimpleDateFormat originalFormat = new SimpleDateFormat(originalPattern);
      SimpleDateFormat targetFormat = new SimpleDateFormat(targetPattern);
      try {
         Date date = originalFormat.parse(expr);
         String formattedDate = targetFormat.format(date); 
         return formattedDate;
      }
      catch (ParseException e) {
         return "Error parsing date: " + e.getMessage();
      }
   }
}
