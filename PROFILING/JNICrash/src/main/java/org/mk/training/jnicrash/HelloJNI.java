package org.mk.training.jnicrash;

/**
 * Hello world!
 *
 */
public class HelloJNI {
   static {
      System.load("/home/mohit/Work/JINT/PROFILING/JNILibrary/dist/hello.so"); // Load native library at runtime
                                   // hello.dll (Windows) or libhello.so (Unixes)
   }
 
   // Declare a native method sayHello() that receives nothing and returns void
   private native void sayHello();
   public native void passArray(int [] array);
   public native void passArrayAndCrash(int [] array);
   // Test Driver
   public static void main(String[] args) {
      HelloJNI simple=new HelloJNI();
      simple.sayHello();  // invoke the native method
      System.out.println("Passing array to C");
    int [] array = {1, 2, 3, 4, 5};
    simple.passArray(array);
    System.out.println("Passing array to C");
    simple.passArrayAndCrash(array);
    //System.gc();
    //simple.passArrayAndCrash(array);
    //System.gc();
   }
}