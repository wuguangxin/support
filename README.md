# support
项目框架

gradle配置

1、Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
2、Add the dependency

  dependencies {
	        compile 'com.github.wuguangxin:support:2.0.0'
	}
