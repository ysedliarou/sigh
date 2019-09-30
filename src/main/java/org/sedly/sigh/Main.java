package org.sedly.sigh;

public class Main {

  public static void main(String[] args) {

    Window window = new Window(Window.WIDTH, Window.HEIGHT);
    window.create();

    while (!window.isClosed()) {
      window.update();
      window.swap();
    }

    window.stop();
  }
}
