#!/bin/bash

path='/etc/passwd'

sort -t: -k3n $path | awk -F: '{print $1, $3}' 

