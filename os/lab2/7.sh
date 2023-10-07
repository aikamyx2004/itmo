#!bin/bash

for pid in $(ps aux | awk '{print $2}')
do
	if [[ -f /proc/$pid/io ]]
	then
		begin[$pid]=$(grep -s "read_bytes" /proc/$pid/io | awk '{print $2}')
	fi
done

sleep 60

>data.txt
for pid in $(ps aux | awk '{print $2}')
do
	now=0
	if [[ -f /proc/$pid/io ]]
	then
		now=$(grep -s "read_bytes" /proc/$pid/io | awk '{print $2}')
	fi
	
	old=0
	if [[ ${begin[$pid]} != "" ]]
	then
		old=${begin[$pid]}
	fi
	
	d=$(echo $now - $old | bc)
	cmd=""
	if [[ -f /proc/$pid/cmdline ]]
	then
		cmd=$(cat /proc/$pid/cmdline | tr -d '\0')
	fi
	echo "$pid : $d : $cmd" >> data.txt
done

sort -nr -k3 data.txt | head -n3

rm data.txt
