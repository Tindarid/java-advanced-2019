#!/bin/bash

modulepath=../../lib:../../artifacts 
classpath=../java
usage="Usage: hw_test [number] [hard or easy]"
my=ru.ifmo.rain.valeyev
gosha=info.kgeorgiy.java.advanced

if [ $# != 2 ]
then
  echo $usage
  exit
fi

if [ $2 != easy ]
then
  if [ $2 != hard ]
  then
    echo $usage
    exit
  fi
fi

if [ $1 == 1 ]
then
  if [ $2 == easy ]
  then
    java -cp $classpath -p $modulepath -m $gosha.walk Walk $my.walk.RecursiveWalk
  else
    java -cp $classpath -p $modulepath -m $gosha.walk RecursiveWalk $my.walk.RecursiveWalk
  fi
elif [ $1 == 2 ]
then
  if [ $2 == easy ]
  then
    java -cp $classpath -p $modulepath -m $gosha.arrayset SortedSet $my.arrayset.ArraySet
  else
    java -cp $classpath -p $modulepath -m $gosha.arrayset NavigableSet $my.arrayset.ArraySet
  fi
elif [ $1 == 3 ]
then
  if [ $2 == easy ]
  then
    java -cp $classpath -p $modulepath -m $gosha.student StudentQuery $my.student.StudentDB
  else
    java -cp $classpath -p $modulepath -m $gosha.student StudentGroupQuery $my.student.StudentDB
  fi
elif [ $1 == 4 ]
then
  if [ $2 == easy ]
  then
    java -cp $classpath -p $modulepath -m $gosha.implementor interface $my.implementor.Implementor
  else
    java -cp $classpath -p $modulepath -m $gosha.implementor class $my.implementor.Implementor
  fi
else
  echo $usage
fi

