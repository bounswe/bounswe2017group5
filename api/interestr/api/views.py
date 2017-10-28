# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.authtoken.models import Token
from rest_framework import generics
from django.shortcuts import render

from django.http import HttpResponse, JsonResponse

from django.contrib.auth import models as auth_models
from . import models as core_models
from django.contrib.auth import authenticate


from django.views.decorators.csrf import csrf_exempt

from . import serializers as core_serializers
from .http import ErrorResponse

from strings import strings

from data_templates import models as data_templates_models

### List Views BEGIN

class UserList(generics.ListAPIView):
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupList(generics.ListAPIView):
    queryset = auth_models.Group.objects.all()
    serializer_class = core_serializers.GroupSerializer

class DataTemplateList(generics.ListCreateAPIView):
    queryset = data_templates_models.DataTemplate.objects.all()
    serializer_class = core_serializers.DataTemplateSerializer

class PostList(generics.ListCreateAPIView):
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer

### List Views END

### Detail Views BEGIN

class UserDetail(generics.RetrieveUpdateAPIView):
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupDetail(generics.RetrieveUpdateAPIView):
    queryset = core_models.Group.objects.all()
    serializer_class = core_serializers.GroupSerializer

class DataTemplateDetail(generics.RetrieveUpdateAPIView):
    queryset = data_templates_models.DataTemplate.objects.all()
    serializer_class = core_serializers.DataTemplateSerializer

class PostDetail(generics.RetrieveUpdateAPIView):
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer

### Detail Views END

#@csrf_exempt
#removes or adds the authenticated user from/to the group
#whose id is pk
def memberGroupOperation(request, pk):
    #check auth
    try:
        group = core_models.Group.objects.get(pk=pk)
    except core_models.Group.DoesNotExist:
        return HttpResponse(status=404)

    # get authenticated user.
    user = request.user
    if not user.is_authenticated():
        # Do something for anonymous users.
        # return JsonResponse({"patates":"ehe"})
        return HttpResponse(status=403)
    # user = auth_models.User.objects.get(id=1)

    if request.method == 'PUT':
        if group.members.filter(id=user.id).count() == 1:
            # handle, user is already a member.
            # return JsonResponse({"patates":"ehe"})
            return HttpResponse(status=410)
        else:
            group.members.add(user)
            group.save()
            serializer = core_serializers.GroupSerializer(group)
            return JsonResponse(serializer.data)

    elif request.method == 'DELETE':
        if group.members.filter(id=user.id).count() == 0:
            # handle, user isn't a member to begin with.
            # return JsonResponse({"patates":"ehe"})
            return HttpResponse(status=410)
        else:
            group.members.remove(user)
            group.save()
            serializer = core_serializers.GroupSerializer(group)
            return JsonResponse(serializer.data)