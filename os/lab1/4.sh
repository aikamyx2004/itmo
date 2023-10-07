#!/bin/bash

if [[ "$HOME" == "$PWD" ]];
then
	echo #HOME;
else
	echo "script ran not from home directory"
	exit 1;
fi


