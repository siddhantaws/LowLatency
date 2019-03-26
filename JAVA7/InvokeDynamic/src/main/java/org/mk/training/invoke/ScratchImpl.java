package org.mk.training.invoke;

public class ScratchImpl {

  private static ScratchImpl inst = null;

  // Constructor
  private ScratchImpl() {
    super();
  }

  /*
   * This is where your actual code will go
   */
  private void run() {
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
      int i=5;
    inst = new ScratchImpl();
    inst.run();
  }

}
