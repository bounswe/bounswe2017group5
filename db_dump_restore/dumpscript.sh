TODAY=`date +%F`

USER=ubuntu
IP=35.177.96.220
REMOTE_DUMPSCRIPT=/home/ubuntu/dbdump.sh

ssh -i $KEYFILE $USER@$IP $REMOTE_DUMPSCRIPT
scp -i $KEYFILE -r $USER@$IP:~/$TODAY.dump ./$TODAY.dump
while [ $? -ne 0 ]; do
    echo "Transfer disrupted, retrying in 10 seconds..."
    sleep 10
    scp -i $KEYFILE -r $USER@$IP:~/$TODAY.dump ./$TODAY.dump
done

echo "Dump successfully downloaded.";

