#!/bin/bash

[ -d ~/.trash ] || {
  echo "Error: Can't find trash folder."
  exit 1
}

[ -f ~/trash.log ] || {
  echo "Error: Can't find trash log file."
  exit 1
}


for line in $(grep $1 ~/trash.log | awk '{print $1 ":" $6}')
do
  filename=$(echo ${line} | awk -F ':' '{print $1}')
  short_filename=$(basename $filename)
  if [ $1 != $short_filename ]
  then
    continue
  fi
  
  trash_name=$(echo ${line} | awk -F ':' '{print $2}')
  if [[ ! -f $trash_name ]]
  then
  	continue
  fi
  
  answer=0
  
  while true; do
    read -r -p "Do you want to restore $filename? (Y/N): " r_answer
    case $r_answer in
        [Yy]* ) answer=1; break;;
        [Nn]* ) answer=0; break;;
        * ) echo "Please answer Y or N.";;
    esac
  done  
  
  if [ $answer -eq 0 ]
  then
    continue;
  fi
  
  folder=$(dirname $filename)
  if [ -d $folder ]; then
    filename_temp=$filename

    while [ -e $filename_temp ]
    do
      read -r -p "The file: $filename_temp is already exists. Enter a new name: " new_name
      filename_temp=$folder/$new_name
    done
	
    ln $trash_name $filename_temp
  else
    echo "There is no $folder directory"
    echo "file will be in home"
    
  	filename_temp=~/$short_filename
  	while [ -e $filename_temp ]
    do
      read -r -p "The file: $filename_temp is already exists. Enter a new name: " new_name
      filename_temp=~/$new_name
    done
  	ln $trash_name $filename_temp
  fi 
  rm $trash_name
done

