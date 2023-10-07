#!/bin/bash

all_lines="";
while true
do
	read line;
	if [ "$line" = "q" ]; then
		echo $all_lines;
		break;
	fi
	all_lines+="$line ";
done
