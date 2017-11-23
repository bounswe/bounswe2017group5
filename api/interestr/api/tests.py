# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from rest_framework.test import APIClient

from django.contrib.auth.models import User
from .models import Post
from .models import Group
from .models import DataTemplate

from rest_framework.authtoken.models import Token

from django.contrib.auth import models as auth_models
from . import models as core_models

import json


# Create your tests here.
class AuthTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user = User.objects.create_user('name', 'wow@wow.com', 'wowpass123')

    def test_token_auth(self):
        response = self.client.post('/api/v1/login/', {'username': 'name', 'password': 'wowpass123'})

        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)

        self.assertEqual(json_response['token'], Token.objects.get(user=self.test_user).key)


class PostTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user = User.objects.create_user('owner', 'wow@wow.com', 'wowpass123')
        self.test_post = Post.objects.create(owner=self.test_user, text='text')

    def test_existing_post(self):
        response = self.client.get('/api/v1/posts/' + str(self.test_post.id) + '/')

        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)

        self.assertEqual(json_response['owner'], self.test_user.id)

    def test_non_existing_post(self):
        response = self.client.get('/api/v1/posts/' + str(999) + '/')

        self.assertEqual(response.status_code, 404)


class GroupTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.user_pass = 'Qq12341234'
        self.test_user1 = User.objects.create_user('user', 'e@mail.com', self.user_pass)
        self.test_user2 = User.objects.create_user('iser', 'g@mail.com', self.user_pass)
        self.test_group = Group.objects.create(name="grupce", description="boyband")

    def test_1_add_users_to_group(self):
        self.client.login(username='user', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/1/', follow=True)
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)

        self.client.login(username='iser', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/1/', follow=True)
        json_response = json.loads(response.content)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 2)

    def test_2_add_existing_user_to_group(self):
        self.client.login(username='user', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/2/', follow=True)
        response = self.client.put('/api/v1/users/groups/2/', follow=True)
        self.assertEqual(response.status_code, 410)

    def test_3_add_users_to_group(self):
        # add 2 users, remove the last one.
        self.client.login(username='user', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/3/', follow=True)
        self.client.login(username='iser', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/3/', follow=True)
        response = self.client.delete('/api/v1/users/groups/3/', follow=True)
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)

    def test_4_remove_nonexistant_users_from_group(self):
        # add user1
        self.client.login(username='user', password=self.user_pass)
        self.client.put('/api/v1/users/groups/4/', follow=True)

        # try to remove user2
        self.client.login(username='iser', password=self.user_pass)
        response = self.client.delete('/api/v1/users/groups/4/', follow=True)

        self.assertEqual(response.status_code, 404)


class DataTemplateTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user = User.objects.create_user('owner', 'wow@wow.com', 'wowpass123')
        self.client.force_authenticate(user=self.test_user)
        self.test_group = Group.objects.create(name="grupce", description="boyband")
        self.test_data_template = DataTemplate.objects.create(user=self.test_user, group=self.test_group,
                                                              name='Basic template', fields=['short_text', 'long_text'])

    def test_create_template(self):
        template_fields = {'name': 'Basic template', 'fields': ['short_text', 'long_text'],
                           'group': self.test_group.id, 'user': self.test_user.id}
        response = self.client.post('/api/v1/data_templates/', template_fields, format='json')
        self.assertEqual(response.status_code, 201)

    def test_existing_template(self):
        response = self.client.get('/api/v1/data_templates/' + str(self.test_data_template.id) + '/')
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], self.test_data_template.name)

    def test_non_existing_template(self):
        response = self.client.get('/api/v1/data_templates/' + str(999) + '/')
        self.assertEqual(response.status_code, 404)

    def test_delete_template(self):
        response = self.client.get('/api/v1/data_templates/' + str(self.test_data_template.id) + '/')
        self.assertEqual(response.status_code, 200)
        response = self.client.delete('/api/v1/data_templates/' + str(self.test_data_template.id) + '/')
        self.assertEqual(response.status_code, 204)
        response = self.client.get('/api/v1/data_templates/' + str(self.test_data_template.id) + '/')
        self.assertEqual(response.status_code, 404)


class ApiDocTests(TestCase):
    def setUp(self):
        self.client = APIClient()

    def test_doc_working(self):
        response = self.client.get('/api/v1/docs/')

        self.assertEqual(response.status_code, 200)


class SearchTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.user_pass = 'Ab12Cd34'
        self.test_user1 = User.objects.create_user('mehmet', 'mehmet@mail.com', self.user_pass)
        self.test_user2 = User.objects.create_user('ali', 'ali@mail.com', self.user_pass)
        self.test_user3 = User.objects.create_user('mehmet ali', 'memoli@mail.com', self.user_pass)
        self.test_group1 = Group.objects.create(name="awesome", description="good")
        self.test_group2 = Group.objects.create(name="best", description="fine")
        self.test_group3 = Group.objects.create(name="awesome best", description="great")


    def count_users(self, query):
        c = 0;
        for User in auth_models.User.objects.all():
            if query in User.username:
                c = c + 1

        return c

    def count_groups(self, query):
        d = 0;
        for Group in core_models.Group.objects.all():
            if query in Group.name:
                d = d + 1

        return d

    #search for the first user
    def test_user_search1(self):
        response = self.client.get('/api/v1/users/' + '?q=' + self.test_user1.username)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_users(self.test_user1.username))

    #search for the second user
    def test_user_search2(self):
        response = self.client.get('/api/v1/users/' + '?q=' + self.test_user2.username)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_users(self.test_user2.username))

    #search for the third user
    def test_user_search3(self):
        response = self.client.get('/api/v1/users/' + '?q=' + self.test_user3.username)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_users(self.test_user3.username))

    #search for a user that does not exist
    def test_user_search4(self):
        response = self.client.get('/api/v1/users/' + '?q=' + 'dummy')
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_users('dummy'))




    #search for the first group
    def test_group_search1(self):
        response = self.client.get('/api/v1/groups/' + '?q=' + self.test_group1.name)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_groups(self.test_group1.name))

    #search for the second group
    def test_group_search2(self):
        response = self.client.get('/api/v1/groups/' + '?q=' + self.test_group2.name)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_groups(self.test_group2.name))

    #search for the third group
    def test_group_search3(self):
        response = self.client.get('/api/v1/groups/' + '?q=' + self.test_group3.name)
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_groups(self.test_group3.name))

    #search for a group that does not exist
    def test_group_search4(self):
        response = self.client.get('/api/v1/groups/' + '?q=' + 'dummy')
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        self.assertEqual(json_response['count'], self.count_groups('dummy'))