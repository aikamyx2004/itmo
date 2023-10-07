#!/bin/bash

operation=+
accumulator=1

(tail -f channel) | 
{
	read gen_pid;
	while true
	do
		read LINE;
		if [[ $LINE = "+" || $LINE = "*" || $LINE = "-" || $LINE = "/" ]]
		then
			operation=$LINE
		elif [[ $LINE = QUIT ]]
		then
			echo "exit"
			kill $gen_pid
			exit 0
		elif [[ $LINE =~ [0-9]+ ]]
		then
			accumulator=$(echo "$accumulator $operation $LINE" | bc -l)	
		else
			echo "line '$LINE' is not +, * or number"
			echo "exit"
			kill $gen_pid
			exit 0
		fi
		echo $accumulator
	done
}
