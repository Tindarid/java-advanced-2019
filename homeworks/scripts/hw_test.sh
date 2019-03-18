#!/bin/bash

modulepath=../../lib:../../artifacts 
classpath=../java
usage="Usage: hw_test [number] [hard or easy]"

if [ $# != 2 ]
then
  echo $usage
  exit
fi

if [ $1 == 1 ]
then
  if [ $2 == easy ]
  then
    java -cp $classpath -p $modulepath -m info.kgeorgiy.java.advanced.walk Walk ru.ifmo.rain.valeyev.walk.RecursiveWalk
  elif [ $2 == hard ]
  then
    java -cp $classpath -p $modulepath -m info.kgeorgiy.java.advanced.walk RecursiveWalk ru.ifmo.rain.valeyev.walk.RecursiveWalk
  else
    echo $usage
  fi
else
  echo $usage
fi

