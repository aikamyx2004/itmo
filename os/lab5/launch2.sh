N=1780000
K=30

for ((i=0; i<K; i++))
do
	sleep 1
    ./newmem.bash $N &
done
