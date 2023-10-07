N=$1
K=30

for ((i=0; i<K; i++))
do
    ./newmem.bash $N &
done
