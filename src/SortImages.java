package src;
import java.util.*;
import java.util.stream.*;
import src.*;

public class SortImages{

  public static void main(String args[]){
    List<FinalImages> ilist = Arrays.asList(
    new FinalImages("img.png"),
    new FinalImages("img.png")
    );
    Map<String, Long> counting = ilist.stream().collect(
        Collectors.groupingBy(FinalImages::getFileName, Collectors.counting()));

    System.out.println(counting);
  }

}
