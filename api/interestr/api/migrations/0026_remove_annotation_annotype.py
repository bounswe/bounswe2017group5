# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-12-27 23:52
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0025_auto_20171227_1932'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='annotation',
            name='annotype',
        ),
    ]
