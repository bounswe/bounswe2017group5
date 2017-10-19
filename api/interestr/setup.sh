#!/usr/bin/sh
#
virtualenv env # create a new environment for the project
source env/bin/activate # activate the env
pip install django djangorestframework psycopg2 # install the deps
createuser bounswe2017group5 && createdb interestr # create db and its user
python manage.py migrate # migrate the db
