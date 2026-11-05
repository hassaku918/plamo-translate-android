#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "PLaMoJNI"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

// Placeholder JNI bridge for future llama.cpp integration.
// Replace load/unload/generate with real llama.cpp calls.

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_hassaku_plamotranslate_data_inference_LlamaCppEngine_nativeLoadModel(
        JNIEnv *env, jobject /*thiz*/, jstring modelPath) {
    const char *path = env->GetStringUTFChars(modelPath, nullptr);
    LOGI("nativeLoadModel: %s (stub)", path);
    env->ReleaseStringUTFChars(modelPath, path);
    // TODO: llama_model_load_from_file / llama_init_from_model
    return JNI_FALSE; // not actually loaded yet
}

JNIEXPORT void JNICALL
Java_com_hassaku_plamotranslate_data_inference_LlamaCppEngine_nativeUnloadModel(
        JNIEnv * /*env*/, jobject /*thiz*/) {
    LOGI("nativeUnloadModel (stub)");
    // TODO: free llama context/model
}

JNIEXPORT jstring JNICALL
Java_com_hassaku_plamotranslate_data_inference_LlamaCppEngine_nativeGenerate(
        JNIEnv *env, jobject /*thiz*/, jstring prompt, jint maxTokens) {
    const char *p = env->GetStringUTFChars(prompt, nullptr);
    LOGI("nativeGenerate maxTokens=%d (stub)", maxTokens);
    env->ReleaseStringUTFChars(prompt, p);

    // Return empty to signal stub
    return env->NewStringUTF("");
}

JNIEXPORT jboolean JNICALL
Java_com_hassaku_plamotranslate_data_inference_LlamaCppEngine_nativeIsLoaded(
        JNIEnv * /*env*/, jobject /*thiz*/) {
    return JNI_FALSE;
}

} // extern "C"
