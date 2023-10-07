#!bin/bash
r=9414531
l=1780000


while [[ r -gt l ]]
do
	dmesg_was=$(dmesg | grep "Out of memory:" | wc -l)
	m=$(((l+r)/2))
	./launch2.2.sh $m
	
	read a
	if [[ $dmesg_was != $(dmesg | grep "Out of memory:" | wc -l) ]]
	then
		r=$m
	else
		l=$m
	fi
	echo "a $l $r"
done

echo $l

echo $r
