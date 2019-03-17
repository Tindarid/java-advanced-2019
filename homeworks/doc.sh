#!/bin/bash

javadoc --module-source-path ./modules --module-path ../lib:../artifacts -d out -private --module ru.ifmo.rain.valeyev.implementor
