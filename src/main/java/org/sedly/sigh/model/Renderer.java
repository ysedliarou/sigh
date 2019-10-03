package org.sedly.sigh.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class Renderer {

  public Renderer() {
    GL11.glFrontFace(GL11.GL_CCW);

    GL11.glCullFace(GL11.GL_BACK);
    GL11.glEnable(GL11.GL_CULL_FACE);

    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL32.GL_DEPTH_CLAMP);

  }

  public void prepare() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glClearColor(0, 0, 0, 1);
  }

  public void render(TexturedMesh texturedMesh) {
    Mesh mesh = texturedMesh.getMesh();

    GL30.glBindVertexArray(mesh.getVaoId());
    GL20.glEnableVertexAttribArray(0); // position
    GL20.glEnableVertexAttribArray(1); // texture
    GL20.glEnableVertexAttribArray(2); // normal
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedMesh.getTexture().getTextureId());
    GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    GL30.glBindVertexArray(0);
  }

}
