# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.forms import model_to_dict
from rest_framework.authtoken.models import Token
from rest_framework import generics
from rest_framework.decorators import api_view
from rest_framework.views import APIView
from rest_framework.permissions import IsAuthenticated

from django.shortcuts import render

from django.http import HttpResponse, JsonResponse

from django.contrib.auth import models as auth_models

from api.models import Tag

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
import random

from . import serializers as core_serializers
from .http import ErrorResponse


### List Views BEGIN

class UserList(generics.ListCreateAPIView):
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
    Create a new template instance.
    """
    serializer_class = core_serializers.DataTemplateSerializer
    pagination_class = DataTemplateLimitOffSetPagination

    def get_queryset(self):
        """
        Optionally restricts the returned purchases to a given user,
        by filtering against a `username` query parameter in the URL.
        """
        queryset = core_models.DataTemplate.objects.all()
        group_id = self.request.query_params.get('group', None)
        if group_id is not None:
            queryset = queryset.filter(group=group_id)
        return queryset


class PostList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing posts.

    post:
    Create a new post instance.
    """

    serializer_class = core_serializers.PostSerializer
    pagination_class = PostLimitOffsetPagination

    def get_queryset(self):
        """
        Optionally restricts the returned purchases to a given user,
        by filtering against a `username` query parameter in the URL.
        """
        queryset = core_models.Post.objects.all()
        group_id = self.request.query_params.get('group', None)
        if group_id is not None:
            queryset = queryset.filter(group=group_id)
        return queryset

class TagList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing tags.

    post:
    Create a new tag instance.
    """
    queryset = core_models.Tag.objects.all()
    serializer_class = core_serializers.TagSerializer


class CommentList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing comments.

    post:
    Create a new comment instance.
    """
    queryset = core_models.Comment.objects.all()
    serializer_class = core_serializers.CommentSerializer


class VoteList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing votes.

    post:
    Create a new vote instance.
    """
    queryset = core_models.Vote.objects.all()
    serializer_class = core_serializers.VoteSerializer

class ProfilePageList(generics.ListAPIView):
    """
    get:
    Return a list of all the existing profile pages.

    post:
    Create a new profile page instance.
    """
    queryset = core_models.ProfilePage.objects.all()
    serializer_class = core_serializers.ProfilePageSerializer

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

class ProfilePageDetail(generics.RetrieveUpdateAPIView):
    """
    get:
    Return the details of the comment with the given id.

    update:
    Update the comment detail with the given id.
    """
    queryset = core_models.ProfilePage.objects.all()
    serializer_class = core_serializers.ProfilePageSerializer

class VoteDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    get:
    Return the details of the vote with the given id.

    update:
    Update the vote detail with the given id.

    delete:
    Delete the vote detail with the given id.
    """
    queryset = core_models.Vote.objects.all()
    serializer_class = core_serializers.VoteSerializer


### Detail Views END

class CurrentUserView(APIView):
    def get(self, request):
        serializer = core_serializers.UserSerializer(request.user)
        return JsonResponse(serializer.data)

class MemberGroupOperation(APIView):
    permission_classes = (IsAuthenticated,)

    def put(self, request, pk):
        group = core_models.Group.objects.get(pk=pk)
        # handle, user is already a member.
        if group.members.filter(id=request.user.id).count() == 1:
            return HttpResponse(status=410)
        group.members.add(request.user);
        group.save()
        serializer = core_serializers.GroupSerializer(group)
        return JsonResponse(serializer.data)

    def delete(self, request, pk):
        group = core_models.Group.objects.get(pk=pk)
        # handle, user isn't a member to begin with.
        if group.members.filter(id=request.user.id).count() == 0:
            return HttpResponse(status=410)
        group.members.remove(request.user);
        group.save()
        serializer = core_serializers.GroupSerializer(group)
        return JsonResponse(serializer.data)


@api_view(['GET'])
def search_wikidata(request, limit=15):
    """
    Returns wikidata search results for the specified name in the requests GET field.
    """
    searched_name = urllib.quote_plus(request.GET["term"])
    url = "http://www.wikidata.org//w/api.php?action=wbsearchentities&format=json&search=" + searched_name + "&language=en&type=item&limit=" + str(
        limit)
    response = urllib.urlopen(url)
    data = json.loads(response.read())["search"]
    response = []

    for tag_data in data:
        try:
            tag = Tag.objects.get(concepturi=tag_data["concepturi"])
            tag.label = tag_data["label"]
            tag.url = tag_data["url"]
            tag.description = tag_data["description"] if "description" in tag_data else None
        except:
            tag = Tag(label=tag_data["label"], url=tag_data["url"],
                      description=tag_data["description"] if "description" in tag_data else None,
                      concepturi=tag_data["concepturi"])
        tag.save()
        response.append(model_to_dict(tag))

    return JsonResponse({"results": response})

@api_view(['GET'])
def recommend_groups(request, limit=5):
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
    
    user = request.user
    limit = int(request.GET.get("limit",limit))
    groups = core_models.Group.objects.all()
    users_groups = user.joined_groups.all()

    #list of groups that the user is not a member of.
    candidates = [group for group in groups if group not in users_groups]
    #sort candidate groups according to their similarities to users current groups
    candidates = sorted(candidates, key=lambda group: total_distance(group, users_groups))

    #in case there are not enough candidates as the requested number
    limit = min(len(candidates),limit)
    candidates = list(map(lambda group3: {"id":group3.id, "name": group3.name},candidates[:limit]))
    return JsonResponse({"results": candidates})

class SignUpView(APIView):
    def post(self, request):
        serialized = core_serializers.UserSerializer(data=request.data)
        if serialized.is_valid():
            auth_models.User.objects.create_user(
                email = request.data['email'],
                username = request.data['username'],
                password = request.data['password'] )
            user_to_send = auth_models.User.objects.get(username = request.data['username'])
            profile_page = core_models.ProfilePage(user = user_to_send)
            profile_page.save()
            out_serializer = core_serializers.UserSerializer(user_to_send)
            return JsonResponse(out_serializer.data)
        else:
            return JsonResponse(serialized._errors, status=417)
