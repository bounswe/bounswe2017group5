# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-31 21:14
from __future__ import unicode_literals

import django.contrib.postgres.fields.jsonb
from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0007_remove_post_content_url'),
    ]

    operations = [
        migrations.AddField(
            model_name='post',
            name='data',
            field=django.contrib.postgres.fields.jsonb.JSONField(default=''),
        ),
    ]
