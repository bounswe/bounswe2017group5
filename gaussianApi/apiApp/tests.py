# -*- coding: utf-8 -*-
from __future__ import unicode_literals

#from django.urls import reverse
from django.test import TestCase
from rest_framework import status
from rest_framework.test import APITestCase
from django.contrib.auth.models import User

from apiApp.serializers import UserSerializer

class UserTests(APITestCase):

	def setUp(self):
		user = User.objects.create_user('testUser2', 'testEmail@testEmail.com', 'passTestUser')


	def test_register(self):
		"""
		We should be able to create an account with a username and a password
		"""
		url = '/register/'
		data = {'username' : 'testUser1234', 'password' : 'pass12345'} # The amazing password...
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(User.objects.count(), 2)
		self.assertEqual(User.objects.get(username='testUser1234').username, 'testUser1234')

	def test_login(self):
		"""
		Login should work for the testUser2
		"""
		url = '/login/'
		data = {'username' : 'testUser2', 'password' : 'passTestUser'}
		response = self.client.post(url, data)
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		try:
			token = response.data
		except AttributeError:
			self.fail("No token attribute")


