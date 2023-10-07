> report2.log
> top2.log
> process2.log

count=0
arr=()

while true
do
    arr+=(1 2 3 4 5 6 7 8 9 10)
    count=$(( count + 1 ))
    if ! (( count % 100000 ))
    then
        top -b -n 1 | head -n 12 | tail -9 >> top2.log
        top -b -n 1 | grep "$$" >> process2.log
        len=${#arr[@]}
        echo $len >> report2.log
        echo $len
    fi
done

