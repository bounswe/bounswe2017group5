# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from django.test import Client

from django.contrib.auth.models import User

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
