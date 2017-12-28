TODAY=`date +%F`

USER=ubuntu
IP=35.177.96.220
REMOTE_RESTORESCRIPT=/home/ubuntu/dbrestore.sh

DUMPFILE=$1

echo "Sending backup file..."
scp -i $KEYFILE $DUMPFILE $USER@$IP:/home/ubuntu/backup.dump

echo "Restoring database..."
ssh -i $KEYFILE $USER@$IP $REMOTE_RESTORESCRIPT /home/ubuntu/backup.dump

echo "Database restored..."