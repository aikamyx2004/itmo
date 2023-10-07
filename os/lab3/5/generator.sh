#!/bin/bash


$(mkfifo channel)

echo $$ >> channel

while true
do
    read s
    echo "$s" >> channel
done
