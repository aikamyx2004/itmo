#!/bin/bash

>data_parallel.txt
for ((i=1;i<=20;i++))
do
	echo "$i"
	echo "$i---------------" >> data_parallel.txt
	for ((j=0;j<10;j++))
	do
		/usr/bin/time -a -f "%e" -o data_parallel.txt  bash ~/lab6/run_parallel.sh $i
	done
done

