![Canopus](https://jitpack.io/v/Oclemy/Canopus.svg)

# What is Canopus?
Canopus is a User Management library utilizing my own personal server to store users. I have created it so that I use it my personal projects and for my students.

## Motivation
When am creating my own projects or those I develop when teaching a course, they tend to need user management features like login and registeration functionalities. I don't want to
be spending alot of time duplicating those features in each and every project. So I have decided to host it right here in github so that I can easily install it newer projects. This
not only saves me time but also gives me a way to maintain, upgrade and add features to older projects at once.

## NB/=
This library is for personal use and also for my students. Am hosting everything in my server. However you can adjust the code to meet your specific scenario in your projects.

## Technologies Used

1. Java Programming Language
2. Retrofit
3. PHP
4. MySQL Database
5. MVVM

## Installation
In the root build.gradle:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Then dependency:

```groovy
implementation 'com.github.Oclemy:Canopus:1.0.1'
```
