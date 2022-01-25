//package images;
import java.util.*;
import java.lang.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.*;
import java.util.stream.*;


public class Image{
    BufferedImage img;
    ArrayList<ArrayList<Map>> rgbArray = new ArrayList<ArrayList<Map>>();


    Image(String fileName){
      readImg(fileName);

    }

    public void readImg(String fileName){
      try {
          img = ImageIO.read(new File(fileName));
      } catch (IOException e) {  }

    }
    public void showImg(){
      JFrame frame = new JFrame("IMAGE");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(new JLabel(new ImageIcon(img)));
      frame.pack();
      frame.setVisible(true);
    }

    public int[][] getPixels(){
      int height = img.getHeight();
      int width = img.getWidth();
      int[][] imgPixels = new int[height][width];
      for (int h=0;h<height;h++){
        for (int w=0;w<width;w++){
          int pixel = img.getRGB(w,h);
          imgPixels[h][w]=pixel;
        }
      }
      return imgPixels;
    }

    public ArrayList<ArrayList<Map>> getRGBArrayFromPixels(int[][] pixels){
      int pixel;
      ArrayList<ArrayList<Map>> rgb = new ArrayList<ArrayList<Map>>();
      ArrayList<Map> rgbRow = new ArrayList<Map>();
      Map<String, Integer> rgbPixel;
      for (int h=0;h<pixels.length;h++){
        for (int w=0;w<pixels[0].length;w++){
          pixel = pixels[h][w];
          rgbPixel = getMapRGB(pixel);
          //rgbPixel = setRGBPixel(rgbPixel, "red",100);
          rgbRow.add(rgbPixel);
        }
        ArrayList<Map> nrgbRow = new ArrayList<Map>(rgbRow);
        rgb.add(nrgbRow);
        rgbRow.clear();
      }
      return rgb;
    }


    public int[][] convertRGBArrayToPixels(ArrayList<ArrayList<Map>> rgb){
      int[][] imgPixels = new int[rgb.size()][rgb.get(0).size()];
      for (int h=0;h<rgb.size();h++){
        for (int w=0;w<rgb.get(0).size();w++){
          imgPixels[h][w] = getPixelFromMap(rgb.get(h).get(w));
        }
      }
      return imgPixels;
    }

    public void setImageFromRGBArray(ArrayList<ArrayList<Map>> rgb){
      int[][] imgPixels = convertRGBArrayToPixels(rgb);
      setImageFromPixels(imgPixels);
    }

    public void setImageFromPixels(int[][] imgPixels){
      int height = imgPixels.length;
      int width = imgPixels[0].length;
      int[] newPixels = array2Dto1D(imgPixels);
      img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      img.setRGB(0, 0, width, height, newPixels, 0, width);
    }


    public Map getMapRGB(int pixel){
      Map<String, Integer> c = new HashMap<String, Integer>();
      Color color = new Color(pixel, true);
      c.put("red",color.getRed()); //get the 2 hex digits and convert
      c.put("green",color.getGreen());
      c.put("blue",color.getBlue());
      c.put("alpha",color.getAlpha());
      return c;
    }

    public Color getColorFromMapRGB(Map c){
      int r = (Integer)c.get("red");
      int g = (Integer)c.get("green");
      int b = (Integer)c.get("blue");
      int a = (Integer)c.get("alpha");
      Color color = new Color(r,g,b,a);
      return color;
    }

    public int getPixelFromMap(Map c){
      return getColorFromMapRGB(c).getRGB();
    }

    public void setImagePixelFromColor(int x, int y, Color color){
      img.setRGB(x, y, color.getRGB());
    }

    public void setImagePixelFromRBGArray(int x, int y, Map color){
      setImagePixelFromColor(x,y,getColorFromMapRGB(color));
    }

    public int[] array2Dto1D(int[][] imgPixels){
      ArrayList<Integer> newPixels = new ArrayList<Integer>();
      for (int[] row: imgPixels){
        for (int color: row){
          newPixels.add(color);
        }
      }
      int[] newPixelArr = newPixels.stream().mapToInt(i -> i).toArray();
      return newPixelArr;
    }

