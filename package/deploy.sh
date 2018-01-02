VERSION=0.1

ENV_DIR=env

ENV_PIP=$ENV_DIR/bin/pip
PSQL="sudo -u postgres -i psql"
PIP="sudo -u ubuntu -i $ENV_PIP"

VIRTUALENV_CMD="/home/ubuntu/.local/bin/virtualenv"

VIRTUALENV="sudo -u ubuntu -i $VIRTUALENV_CMD"

HOST_ADDRESS=$1

clean_up() {
  rm -rf interestr
  $PSQL << EOF
DROP USER bounswe2017group5;
DROP DATABASE interestr_prod;
EOF

}

if [ $(id -u) != 0 ]; then
  echo "Run the script as root" >&2
  exit 1
fi

trap clean_up SIGTERM

echo "Creating virtual environment"
$VIRTUALENV env

echo "Installing requirements..."
$PIP install -r requirements.txt

echo "Creating django-admin project..."
sudo -u ubuntu -i django-admin startproject interestr

echo "Installing Interestr packages..."
$PIP install interestr-api-$VERSION.tar.gz
$PIP install interestr-website-$VERSION.tar.gz

echo "Patching settings file..."
patch -bf ./interestr/interestr/settings.py < settings.patch
patch -bf ./interestr/interestr/settings.py << EOF
28c28
< ALLOWED_HOSTS = ['localhost', '127.0.0.1']
---
> ALLOWED_HOSTS = ['localhost', '127.0.0.1', '$HOST_ADDRESS']
EOF

echo "Adding URLS..."
cp -f urls.py interestr/interestr/urls.py

echo "Setting up database..."

$PSQL << EOF
CREATE USER bounswe2017group5 WITH PASSWORD '1234';
ALTER USER bounswe2017group5 WITH CREATEDB;
CREATE DATABASE interestr_prod;
EOF

echo "Adding apache config file..."
cp siteconf.conf /etc/apache2/sites-available/001-django.conf
ln -sf /etc/apache2/sites-available/001-django.conf /etc/apache2/sites-enabled/001-django.conf
chmod 777 /etc/apache2/sites-enabled/001-django.conf
rm -f /etc/apache2/sites-enabled/000-default.conf

echo "Migrations..."
python interestr/manage.py migrate > /dev/null
echo "...and stuff..."
sudo -u ubuntu -i python interestr/manage.py collectstatic --noinput > /dev/null

echo "Restarting apache2"
service apache2 restart > /dev/null

echo "All done!"

