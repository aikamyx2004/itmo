#!/bin/bash

regex='\w+@\w+\.\w+'
path="/etc"
echo $(grep -aoh -r -P "$regex" "$path" | sort -u) | sed 's/ /, /g' > emails.lst
