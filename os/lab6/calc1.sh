#!/bin/bash

a=$(($RANDOM%10)) 
b=$(($RANDOM%10)) 
k=$(($RANDOM%10)) 
N=$((700+$RANDOM%100))

# f_0=a
# f_1=b
# f_i = f_{i-2}+kf_{i-1} 
for ((i=0;i<N;i++))
do
    temp=$(echo "$k*$b" | BC_LINE_LENGTH=0  bc)
    temp=$(echo "$temp+$a" | BC_LINE_LENGTH=0  bc)
    a=$b
    b=$temp
done
#echo $a
