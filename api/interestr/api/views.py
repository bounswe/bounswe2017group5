# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.authtoken.models import Token
from rest_framework import generics
from django.shortcuts import render

from django.contrib.auth import models as auth_models
from . import models as core_models
from django.contrib.auth import authenticate

from . import serializers as core_serializers
from .http import ErrorResponse

from strings import strings

### List Views BEGIN

class UserList(generics.ListAPIView):
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupList(generics.ListCreateAPIView):
    queryset = core_models.Group.objects.all()
    serializers_class = core_serializers.GroupSerializer

class PostList(generics.ListCreateAPIView):
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer

### List Views END

### Detail Views BEGIN

class UserDetail(generics.RetrieveUpdateAPIView):
    queryset = auth_models.User.objects.all()
    serializers_class = core_serializers.UserSerializer

class GroupDetail(generics.RetrieveUpdateAPIView):
    queryset = auth_models.Group.objects.all()
    serializers_class = core_serializers.GroupSerializer

class PostDetail(generics.RetrieveUpdateAPIView):
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer


### Detail Views END


