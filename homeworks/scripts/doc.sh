#!/bin/bash

myjava=ru/ifmo/rain/valeyev/implementor
testjava=../modules/info.kgeorgiy.java.advanced.implementor/info/kgeorgiy/java/advanced/implementor

javadoc -d doc -private $myjava/Implementor.java $myjava/JarImplementor.java $testjava/Impler.java $testjava/ImplerException.java $testjava/JarImpler.java
