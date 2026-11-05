cmake_minimum_required(VERSION 3.22.1)
project("plamo_translate")

# Placeholder for llama.cpp integration.
# When ready:
# 1. Add llama.cpp as submodule or copy sources under cpp/llama.cpp
# 2. Uncomment and adjust the following

# set(LLAMA_DIR ${CMAKE_CURRENT_SOURCE_DIR}/llama.cpp)
# add_subdirectory(${LLAMA_DIR} llama_build)

add_library(plamo_jni SHARED
    plamo_jni.cpp
)

find_library(log-lib log)

target_link_libraries(plamo_jni
    ${log-lib}
    # llama  # enable when llama.cpp is linked
    android
)

target_compile_options(plamo_jni PRIVATE -O3 -ffast-math)
