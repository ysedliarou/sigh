package org.sedly.sigh.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.sedly.sigh.model.Loader;
import org.sedly.sigh.shader.ShaderProgram;

public class ResourceUtil {

  public static String loadString(String fileName) {
    StringBuffer sb = new StringBuffer();
    try {
      BufferedReader bf = new BufferedReader(new FileReader(new File(
          ShaderProgram.class.getResource(fileName).getFile())));
      String line;
      while ((line = bf.readLine()) != null) {
        sb.append(line).append("\n");
      }
      bf.close();
    } catch (
        IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  public static BufferedImage loadImage(String fileName) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(Loader.class.getResource(fileName).getFile()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

}
