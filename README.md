# baserecycleradapter
平时开发中整理出来的一些常用的封装和解决方案
# 用法
- 在项目根目录下的build.gradle中加入
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- 添加依赖关系
```
dependencies {
	        compile 'com.github.wb1992321.LibrarysDemo:baserecycleradapter:1.0.0'
	}
```

# 用法
- 直接继承BaseAdapter，BaseEmptyAdapter，SwipeAdapter就行了
> 后面SwipeAdapter是继承自BaseEmptyAdapter
- 加入了SwipeLayout
> 关于SwipeLayout可以参考[这里](https://github.com/daimajia/AndroidSwipeLayout).只是抽出来了SwipeLayout

# 计划
- 继续增加日常开发中需要的功能和场景
- 实现更多默认的适配器
