#!/bin/bash 

[ -d ~/backups ] || {
    echo "Error: Can't find backup folder."
    exit 1
}

mkdir -p ~/restore

cd ~/backups
folder=$(ls | sort -t "-" -n -k2 -k3 -k4 | tail -1)

[ -d $folder ] || {
    echo "Error: No backup folder."
    exit
}

cd $folder
for backup_file in $(ls | grep -v -P ".\d{4}-\d{2}-\d{2}$")
do

    [ -e ~/restore/$backup_file ] && echo "Overwriting file $backup_file"
      
    cp $backup_file ~/restore/
done

