# ArcCircleProgressBar - Flexibility is Power!

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
- set simple/colored/gradient shadows

## Examples
### Arc colored shadow
![изображение](https://user-images.githubusercontent.com/48997650/179523645-b7a4f9cf-3f9c-41e9-93ad-87ee0a064a52.png)
### Arc simple shadow
![изображение](https://user-images.githubusercontent.com/48997650/179523768-419eb254-bf94-44b7-94fb-951608b4eb09.png)
### Arc gradient indicator
![изображение](https://user-images.githubusercontent.com/48997650/179523819-438b4d02-f130-4a25-b25f-1c5c86cd2ae3.png)
### Arc gradient shadow
![изображение](https://user-images.githubusercontent.com/48997650/179523868-287f85ef-6e47-4d75-b219-fe89ed23f10c.png)
### Circle gradient shadow
![изображение](https://user-images.githubusercontent.com/48997650/179523906-01fa10da-c3e4-49b5-8fc1-4f48a815c241.png)


## How to use

### In XML

#### List of attributes:

Past indicator or canal instead of [prefix]


 - progress				
 - roundTips				
 - shadowLeft				
 - shadowTop				
 - shadowRight				
 - shadowBottom				
 - progressScale				
 - indicatorWidth				
 - canalWidth				
 - [prefix]StartAngle			
 - [prefix]EndAngle			
 - [prefix]Color				
 - [prefix]DrawGradient			
 - [prefix]GradientAngle			
 - [prefix]GradientWidth			
 - [prefix]GradientColors			
 - [prefix]GradientPositions		
 - [prefix]GradientTileMode		
 - [prefix]HasShadow			
 - [prefix]ShadowColor			
 - [prefix]ShadowDrawGradient		
 - [prefix]ShadowGradientAngle		
 - [prefix]ShadowGradientWidth		
 - [prefix]ShadowGradientColors		
 - [prefix]ShadowGradientPositions	
 - [prefix]ShadowBlurValue		
 - [prefix]ShadowGradientTileMode		

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



