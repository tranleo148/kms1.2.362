package constants;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

public class DiskUtils {
  public static String getSerialNumber(String drive) {
    String result = "";
    try {
      File file = File.createTempFile("realhowto", ".vbs");
      file.deleteOnExit();
      FileWriter fw = new FileWriter(file);
      String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\nSet colDrives = objFSO.Drives\nSet objDrive = colDrives.item(\"" + drive + "\")\nWscript.Echo objDrive.SerialNumber";
      fw.write(vbs);
      fw.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = input.readLine()) != null)
        result = result + line; 
      input.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return result.trim();
  }
  
  public static void main(String[] args) {
    String sn = getSerialNumber("C");
    JOptionPane.showConfirmDialog((Component)null, sn, "Serial Number of C:", -1);
  }
}
