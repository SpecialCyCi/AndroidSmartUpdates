LOCAL_PATH:= $(call my-dir)  
include $(CLEAR_VARS) 

#bzlib module
LOCAL_MODULE := libbz
LOCAL_SRC_FILES := \
	blocksort.c \
	huffman.c \
	crctable.c \
	randtable.c \
	compress.c \
	decompress.c \
	bzlib.c
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS) 
#patcher module
LOCAL_MODULE := Patcher  
LOCAL_SRC_FILES := bspatch.c  
LOCAL_STATIC_LIBRARIES := libbz
LOCAL_C_INCLUDES += \$(JNI_H_INCLUDE) external/bzip2  
LOCAL_CFLAGS +=  
include $(BUILD_SHARED_LIBRARY)  