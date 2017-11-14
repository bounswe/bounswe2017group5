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
    """
    Return a list of all the existing users.
    """
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing groups.

    post:
    Create a new group instance.
    """
    queryset = core_models.Group.objects.all()
    serializer_class = core_serializers.GroupSerializer

class DataTemplateList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing data templates.

    post:
    Create a new data template instance.
    """
    queryset = data_templates_models.DataTemplate.objects.all()
    serializer_class = core_serializers.DataTemplateSerializer

class PostList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing posts.

    post:
    Create a new data post instance.
    """
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer

### List Views END

### Detail Views BEGIN

class UserDetail(generics.RetrieveUpdateAPIView):
    """
    get:
    Return the details of the user with the given id.

    update:
    Update the user with the given id.
    """
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer

class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the group with the given id.

    update:
    Update the group with the given id.

    delete:
    Delete the group with the given id.
    """
    queryset = core_models.Group.objects.all()
    serializer_class = core_serializers.GroupSerializer

class DataTemplateDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the data template with the given id.

    update:
    Update the data template detail with the given id.

    delete:
    Delete the data template detail with the given id.
    """
    queryset = data_templates_models.DataTemplate.objects.all()
    serializer_class = core_serializers.DataTemplateSerializer

class PostDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the post with the given id.

    update:
    Update the post detail with the given id.

    delete:
    Delete the post detail with the given id.
    """
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer

### Detail Views END

@csrf_exempt
def memberGroupOperation(request, pk):
    """
    Removes or adds the authenticated user from/to the group
    whose id is pk.
    """
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
