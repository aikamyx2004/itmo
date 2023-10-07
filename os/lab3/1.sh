#!/bin/bash

d=$(date '+%F_%T')

mkdir ~/test && echo "catalog test was created successfully" >> ~/report && touch ~/test/$d.txt

ping -c 1 www.net_nikogo.ru || echo "error: www.net_nikogo.ru is not available" >> ~/report	
