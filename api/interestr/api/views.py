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
import ast
import random

from . import serializers as core_serializers
from .http import ErrorResponse
from random import sample


# List Views BEGIN

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
    serializer_class = core_serializers.DataTemplateSimpleSerializer
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

    pagination_class = PostLimitOffsetPagination

    def get_serializer_class(self, *args, **kwargs):
        try:  # without try-catch the api docs will break
            if self.request.method in ["POST", "PUT", "PATCH"]:
                return core_serializers.PostCreateSerializer
            return core_serializers.PostSerializer
        except:
            return core_serializers.PostSerializer

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

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


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

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class VoteList(generics.ListCreateAPIView):
    """
    get:
    Return a list of all the existing votes.

    post:
    Create a new vote instance.
    """
    queryset = core_models.Vote.objects.all()

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)

    def get_serializer_class(self, *args, **kwargs):
        try:  # without try-catch the api docs will break
            if self.request.method in ["POST", "PUT", "PATCH"]:
                return core_serializers.VoteCreateSerializer
            return core_serializers.VoteSerializer
        except:
            return core_serializers.VoteSerializer


class ProfilePageList(generics.ListAPIView):
    """
    get:
    Return a list of all the existing profile pages.

    post:
    Create a new profile page instance.
    """
    queryset = core_models.ProfilePage.objects.all()
    serializer_class = core_serializers.ProfilePageSerializer

# List Views END

# Detail Views BEGIN


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

    def get_serializer_class(self, *args, **kwargs):
        try:  # without try-catch the api docs will break
            if self.request.method in ["POST", "PUT", "PATCH"]:
                return core_serializers.DataTemplateSimpleSerializer
            return core_serializers.DataTemplateSerializer
        except:
            return core_serializers.DataTemplateSerializer


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

    def get_serializer_class(self, *args, **kwargs):
        try:  # without try-catch the api docs will break
            if self.request.method in ["POST", "PUT", "PATCH"]:
                return core_serializers.PostCreateSerializer
            return core_serializers.PostSerializer
        except:
            return core_serializers.PostSerializer


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

    def get_serializer_class(self, *args, **kwargs):
        try:  # without try-catch the api docs will break
            if self.request.method in ["POST", "PUT", "PATCH"]:
                return core_serializers.VoteCreateSerializer
            return core_serializers.VoteSerializer
        except:
            return core_serializers.VoteSerializer

#  Detail Views END

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
        group.members.add(request.user)
        group.save()
        serializer = core_serializers.GroupSerializer(group)
        return JsonResponse(serializer.data)

    def delete(self, request, pk):
        group = core_models.Group.objects.get(pk=pk)
        # handle, user isn't a member to begin with.
        if group.members.filter(id=request.user.id).count() == 0:
            return HttpResponse(status=410)
        group.members.remove(request.user)
        group.save()
        serializer = core_serializers.GroupSerializer(group)
        return JsonResponse(serializer.data)

class FollowOperation(APIView):
    permission_classes = (IsAuthenticated,)

    def put(self, request, pk):
        profile = core_models.ProfilePage.objects.get(pk=pk)
        # handle, user is already a follower.
        if profile.followed_by.filter(id=request.user.profile.id).count() == 1:
            return HttpResponse(status=410)
        request.user.profile.follows.add(profile)
        request.user.profile.save()
        serializer = core_serializers.ProfilePageSerializer(request.user.profile)
        return JsonResponse(serializer.data)

    def delete(self, request, pk):
        profile = core_models.ProfilePage.objects.get(pk=pk)
        # handle, user is not actually a follower.
        if profile.followed_by.filter(id=request.user.profile.id).count() == 0:
            return HttpResponse(status=410)
        request.user.profile.follows.remove(profile)
        request.user.profile.save()
        serializer = core_serializers.ProfilePageSerializer(request.user.profile)
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

        return len(members1) + len(members2) - 2 * len(members1 & members2) + random.random()

    def total_distance(group, group_list):
        """
        Sum of all distances a group has to groups from a list
        """
        return sum([distance(group, group2) for group2 in group_list])

    user = request.user
    if request.GET:
        limit = int(request.GET.get("limit", limit))
    groups = core_models.Group.objects.all()
    users_groups = user.joined_groups.all()

    # list of groups that the user is not a member of.
    candidates = [group for group in groups if group not in users_groups]
    # sort candidate groups according to their similarities to users current groups
    candidates = sorted(
        candidates, key=lambda group: total_distance(group, users_groups))

    # in case there are not enough candidates as the requested number
    limit = min(len(candidates), limit)
    candidates = list(
        map(lambda group3: core_serializers.GroupSerializer(group3).data, sample(candidates, limit)))
    return JsonResponse({"results": candidates})


