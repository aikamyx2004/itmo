#!/bin/bash

ps aux | tail -n +2 | sort -r -k 9 | head -1 | awk '{print $2}'
