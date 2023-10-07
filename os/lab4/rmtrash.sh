#!/bin/bash

[ -d $1 ] && exit
[ -f $PWD/$1 ] || {
	echo "no such file"
 	exit
 }

mkdir -p ~/.trash

cnt=0

name=$(basename $1)
while true
do
	path=~/.trash/${name}_${cnt}
	if [ -f "$path" ];
	then
    	((cnt=cnt + 1))
    else
    	break
    fi
done

ln $1 $path 
rm $1

echo "$PWD/$1 file deleted, new link $path" >> ~/trash.log
