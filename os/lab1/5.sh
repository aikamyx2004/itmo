#!/bin/bash

# у меня нет anaconda, я нашел похожий файл, но там вместо INFO было INF

regex="^\d{2}:\d{2}:\d{2},\d{3} INF "

path='/var/log/anaconda/syslog'

grep -P "$regex" $path > info.log
