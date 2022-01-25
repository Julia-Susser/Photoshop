package src;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.*;
public class trial{
  public int a=3;
  public int o;
  public trial(int p){
    o = p;
  }
  public Map h(){
    Map<String, Integer> h = new HashMap<String, Integer>();

    return h;
  }
  public void c(){
    //Map<String, Integer> p = h();
  }
  public void k(int a){
    a = 7;
    System.out.println(a);
  }
  public void l(){
    a = 9;
  }
  public static void main(String args[]){
    trial t = new trial(5);
    System.out.println(t.o);

    t.k(5);
    System.out.println(t.a);
    t.l();
    System.out.println(t.a);
    List<Integer> num = new ArrayList<Integer>(Arrays.asList(1,2,3));
    List<List<Integer>> k = new ArrayList<List<Integer>>();
    k.add(num);
    k = k.stream().map(n -> n).collect(Collectors.toList());

    // List<Integer> num = Arrays.asList(1,2,3,4,5);
    List<Integer> collect1 = num.stream().map(n -> n * 2).collect(Collectors.toList());
    System.out.println(collect1); //[2, 4, 6, 8, 10]
    num.forEach((n) -> System.out.println(n));

    ArrayList<Integer> l = new ArrayList<Integer>(Arrays.asList(3));
  }
}
