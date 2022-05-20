# Np Android sdk

# How to use

1.添加类库
```gradle
#根build.gradle文件配置仓库
 repositories {
      #...
        maven { url 'https://jitpack.io' }
    }

#需要依赖的module的build.gradle添加依赖
  implementation 'com.github.1006245347:NpDemo:v0.0.1'
``` # Np Android sdk

# How to use

1.添加类库
```gradle
#根build.gradle文件配置仓库
 repositories {
      #...
        maven { url 'https://jitpack.io' }
    }

#需要依赖的module的build.gradle添加依赖
  implementation 'com.github.1006245347:NpDemo:v0.0.1'
```

2.代码设置
```java
#添加网络请求
 <uses-permission android:name="android.permission.INTERNET" />
#初始化sdk,参数依次是Application上下文，设备唯一标识，应用主页面
NpServer.initSdk(getApplicationContext(),uniqueId,MainActivity.class);
```

```java
#统计app前台显示，在BaseActivity()监听

   @Override
    protected void onResume() {
        super.onResume();
        NpServer.stayOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NpServer.stayOnPause(this);
    }

```
```java
#初始化sdk,参数依次是Application上下文，设备唯一标识，应用主页面
NpServer.initSdk(getApplicationContext(),uniqueId,MainActivity.class);
```

```java
#上报事件key为video_source的点击事件，上报的事件的参数String类型
            JSONObject js = new JSONObject();
                try {
                    js.put("id", 3);
                    js.put("name", "tom");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NpServer.postEvent("video_source", js.toString()); //例1
                NpServer.postEvent("video_source", "1");//例2
```



2.代码设置
```java
#添加网络请求
 <uses-permission android:name="android.permission.INTERNET" />
#初始化sdk,参数依次是Application上下文，设备唯一标识，应用主页面
NpServer.initSdk(getApplicationContext(),uniqueId,MainActivity.class);
```

```java
#统计app前台显示，在BaseActivity()监听

   @Override
    protected void onResume() {
        super.onResume();
        NpServer.stayOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NpServer.stayOnPause(this);
    }

```
```java
#初始化sdk,参数依次是Application上下文，设备唯一标识，应用主页面
NpServer.initSdk(getApplicationContext(),uniqueId,MainActivity.class);
```

```java
#上报事件key为video_source的点击事件，上报的事件的参数String类型
JSONObject js = new JSONObject();
                try {
                    js.put("id", 3);
                    js.put("name", "tom");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NpServer.postEvent("video_source", js.toString()); //例1
                NpServer.postEvent("video_source", "1");//例2
```

