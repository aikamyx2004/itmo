#!/bin/bash

echo $$ > pid

A=1
OPERAND=2

usr1()
{
	A=$(( A + OPERAND ))
}

usr2()
{
	A=$(( A * OPERAND ))
}

term()
{
	echo "STOP"
	exit
}

plus()
{
	OPERAND=$(( OPERAND + 1 ))
}

minus()
{
	OPERAND=$(( OPERAND - 1 ))
}

trap 'usr1' USR1
trap 'usr2' USR2
trap 'term' SIGTERM
trap 'plus' SIGPROF
trap 'minus' SIGIO

while true; do
	echo $A
	sleep 1
done
