-----------------------------------------------------------------------------------------------
C/C++ 代码 生成dll
推荐用MinGW。
先写个头文件吧：
test.h:
C代码  

	1. #ifndef MINGW_DLL_H__  
	2. #define MINGW_DLL_H__  
	3. int add(int a,int b);  
	4. #endif  

 
 
接着写C文件：
test.c:
C代码  

	1. #include <stdio.h>  
	2. #include "test.h"  
	3. int add(int a,int b){  
	4.   printf("\n");  
	5.   printf("dll function add() called\n");  
	6.   return a+b;  
	7. }  
用MinGW将C编译成dll，命令如下：
C代码  

	1. gcc -Wall -shared test.c -o test.dll  
	2编译test
		
		编译命令如下：
		g++ test.cpp -o test
		输出结果为：test.exe
-----------------------------------------------------------------------------------------------

Jnative   需要jar JNative.jar
jna 	    需要jar jna.jar
jni		    需要jacob-1.17-M2-x86.dll  jacob-1.17-M2-x64.dll  jacob-1.17.jar
JACOB      
 调用本地方法