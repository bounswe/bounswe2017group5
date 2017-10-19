# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

class String(models.Model):
    name = models.CharField(max_length=50)
    value = models.TextField()

    def __str__(self):
        return self.name + " : " + self.value
