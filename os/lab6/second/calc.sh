#!/bin/bash
temp="data/$1"
cat "data/$1.txt" > $temp
for n in $(cat $temp)
do
	echo $((2*n)) >> $temp
done
