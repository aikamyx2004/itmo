#!/bin/bash

touch data.txt

for pid in $(ps aux | awk '{print $2}')
do
	if [[ -f /proc/$pid/status && -f /proc/$pid/sched ]]
	then 
		ppid=$(grep -P "^PPid"  /proc/$pid/status | awk '{print $2}')
		ser=$(grep -P "^se.sum_exec_runtime" /proc/$pid/sched | awk '{print $3}')
		nrs=$(grep -P "^nr_switches" /proc/$pid/sched | awk '{print $3}')
		if [[ $nrs -ne "" ]]
		then
			avg=$(echo $ser/$nrs | bc -l)
		fi 
		echo $pid $ppid $avg >> data.txt
	fi
done

sort -n -k2 data.txt | awk '{print "ProcessID="$1" : Parent_ProcessID="$2" : Average_Running_Time="$3"" }' > 4.txt 

rm data.txt

