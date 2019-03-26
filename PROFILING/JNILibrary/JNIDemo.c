
#include <jni.h>
#include <stdio.h>
#include <string.h>
#include "HelloJNI.h"

JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_sayHello
        (JNIEnv *env, jobject obj)
{

    printf("\nHello World from C\n");

}

JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_passArray
  (JNIEnv *env, jobject obj, jintArray ptr) {

  int i;
  printf("Hello from JNI\n");
  jsize len = (*env)->GetArrayLength(env, ptr);
  jint *body = (*env)->GetIntArrayElements(env, ptr, 0);
  for (i=0; i < len; i++)
    printf("Hello from JNI - element: %d\n", body[i]);
  (*env)->ReleaseIntArrayElements(env, ptr, body, 0);
}

JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_passArrayAndCrash
  (JNIEnv *env, jobject obj, jintArray ptr) {

  int i;
  printf("Hello from JNI and crash\n");
  jsize len = (*env)->GetArrayLength(env, ptr);
  jint *body = (*env)->GetIntArrayElements(env, ptr, 0);
  for (i=0; i < 10; i++)
    printf("Hello from JNI - element: %d\n", body[i]);
  memcpy(*body,"hello javahello javahello javahello javahello javahello java",100);
  (*env)->ReleaseIntArrayElements(env, ptr, body, 0);
}