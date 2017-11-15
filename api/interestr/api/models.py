# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth import models as auth_models
from django.utils import timezone

from django.contrib.postgres.fields import JSONField
import inspect
from enum import Enum


# BaseModel, do not touch this...
class BaseModel(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)

    class Meta:
        abstract = True
        ordering = ('created',)


class ChoiceEnum(Enum):
    @classmethod
    def choices(cls):
        # get all members of the class
        members = inspect.getmembers(cls, lambda m: not (inspect.isroutine(m)))
        # filter down to just properties
        props = [m for m in members if not (m[0][:2] == '__')]
        # format into django choice tuple
        choices = tuple([(str(p[1].value), p[0]) for p in props])
        return choices


# Models START

class Tag(BaseModel):
    label = models.CharField(max_length=40)
    url = models.URLField()
    concepturi = models.URLField(unique=True)
    description = models.TextField()

    def __str__(self):
        return self.label+'('+self.concepturi+')'

class Group(BaseModel):
    name = models.CharField(max_length=40)
    description = models.TextField(blank=True, default='')
    location = models.TextField(blank = True, default='')
    tags = models.TextField(blank = True, default='') #TODO change to list
    #tags = models.ManyToManyField(Tag, related_name='tags')
    is_private = models.BooleanField(blank = True,default = False)
    members = models.ManyToManyField(auth_models.User)
    picture = models.ImageField(blank=True, null=True)

    def __str__(self):
        return self.name

    def size(self):
        return self.members.count()

    def has_user(self, user):
        return user in self.members

    def join_user(self, user):
        return self.members.add(user)

    def remove_user(self, user):
        return self.members.remove(user)

    def get_picture(self):
        if self.picture and hasattr(self.picture, 'url'):
            return self.picture.url
        else:
            return '/static/assets/img/group_default_icon.png'


class Post(BaseModel):
    owner = models.ForeignKey(auth_models.User, related_name="posts", default=None, null=True)
    text = models.TextField(default='')
    group = models.ForeignKey(Group, related_name='posts', on_delete=models.CASCADE, default=None, null=True)
    data_template = models.ForeignKey('api.DataTemplate', related_name='posts', default=None, null=True)
    data = JSONField(default=None, null=True)

    def __str__(self):
        return self.text


class DataTemplate(BaseModel):
    name = models.CharField(max_length=40)
    group = models.ForeignKey(Group, related_name='data_templates', on_delete=models.SET_NULL, default=None, null=True)
    user = models.ForeignKey(auth_models.User, related_name='data_templates', on_delete=models.SET_NULL, default=None,
                             null=True)
    fields = JSONField()

    def __str__(self):
        return self.name

# Models END
