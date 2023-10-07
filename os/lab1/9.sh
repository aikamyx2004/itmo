#!/bin/bash

path='/var/log/'


find $path -name '*.log' -type f | xargs cat | wc -l


