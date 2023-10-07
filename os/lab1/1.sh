#!/bin/bash

mx=$1;

if [[ "$mx" -lt "$2" ]]; then
    mx=$2;
fi

if [[ "$mx" -lt "$3" ]]; then
    mx=$3;
fi

echo $mx;
