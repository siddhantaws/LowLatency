#!/bin/bash
for (( c=1; c<=30; c++ ))
do
   taskset -c 1,2 ./runTest.sh $1 $2
done
