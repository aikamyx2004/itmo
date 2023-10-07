#!/bin/bash

for ((i=0;i<$1;i++))
do
	bash calc1.sh & 
	pids+=" $!"
done

for pid in $pids
do
  wait $pid
done

