# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.postgres.fields import JSONField
from django.contrib.auth import models as auth_models

from api import models as core_models

class DataTemplateType:
    SHORT_TEXT = 0
    LONG_TEXT = 1
    SINGLE_CHOICE = 2
    MULTIPLE_CHOICE = 3
    DATE = 4
    NUMERIC = 5
    EMAIL = 6

class DataTemplate(core_models.BaseModel):
    name = models.CharField(max_length=40)

    group = models.ForeignKey(core_models.Group, on_delete=models.SET_NULL, null=True)
    user = models.ForeignKey(auth_models.User, on_delete=models.SET_NULL, null=True)

    fields = JSONField(default=None, null=True)

    def usage_count(self):
        return length(self.posts.all())

    def __str__(self):
        return self.name
# Create your models here.
