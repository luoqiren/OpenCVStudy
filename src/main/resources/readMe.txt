思路如下：
1. 想要获取客户正面身份证以及客户当前照片进行人脸比对识别；
2. 使用opencv进行人脸截取以及人脸比对  ref:https://blog.csdn.net/qq_37598011/category_8619305.html；
3. 使用Thumbnailator 进行图片高质量压缩 ref:https://www.cnblogs.com/miskis/p/5500822.html；
4. 未完成人脸照片采集、活体采集 20211025；
5. 测试代码在测试目录；

----------------------------------------------------------------------
其中针对opencv启动命令要使用
-Djava.library.path=D:\Program_Files\openCV\opencv\build\java\x86 
或者
-Djava.library.path=D:\Program_Files\openCV\opencv\build\java\x64
-----------------------------------------------------------------------
针对Thumbnailator启动命令要增加JVM内存，至少使用 -Xmx512m 得以读取并压缩5M左右大小的图片
