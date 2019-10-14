package org.sedly.sigh.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.sedly.sigh.math.Vector3f;

public class ObjLoader {

  private static class Vector2f {

    float x, y;

    Vector2f(float x, float y) {
      this.x = x;
      this.y = y;
    }
  }

  public static Model loadObjModel(String fileName) {

    List<Vector3f> vertices = new ArrayList<Vector3f>();
    List<Vector2f> textures = new ArrayList<Vector2f>();
    List<Vector3f> normals = new ArrayList<Vector3f>();
    List<Integer> indices = new ArrayList<Integer>();
    float[] verticesArray = null;
    float[] normalsArray = null;
    float[] textureArray = null;
    int[] indicesArray = null;

    List<String> faces = new ArrayList<>();

    try {
      FileReader fr = new FileReader(new File(ObjLoader.class.getResource(fileName).getFile()));
      BufferedReader reader = new BufferedReader(fr);

      String line;
      while ((line = reader.readLine()) != null) {
        String[] currentLine = line.split("\\s+");
        if (currentLine[0].equals("v")) {
          Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
          vertices.add(vertex);
        } else if (currentLine[0].equals("vt")) {
          Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]));
          textures.add(texture);
        } else if (currentLine[0].equals("vn")) {
          Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
          normals.add(normal);
        } else if (currentLine[0].equals("f")) {
          faces.add(line);
        }
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    textureArray = new float[vertices.size() * 2];
    normalsArray = new float[vertices.size() * 3];

    for (String face : faces) {
      String[] currentLine = face.split("\\s+");
      String[] vertex1 = currentLine[1].split("/");
      String[] vertex2 = currentLine[2].split("/");
      String[] vertex3 = currentLine[3].split("/");

      if (vertex1.length > 1) {
        processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
        processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
        processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
      } else {
      }
    }

    verticesArray = new float[vertices.size() * 3];
    indicesArray = new int[indices.size()];
    int vertexPointer = 0;
    for (Vector3f vertex : vertices) {
      verticesArray[vertexPointer++] = vertex.getX();
      verticesArray[vertexPointer++] = vertex.getY();
      verticesArray[vertexPointer++] = vertex.getZ();
    }

    for (int i = 0; i < indices.size(); i++) {
      indicesArray[i] = indices.get(i);
    }

    return new Model(verticesArray, indicesArray, textureArray, normalsArray);
  }

  private static void processVertex(String[] vertexData, List<Integer> indices,
      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {

      int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
      indices.add(currentVertexPointer);
      Vector2f currentText = textures.get(Integer.parseInt(vertexData[1]) - 1);
      textureArray[currentVertexPointer * 2] = currentText.x;
      textureArray[currentVertexPointer * 2 + 1] = 1 - currentText.y;
      Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
      normalsArray[currentVertexPointer * 3] = currentNorm.getX();
      normalsArray[currentVertexPointer * 3 + 1] = currentNorm.getY();
      normalsArray[currentVertexPointer * 3 + 2] = currentNorm.getZ();
  }


}
