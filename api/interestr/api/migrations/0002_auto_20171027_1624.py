# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-27 16:24
from __future__ import unicode_literals

from django.db import migrations, models
import django.db.models.deletion
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='post',
            name='contenUrl',
            field=models.URLField(default=''),
        ),
        migrations.AddField(
            model_name='post',
            name='group',
            field=models.ForeignKey(default=None, on_delete=django.db.models.deletion.CASCADE, related_name='posts', to='api.Group'),
        ),
        migrations.AddField(
            model_name='post',
            name='published_date',
            field=models.DateTimeField(default=django.utils.timezone.now),
        ),
        migrations.AddField(
            model_name='post',
            name='text',
            field=models.TextField(default=''),
        ),
    ]
