# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.authtoken.models import Token
from rest_framework import generics
from django.shortcuts import render

from django.contrib.auth import models as auth_models
from django.contrib.auth import authenticate

from django.http import JsonResponse

from . import serializers as core_serializers

def obtain_auth_token(request):
    # !!!!!! Has to use https !!!!!!
    # TODO make it available only over https
    username = request.data.get('username')
    password = request.data.get('password')

    user = authenticate(username=username, password=password)

    if user is not None:
        token = Token.objects.create(user=user)
        return JsonResponse({ "token" : token.key })
    else:
        pass


### List Views BEGIN

class UserList(generics.ListAPIView):
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupList(generics.ListAPIView):
    queryset = auth_models.Group.objects.all()
    serializers_class = core_serializers.GroupSerializer

### List Views END

### Detail Views BEGIN

class UserDetail(generics.RetrieveUpdateAPIView):
    queryset = auth_models.User.objects.all()
    serializers_class = core_serializers.UserSerializer

class GroupDetail(generics.RetrieveUpdateAPIView):
    queryset = auth_models.Group.objects.all()
    serializers_class = core_serializers.GroupSerializer

### Detail Views END
