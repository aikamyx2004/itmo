#!/bin/bash

awk '{  print $1, $2, $3, \
		(substr($4, 2, 1) == '4' ? 4 : 0) + int(substr($4, 3, 1))
	}' students.txt
