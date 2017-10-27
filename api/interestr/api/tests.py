# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from django.test import Client

from django.contrib.auth.models import User
from .models import Post

from rest_framework.authtoken.models import Token

import json

# Create your tests here.
class AuthTests(TestCase):

    def setUp(self):
        self.client = Client()
        self.test_user = User.objects.create_user('name', 'wow@wow.com', 'wowpass123')


    def test_token_auth(self):
        response = self.client.post('/api/v1/login/', { 'username' : 'name', 'password' : 'wowpass123' })

        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)

        self.assertEqual(json_response['token'], Token.objects.get(user=self.test_user).key)

class PostTests(TestCase):

	def setUp(self):
		self.client = Client()
		self.test_user = User.objects.create_user('owner', 'wow@wow.com', 'wowpass123')
		self.test_post = Post.objects.create(owner = self.test_user, text = 'text', content_url = '/post/1')

	def test_existing_post(self):
		response = self.client.get('/api/v1/posts/' + str(self.test_post.id) + '/')

		self.assertEqual(response.status_code, 200)

		json_response = json.loads(response.content)

		self.assertEqual(json_response, {'owner' : self.test_user.id, 'text' : 'text', 'content_url' : '/post/1', 'group' : None})

	def test_non_existing_post(self):
		response = self.client.get('/api/v1/posts/' + str(999) + '/')

		self.assertEqual(response.status_code, 404)

