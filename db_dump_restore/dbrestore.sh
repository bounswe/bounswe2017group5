BACKUP=backup.dump

export LANGUAGE=en_US.UTF-8
export LC_ALL=en_US.UTF-8

echo "Restoring database...";
sudo -u postgres -i dropdb interestr;
sudo -u postgres -i createdb interestr;
sudo -u postgres -i pg_restore --verbose --clean --no-acl --no-owner -h localhost -U bounswe2017group5 -d interestr /home/ubuntu/$BACKUP;
cat /home/ubuntu/$BACKUP | sudo -u postgres -i psql interestr
tput setaf 2;
echo "🍻  done deal!"
tput sgr0;
