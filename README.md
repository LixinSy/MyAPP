#### 1、介绍
一个花草识别Android App。
识别模型通过TensorFlow和迁移训练算出，训练环境是python3 + NVIDIA GeForce GTX 1050 Ti，计算力不足所以使用Google已有的模型进行迁移训练。
训练模型的工程[在这](https://github.com/fixkme/incep/tree/master)
#### 2、功能
使用手机相机对花草拍照，识别出花草名字，然后通过名字在百度百科爬取信息，最后展示这个花草的信息以及列出更多的相关花草图片。
