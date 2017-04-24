# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

class Group(models.Model):
	created = models.DateTimeField(auto_now_add=True)
	name = models.CharField(max_length=30, blank=True, default='')
	# members field should be added as a one2many relation between group and user
	isPublic = models.BooleanField(default = True)
	description = models.TextField();
	avatar = models.ImageField(upload_to=None, height_field=None, width_field=None, max_length=100, **options)
	location_lat = models.CharField(max_length = 10, blank=True)
	location_lon = models.CharField(max_length = 10, blank=True)
	#tags field should be added as a one2many relation between group and tags


	class Meta:
		ordering = ('created',)



# Create your models here.
