#!/bin/bash

./4_loop.sh&
pid1=$!

./4_loop.sh&
pid2=$!

./4_loop.sh&
pid3=$!

echo $pid1 $pid2
# renice +10 $pid1

cpulimit -p $pid1 -l 10 &
