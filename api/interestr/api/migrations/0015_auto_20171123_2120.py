# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-11-23 21:20
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0014_auto_20171119_1159'),
    ]

    operations = [
        migrations.AlterField(
            model_name='group',
            name='members',
            field=models.ManyToManyField(blank=True, related_name='joined_groups', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AlterField(
            model_name='group',
            name='moderators',
            field=models.ManyToManyField(blank=True, related_name='moderated_groups', to=settings.AUTH_USER_MODEL),
        ),
        migrations.AlterField(
            model_name='group',
            name='tags',
            field=models.ManyToManyField(blank=True, related_name='groups', to='api.Tag'),
        ),
    ]