@api_view(['GET'])
def recommend_posts(request, limit=5):
    """
    Returns recommended posts on the basis of the used data templates
    """

    user = request.user
    if request.GET:
        limit = int(request.GET.get("limit", limit))
    posts = core_models.Post.objects.all()
    user_posts = user.posts.all()

    def matching_template_post_count(post):
        return len(filter(lambda p: p.data_template == post.data_template, user_posts))
    # sort candidate groups according to their similarities
    candidates = sorted(list(set(posts) - set(user_posts)),
                        key=matching_template_post_count)

    # in case there are not enough candidates as the requested number
    limit = min(len(candidates), limit)
    candidates = list(
        map(lambda post: core_serializers.PostSerializer(post).data, sample(candidates, limit)))
    return JsonResponse({"results": candidates})

@api_view(['POST'])
def search_posts_by_template(request):
    """
    Returns search results based on template field constraints
    """
    type_dict = { "number": "integer",
                  "date":"date",
                  "text":"text",
                  "textarea":"text",
                  "checkbox":"text",
                  "multisel":"text",
                  "email": "text",
                  "url": "text",
                  "tel": "text"
            }
    operation_dict = { "greater": ">",
                  "less":"<",
                  "equals":"=",
                  "contains":"~" 
            }

    template_id = request.data.get('template_id', None)
    constraints = request.data.get('constraints', [])

    try:
        template = core_models.DataTemplate.objects.get(id=template_id)
        fields = template.fields
    except:
        # Handle the default template case.
        fields = [{ "inputs": [{ "type": "text", "label": False }], "type": "text", "legend": "Text" }]
    
    sql_query = "SELECT * FROM api_post WHERE data_template_id" + (("=" + str(template_id)) if template_id != None else " IS NULL")
    for constraint in constraints:
        field_legend = constraint["field"]
        field_idx = filter(lambda x: x[1]["legend"]==field_legend, enumerate(fields))
        if len(field_idx) == 0:
            # Incorrect POST data, non-existent field legend
            return JsonResponse({"results": []})
            break
        field_idx = field_idx[0][0]
        field_type = type_dict[fields[0]["type"]] 

        operation = operation_dict[constraint["operation"]]
        data = constraint["data"]

        sql_query += " AND (data->" + str(field_idx)  + "->>'response')::" + field_type + operation + "('" + data + "')::" + field_type     

    sql_query += ";"
    qs =core_models.Post.objects.raw(sql_query)
    results = core_serializers.PostSerializer(qs, many=True).data
    return JsonResponse({"results": results})

class SignUpView(APIView):
    def post(self, request):
        serialized = core_serializers.UserSerializer(data=request.data)
        user_with_same_email = auth_models.User.objects.filter(
            email=request.data['email'])
        if serialized.is_valid() and not user_with_same_email:
            auth_models.User.objects.create_user(
                email=request.data['email'],
                username=request.data['username'],
                password=request.data['password'])
            user_to_send = auth_models.User.objects.get(
                username=request.data['username'])
            profile_page = core_models.ProfilePage(user=user_to_send)
            profile_page.save()
            user_to_send.profile = profile_page
            user_to_send.save()
            out_serializer = core_serializers.UserSerializer(user_to_send)
            return JsonResponse(out_serializer.data)
        else:
            return JsonResponse(serialized._errors, status=417)
