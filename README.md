# ArcCircleProgressBar - Flexible is a Power!

This is a flexible arc or circle progress bar.

[![](https://jitpack.io/v/xYinKio/ArcCircleProgressBar.svg)](https://jitpack.io/#xYinKio/ArcCircleProgressBar)


## Dependencies

1. Add repository in buld.gradle of progect or in settings.gradle

```
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add dependency in build.gradle of module
```
    dependencies {
	        implementation 'com.github.xYinKio:ArcCircleProgressBar:Tag'
	}
```


## What can it do

- circle progress bar
- arc progress bar
- set  width for both of them
- set colors for both of them
- set gradients for both of them
- set start and end angles for both of them

## Examples

![image](https://user-images.githubusercontent.com/48997650/156380567-d2c1e53a-c147-4d6a-b0a7-9e1405302b0a.png)
![image](https://user-images.githubusercontent.com/48997650/156380737-d1ce01cc-1acd-4a08-9846-64beb818e44f.png)
![image](https://user-images.githubusercontent.com/48997650/156380853-57d969e1-1240-45fe-af2a-ef250db62b37.png)
![image](https://user-images.githubusercontent.com/48997650/156380983-aa9d876d-70e0-4cd1-b4b4-5bcbf6699175.png)
![image](https://user-images.githubusercontent.com/48997650/156381157-85da475f-bdf2-417a-9650-1e39f3144871.png)
![image](https://user-images.githubusercontent.com/48997650/156381335-411f01bf-3e79-4c06-acda-324a28da83d8.png)
![image](https://user-images.githubusercontent.com/48997650/156381712-607e09a4-59a6-4e4a-9c76-24be3d1f71e0.png)
![image](https://user-images.githubusercontent.com/48997650/156381927-787ce7f6-33b4-4dd7-992d-e7d4a5817ff4.png)
![image](https://user-images.githubusercontent.com/48997650/156382181-85f03ba2-156b-49e1-858e-9c61ca908cfb.png)

## How to use

### In XML

#### List of attributes:

```
    app:progress="50"
    app:roundTips="true"

    app:indicatorWidth="20dp"
    app:indicatorStartAngle="0"
    app:indicatorEndAngle="270"
    app:indicatorColor="#00FDCB"
    app:indicatorShowGradient="true"
    app:indicatorGradientAngle="90"
    app:indicatorGradientWidth="100dp"
    app:indicatorGradientColors="@array/indicator_colors" // use color array in xml
    app:indicatorGradientPositions="@array/indicator_positions" // use typed array in xml
    app:indicatorGradientTileMode="mirror"


    app:canalStartAngle="0"
    app:canalEndAngle="270"
    app:canalWidth="10dp"
    app:canalColor="#3D03A6"
    app:canalShowGradient="false"
    app:canalGradientAngle="30"
    app:canalGradientWidth="100dp"
    app:canalGradientColors="@array/canal_colors"
    app:canalGradientPositions="@array/canal_positions"
    app:canalGradientTileMode="mirror" />
```

#### Color array

```
<resources>
    <array name="indicator_colors">
        <item>#D11212</item>
        <item>#08FF3D</item>
        <item>#0040FF</item>
    </array>
</resources>
```

#### Positions array

```
<resources>
    <array name="indicator_positions">
        <item>0.25</item>
        <item>0.5</item>
        <item>0.75</item>
    </array>
</resources>
```
### In Code

ArcCircleProgressBar has `indicator` and `canal` fields that you can use to access to base properties. If you want to use animations don\`t forget to call `invalidate()` 



