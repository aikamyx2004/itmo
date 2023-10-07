#!bin/bash

max=0
ans=-1

for pid in $(ps aux | awk '{print $2}')
do
	if [[ -f /proc/$pid/status ]]
	then
		m=$(grep -s "VmHWM" /proc/$pid/status | awk '{print $2}')
		if [[ $m -gt $max ]]
		then
			ans=$pid
			max=$m
		fi
	fi
done

echo $ans
