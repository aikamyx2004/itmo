#!/bin/bash 

mkdir -p ~/backups
cd ~/backups

folder=$(ls | sort -t "-" -n -k2 -k3 -k4 | tail -1)

date_last=$(echo $folder | awk -F - '{print $2 "-" $3 "-" $4}')

last=$(date -d $date_last '+%s')

now=$(date '+%s')

diff=$(($now - $last))


date_now=$(date +'%Y-%m-%d')
    
if [[ $diff -gt $((7*24*60*60)) ]] 
then
    backup_folder="Backup-$date_now"
    mkdir $backup_folder
    
	report_file=$backup_folder/"backup-report"
    echo "************Backup started************" >> $report_file
	    
    echo "The folder: $(realpath $backup_folder) was created at $date_now" >> $report_file
  
    cp $HOME/source/* $backup_folder/
    echo "The files: $(ls -hm $backup_folder) was copied" >> $report_file

    echo "*************Backup ended*************" >> $report_file
    
else
	backup_folder=$folder
	report_file=$backup_folder/"backup-report"
	echo "************Backup saved folder started************" >> $report_file
  	for filename in ~/source/*
  	do
		name=$(basename $filename)
		backup_file=$backup_folder/$name
		
		if [[ -f $backup_file ]]
		then
      		if ! cmp -s "$filename" "$backup_file"
      		then
        		mv -f $backup_file $backup_file.$date_now
    			cp $filename $backup_file
        		modified_files+="$name, "
		    fi
		else
			cp $filename $backup_file
			added_files+="$name, "
		fi
  	done
  	echo "Added files: $added_files" >> $report_file
  	echo "Modified files: $modified_files" >> $report_file
  	echo "************Backup saved folder started************" >> $report_file
	
fi

