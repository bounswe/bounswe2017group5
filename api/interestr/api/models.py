# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.contrib.auth import models as auth_models

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
    description = models.TextField(blank=True, default='No description.')
    # members = models.ManyToManyField(auth_models.User)

    def __str__(self):
        return self.name

    def size(self):
        return len(self.members)
    #ToDo def addMember(self, request)

class Post(BaseModel):
    owner = models.ForeignKey(auth_models.User, related_name="posts")

    def __str__(self):
        return self.owner.username

# Models END
