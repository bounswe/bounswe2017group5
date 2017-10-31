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
    location = models.TextField(blank = True, default='')
    tags = models.TextField(blank = True, default='') #TODO change to list
    is_private = models.BooleanField(blank = True,default = False)
    members = models.ManyToManyField(auth_models.User)
    picture = models.ImageField(blank=True, null=True)
    def __str__(self):
        return self.name

    def size(self):
        return self.members.count()

    def get_picture(self):
        if self.picture and hasattr(self.picture, 'url'):
            return self.picture.url
        else:
            return '/static/assets/img/group_default_icon.png'


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
