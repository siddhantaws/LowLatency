/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_mk_training_jnicrash_HelloJNI */

#ifndef _Included_org_mk_training_jnicrash_HelloJNI
#define _Included_org_mk_training_jnicrash_HelloJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_mk_training_jnicrash_HelloJNI
 * Method:    sayHello
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_sayHello
  (JNIEnv *, jobject);

/*
 * Class:     org_mk_training_jnicrash_HelloJNI
 * Method:    passArray
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_passArray
  (JNIEnv *, jobject, jintArray);

/*
 * Class:     org_mk_training_jnicrash_HelloJNI
 * Method:    passArrayAndCrash
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_org_mk_training_jnicrash_HelloJNI_passArrayAndCrash
  (JNIEnv *, jobject, jintArray);

#ifdef __cplusplus
}
#endif
#endif
