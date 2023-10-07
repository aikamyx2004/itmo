#!/bin/bash

for ((i=0;i<$1;i++))
do
	bash calc.sh $i & 
	pids+=" $!"
done

for pid in $pids
do
  wait $pid
done

