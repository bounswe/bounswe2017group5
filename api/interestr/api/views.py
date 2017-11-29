# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.authtoken.models import Token
from rest_framework import generics
from rest_framework.decorators import api_view
from django.shortcuts import render

from django.http import HttpResponse, JsonResponse

from django.contrib.auth import models as auth_models
from . import models as core_models
from django.contrib.auth import authenticate

from .pagination import GroupLimitOffsetPagination
from .pagination import PostLimitOffsetPagination
from .pagination import UserLimitOffsetPagination
from .pagination import DataTemplateLimitOffSetPagination

from django.views.decorators.csrf import csrf_exempt

import urllib
import json
import random

from . import serializers as core_serializers
from .http import ErrorResponse

### List Views BEGIN

class UserList(generics.ListAPIView):
    """
    Return a list of all the existing users.
    """
    queryset = auth_models.User.objects.all()
    serializer_class = core_serializers.UserSerializer
    pagination_class = UserLimitOffsetPagination

class GroupList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing groups.

    post:
    Create a new group instance.
    """
    queryset = core_models.Group.objects.all()
    serializer_class = core_serializers.GroupSerializer
    pagination_class = GroupLimitOffsetPagination

class DataTemplateList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing data templates.

    post:
    Create a new data template instance.
    """
    queryset = core_models.DataTemplate.objects.all()
    serializer_class = core_serializers.DataTemplateSerializer
    pagination_class = DataTemplateLimitOffSetPagination

class PostList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing posts.

    post:
    Create a new data post instance.
    """
    queryset = core_models.Post.objects.all()
    serializer_class = core_serializers.PostSerializer
    pagination_class = PostLimitOffsetPagination

class TagList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing tags.

    post:
    Create a new data tag instance.
    """
    queryset = core_models.Tag.objects.all()
    serializer_class = core_serializers.TagSerializer


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
    queryset = core_models.DataTemplate.objects.all()
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

class TagDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the tag with the given id.

    update:
    Update the tag detail with the given id.

    delete:
    Delete the tag detail with the given id.
    """
    queryset = core_models.Tag.objects.all()
    serializer_class = core_serializers.TagSerializer


### Detail Views END

@api_view(['PUT', 'DELETE'])
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
        return HttpResponse(status=403)

    if request.method == 'PUT':
        if group.members.filter(id=user.id).count() == 1:
            # handle, user is already a member.
            return HttpResponse(status=410)
        else:
            group.members.add(user)
            group.save()
            serializer = core_serializers.GroupSerializer(group)
            return JsonResponse(serializer.data)

    elif request.method == 'DELETE':
        if group.members.filter(id=user.id).count() == 0:
            # handle, user isn't a member to begin with.
            return HttpResponse(status=410)
        else:
            group.members.remove(user)
            group.save()
            serializer = core_serializers.GroupSerializer(group)
            return JsonResponse(serializer.data)

@api_view(['GET'])
def search_wikidata(request, limit=15):
    """
    Returns wikidata search results for the specified name in the requests GET field.
    """
    searched_name = urllib.quote_plus(request.GET["name"])
    url = "http://www.wikidata.org//w/api.php?action=wbsearchentities&format=json&search="+searched_name+"&language=en&type=item&limit="+str(limit)
    response = urllib.urlopen(url)
    data = json.loads(response.read())
 
    data = data["search"]
    fields = ('label', 'url','description', 'concepturi', 'created', 'updated')
    data = [{k:tag_data[k] for k in fields if k in tag_data} for tag_data in data]

    return JsonResponse({"resuts":data})


def recommend_groups(user, limit=2):
    """
    Returns recommended groups on the basis of the other groups of the 
    users that the given user has a common group.
    """
    def distance(group1, group2):
        """
        Calculates the distance between groups based on how much they 
        'agree' on their members
        """
        members1 = group1.members.all().values_list('id', flat=True)
        members2 = group2.members.all().values_list('id', flat=True)
        
        return len(members1) + len(members2) - 2*len(members1 & members2)+random.random()

    def total_distance(group, group_list):
        """
        Sum of all distances a group has to groups from a list
        """
        return sum(list(map(lambda group2: distance(group,group2), group_list)))
    
    groups = core_models.Group.objects.all()
    users_groups = user.joined_groups.all()

    #list of groups that the user is not a member of.
    candidates = [group for group in groups if group not in users_groups]
    #sort candidate groups according to their similarities to users current groups
    candidates = sorted(candidates, key=lambda group: total_distance(group, users_groups))

    #in case there are not enough candidates as the requested number
    limit = min(len(candidates),limit)

    return candidates[:limit]

