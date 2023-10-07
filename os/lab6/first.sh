#!/bin/bash

>data1.txt
for ((i=1;i<=20;i++))
do
	echo "$i"
	echo "$i---------------" >> data1.txt
	for ((j=0;j<10;j++))
	do
		/usr/bin/time -a -f "%e" -o data1.txt  bash ~/lab6/run1.sh $i
	done
done

