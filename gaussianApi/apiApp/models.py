# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

class Tag(models.Model):
	created = models.DateTimeField(auto_now_add=True)
	name = models.CharField(max_length=30, blank=True, default='')

	class Meta:
		ordering = ('created',)

# Create your models here.
