#!/bin/bash

myjava=../java/ru/ifmo/rain/valeyev/implementor
goshajava=../java/info/kgeorgiy/java/advanced/implementor

echo Compiling
javac $goshajava/JarImpler.java
javac -cp ../java $myjava/JarImplementor.java

echo Packing in jar
jar cfe app.jar ru.ifmo.rain.valeyev.implementor.JarImplementor $myjava/*.class $goshajava/*.class

echo Deleting temp .class files
rm $myjava/*.class
rm $goshajava/*.class
