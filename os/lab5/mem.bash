> report.log
> top.log
> process.log
count=0
arr=()

while true
do
    arr+=(1 2 3 4 5 6 7 8 9 10)
    count=$(( count + 1 ))
    if ! (( count % 100000 ))
    then
        top -b -n 1 | head -n 12 | tail -9 >> top.log
        top -b -n 1 | grep "$$" >> process.log
        len=${#arr[@]}
        echo $len >> report.log
        echo $len
    fi
done

