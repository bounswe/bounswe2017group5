# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-11-29 23:47
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('api', '0018_vote'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='vote',
            options={},
        ),
        migrations.AlterUniqueTogether(
            name='vote',
            unique_together=set([('owner', 'post')]),
        ),
    ]
