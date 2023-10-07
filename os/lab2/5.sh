#!/bin/bash

bash 4.sh

>5.txt

p_ppid=0
sum=0
cnt=0

while read i
do
	ppid=$(echo $i | awk '{print substr($3, 1+index($3, "="))}')
	avg=$(echo $i | awk '{print substr($5, 1+index($5, "="))}')
		
	if [[ $ppid -eq $p_ppid ]]
	then
		sum=$(echo $sum + $avg | bc)
		cnt=$(echo $cnt + 1 | bc)
	else
		echo "Average_Running_Children_of_ParentID=$p_ppid is " $(echo $sum / $cnt | bc -l) >> 5.txt
		cnt=1
		sum=$avg
		p_ppid=$ppid
	fi
	echo $i >> 5.txt
done < 4.txt

echo "Average_Running_Children_of_ParentID=$p_ppid is " $(echo $sum / $cnt | bc -l) >> 5.txt
