# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from .models import Group, Post
from django.contrib import admin

# Register your models here.
admin.site.register(Group)
admin.site.register(Post)
