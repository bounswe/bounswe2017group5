# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-31 15:22
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('website', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='group',
            name='is_private',
            field=models.BooleanField(default=0),
            preserve_default=False,
        ),
    ]