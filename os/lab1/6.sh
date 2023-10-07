#!/bin/bash
	
path="/var/log/anaconda/X.log"


#logs start from the 14th line
sed -n '14,$s/(WW)/Warning:/p' "$path" > full.log

sed -n '14,$s/(II)/Information:/p' "$path" >> full.log

cat full.log
