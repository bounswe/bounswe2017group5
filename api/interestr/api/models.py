# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth import models as auth_models
from django.utils import timezone
from data-templates import DataTemplate 

# BaseModel, do not touch this...
class BaseModel(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)

    class Meta:
        abstract = True
        ordering = ('created', )

# Models START

class Group(BaseModel):
    name = models.CharField(max_length=40)

    def __str__(self):
        return self.name

class Post(BaseModel):
    owner = models.ForeignKey(auth_models.User, related_name="posts")
    text = models.TextField(default = '')
    content_url = models.URLField(default = '')
    group = models.ForeignKey('api.Group', related_name='posts', on_delete=models.CASCADE,
    	default = None)
    published_date = models.DateTimeField(default = timezone.now)


    def publish(self):
    	self.published_date = timezone.now()
    	self.save()

    def edit(text, content):
    	self.text = text
    	self.contentUrl = content
       	publish(save)


    def __str__(self):
        return self.text

# Models END

