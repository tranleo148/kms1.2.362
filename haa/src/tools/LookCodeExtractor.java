package tools;

import java.io.File;
import java.io.FileOutputStream;

public class LookCodeExtractor {
  public static void main(String[] args) {
    try {
      System.out.println("헤어 성형을 추출합니다.");
      if ((new File("mhair.txt")).exists())
        (new File("mhair.txt")).delete(); 
      if ((new File("mface.txt")).exists())
        (new File("mface.txt")).delete(); 
      if ((new File("fhair.txt")).exists())
        (new File("fhair.txt")).delete(); 
      if ((new File("fface.txt")).exists())
        (new File("fface.txt")).delete(); 
      FileOutputStream mhair = new FileOutputStream("mhair.txt");
      FileOutputStream mface = new FileOutputStream("mface.txt");
      FileOutputStream fhair = new FileOutputStream("fhair.txt");
      FileOutputStream fface = new FileOutputStream("fface.txt");
      try {
        for (int i = 3000; i < 4499; i++) {
          if ((new File(getPath(true, i * 10))).exists()) {
            boolean allcheck = true;
            for (int s = 1; s <= 7; s++) {
              if (!(new File(getPath(true, i * 10 + s))).exists())
                allcheck = false; 
            } 
            if (allcheck)
              if (i / 100 == 30 || i / 100 == 33 || i / 100 == 40 || i / 100 == 43) {
                mhair.write(((i * 10) + ",").getBytes());
              } else {
                fhair.write(((i * 10) + ",").getBytes());
              }  
          } 
        } 
        for (int a = 0; a <= 6; a++) {
          for (int j = 0; j < 99; j++) {
            if ((new File(getPath(false, 20000 + j + a * 1000))).exists()) {
              boolean allcheck = true;
              for (int s = 1; s <= 7; s++) {
                if (!(new File(getPath(false, 20000 + a * 1000 + j + s * 100))).exists())
                  allcheck = false; 
              } 
              if (allcheck)
                if (a == 0 || a == 3 || a == 5) {
                  mface.write(((20000 + a * 1000 + j) + ",").getBytes());
                } else {
                  fface.write(((20000 + a * 1000 + j) + ",").getBytes());
                }  
            } 
          } 
        } 
      } catch (NullPointerException npe) {
        System.err.println("오류가 발생했습니다.");
      } 
      System.out.println("완료");
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private static String getPath(boolean hair, int value) {
    String path = "wz/Character.wz/" + (hair ? "Hair" : "Face") + "/" + StringUtil.getLeftPaddedStr(Integer.toString(value), '0', 8) + ".img.xml";
    File es = new File(path);
    System.out.println(es.getAbsolutePath());
    System.out.println(es.exists());
    return path;
  }
}
