# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.authtoken.models import Token
from rest_framework import generics
from rest_framework.decorators import api_view
from rest_framework.views import APIView
from rest_framework.permissions import IsAuthenticated

from django.shortcuts import render


from django.http import HttpResponse, JsonResponse

from django.contrib.auth import models as auth_models
from . import models as core_models
from django.contrib.auth import authenticate

from .pagination import GroupLimitOffsetPagination
from .pagination import PostLimitOffsetPagination
from .pagination import UserLimitOffsetPagination
from .pagination import DataTemplateLimitOffSetPagination

from django.db.models import Q

from django.views.decorators.csrf import csrf_exempt

import urllib
import json

from . import serializers as core_serializers
from .http import ErrorResponse

### List Views BEGIN

class UserList(generics.ListAPIView):
    """
    Return a list of all the existing users.
    """
    serializer_class = core_serializers.UserSerializer
    pagination_class = UserLimitOffsetPagination

    def get_queryset(self, *args, **kwargs):
        query_list = auth_models.User.objects.all()
        query = self.request.GET.get("q")
        if query:
            query_list = query_list.filter(
                Q(username__icontains=query)
            ).distinct()
        return query_list

class GroupList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing groups.

    post:
    Create a new group instance.
    """
    serializer_class = core_serializers.GroupSerializer
    pagination_class = GroupLimitOffsetPagination

    def get_queryset(self, *args, **kwargs):
        query_list = core_models.Group.objects.all()
        query = self.request.GET.get("q")
        if query:
            query_list = query_list.filter(
                Q(name__icontains=query)
            ).distinct()
        return query_list

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

class CommentList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing comments.

    post:
    Create a new data comment instance.
    """
    queryset = core_models.Comment.objects.all()
    serializer_class = core_serializers.CommentSerializer

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

class CommentDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the comment with the given id.

    update:
    Update the comment detail with the given id.

    delete:
    Delete the comment detail with the given id.
    """
    queryset = core_models.Comment.objects.all()
    serializer_class = core_serializers.CommentSerializer


### Detail Views END

class MemberGroupOperation(APIView):
    permission_classes = (IsAuthenticated,)

    def put(self, request, pk):
        group = core_models.Group.objects.get(pk=pk)
        # handle, user is already a member.
        if group.members.filter(id=request.user.id).count() == 1:
            return HttpResponse(status=410)
        group.members.add(request.user);    group.save()
        serializer = core_serializers.GroupSerializer(group)
        return JsonResponse(serializer.data)

    def delete(self, request, pk):
        group = core_models.Group.objects.get(pk=pk)
        # handle, user isn't a member to begin with.
        if group.members.filter(id=request.user.id).count() == 0:
            return HttpResponse(status=410)
        group.members.remove(request.user); group.save()
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
