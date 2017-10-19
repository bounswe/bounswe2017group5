# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.postgres.fields import JSONField

class DataTemplateType:
    SHORT_TEXT = 0
    LONG_TEXT = 1
    SINGLE_CHOICE = 2
    MULTIPLE_CHOICE = 3
    DATE = 4
    NUMERIC = 5
    EMAIL = 6

class DataTemplate(models.Model):
    name = models.CharField(max_length=40)

    fields = JSONField()

    def __str__(self):
        return name
# Create your models here.
