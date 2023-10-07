#!/bin/bash

man --nh bash | sed 's/[[:punct:]]/ /g' | tr -s ' ' '\n' | grep -o -P "\w{4}\w*$" | sort | uniq -c | sort -k1 -nr | awk '{print $2}' | head -3