    public static void main(String args[]){
      Image i = new Image("../input/img.png");



    }
}



class Grayscale extends Image{

  public Grayscale(String filename){
    super(filename);
  }


  public void editImageColoring(){
    Map<String, Integer> rgbPixel;
    int[][] pixels = getPixels();
    rgbArray = getRGBArrayFromPixels(pixels);
    for (int h=0;h<rgbArray.size();h++){
      for (int w=0;w<rgbArray.get(0).size();w++){
        rgbPixel = rgbArray.get(h).get(w);
        rgbPixel = convertRGBPixel(rgbPixel, "grayscale luminance");
        rgbArray.get(h).set(w,rgbPixel);
      }
    }
    setImageFromRGBArray(rgbArray);
    showImg();

  }
  public Map convertRGBPixel(Map rgbPixel, String how){
    int value;
    switch(how){
      case "grayscale avg":
        value = getAverageColorValueFromMap(rgbPixel);
        setAllValues(rgbPixel,value);
        break;
      case "simple":
        editRGBPixel(rgbPixel,"green",-100, "add");
        break;
      case "grayscale luminance":
        editRGBPixel(rgbPixel,"red",.2126, "multiply");
        editRGBPixel(rgbPixel,"green",.7512, "multiply");
        editRGBPixel(rgbPixel,"blue",.0722, "multiply");
        value = getSumFromMap(rgbPixel);
        setAllValues(rgbPixel,value);
        break;
    }

    return rgbPixel;
  }

  public void run(){
    int[][] pixels = getPixels();
    rgbArray = getRGBArrayFromPixels(pixels);
    setImageFromRGBArray(rgbArray);
    showImg();
  }

  public int getAverageColorValueFromMap(Map color){
    Map<String, Integer> new_map = new HashMap<String, Integer>();
    new_map.putAll(color);
    new_map.remove("alpha");
    ArrayList<Integer> list = new ArrayList<Integer>(new_map.values());
    int average = (int)list.stream().mapToInt(val -> val).average().orElse(0.0);
    return valid(average);
  }

  public int getSumFromMap(Map color){
    Map<String, Integer> new_map = new HashMap<String, Integer>();
    new_map.putAll(color);
    new_map.remove("alpha");
    ArrayList<Integer> list = new ArrayList<Integer>(new_map.values());
    int sum = (int)list.stream().mapToInt(val -> val).sum();
    return valid(sum);
  }

  public int valid(int value){
    return Math.min(Math.max(value, 0),255);
  }


  public Map editRGBPixel(Map c, String aspect, double change, String how){
    int value = (Integer)c.get(aspect);
    switch (how){
      case "multiply":
        Double temp = value * change;
        value = temp.intValue();
        break;
      case "add":
        value = value + (int)change;
        break;
    }
    value = valid(value);
    c.put(aspect, value);
    return c;
  }
  public Map setRGBPixel(Map c, String aspect, int value){
    c.put(aspect, value);
    return c;
  }

  public Map setAllValues(Map color, int value){
    color.replace("green",value);
    color.replace("blue",value);
    color.replace("red",value);
    return color;
  }

  public static void main(String args[]){
    Grayscale image = new Grayscale("../input/img.png");
    image.editImageColoring();
    //image.convertToGrayscaleAverage();
    //image.run();
  }
}

interface FinalImagesInterface{
  // public void func();
}

class FinalImages extends Grayscale implements FinalImagesInterface{
  String FileName;
  public FinalImages(String filename){
    super(filename);
    FileName = filename;
    //editImageColoring();

  }
  public int[] getImageDimensions(){
    int[] dim = {img.getHeight(), img.getWidth()};
    return dim;
  }
  public String getFileName(){
    return FileName;
  }
  public static void main(String args[]){
    FinalImages image = new FinalImages("../input/img.png");
    image.editImageColoring();
    //image.run();
  }
}
