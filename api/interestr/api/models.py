# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth import models as auth_models
from django.utils import timezone

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
    description = models.TextField(blank=True, default='')
    members = models.ManyToManyField(auth_models.User)
    picture = models.ImageField(blank=True, null=True)#ToDo add default picture
    def __str__(self):
        return self.name

    def size(self):
        return self.members.count()
    

class Post(BaseModel):
    owner = models.ForeignKey(auth_models.User, related_name="posts",
     default = None, null=True)
    text = models.TextField(default = '')
    content_url = models.URLField(default = '')
    group = models.ForeignKey('api.Group', related_name='posts', 
        on_delete=models.CASCADE,	default = None, null=True)
    data_template = models.ForeignKey('data_templates.DataTemplate', related_name='posts',
        default = None, null=True)

    def __str__(self):
        return self.text

# Models END
