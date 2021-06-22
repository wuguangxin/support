# support
android项目框架支持库

gradle配置

1、在项目根目录的 build.gradle 中添加仓库地址：

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  
2、在 dependency 中添加依赖项

  	dependencies {
		...
	        implementation com.github.wuguangxin:support:2.0.3
	}
