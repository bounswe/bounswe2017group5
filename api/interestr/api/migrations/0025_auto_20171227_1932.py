# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-12-27 19:32
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0024_annotation'),
    ]

    operations = [
        migrations.AlterField(
            model_name='annotation',
            name='user',
            field=models.ForeignKey(default=None, null=True, on_delete=django.db.models.deletion.SET_NULL, related_name='annotations', to=settings.AUTH_USER_MODEL),
        ),
    ]
