#!/bin/bash

echo "This utility combine nano, vi, link"
echo "Choose the number of programe you want to run"
echo "1) nano"
echo "2) vi"
echo "3) links"
echo "4) quit"
echo ""

while true
do
	read number
	if [[ $number -eq 1 ]]
	then
		nano
	fi
	
	if [[ $number -eq 2 ]]
	then
		vi
	fi
	
	if [[ $number -eq 3 ]]
	then
		links
	fi
	
	if [[ $number -eq 4 ]]
	then
		exit
	fi
done
