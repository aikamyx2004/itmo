#!/bin/bash

ps aux | grep -P "^$USER" | wc -l > 1.txt 
ps aux | grep -P "^$USER" | awk '{print $2 ":" $11}' >> 1.txt
