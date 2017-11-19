# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from rest_framework.test import APIClient

from django.contrib.auth.models import User
from .models import Post
from .models import Group
from .models import DataTemplate

from rest_framework.authtoken.models import Token

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
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)

        self.client.login(username='iser', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        json_response = json.loads(response.content)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 2)

    def test_2_add_existing_user_to_group(self):
        self.client.login(username='user', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        self.assertEqual(response.status_code, 410)

    def test_3_add_users_to_group(self):
        # add 2 users, remove the last one.
        self.client.login(username='user', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        self.client.login(username='iser', password=self.user_pass)
        response = self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        response = self.client.delete('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)

    def test_4_remove_nonexistant_users_from_group(self):
        # add user1
        self.client.login(username='user', password=self.user_pass)
        self.client.put('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)

        # try to remove user2
        self.client.login(username='iser', password=self.user_pass)
        response = self.client.delete('/api/v1/users/groups/'+ str(self.test_group.id) +'/', follow=True)

        self.assertEqual(response.status_code, 410)


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
