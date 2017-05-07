# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from rest_framework import status
from rest_framework.test import APITestCase
from django.contrib.auth.models import User

class UserTests(APITestCase):

	def setUp(self):
		user = User.objects.create_user('testUser2', 'testEmail@testEmail.com', 'passTestUser')


	def login(self, data):
		url = '/login/'
		return self.client.post(url, data)

	def test_register(self):
		"""
		We should be able to create an account with a username and a password.
		"""
		url = '/register/'
		data = {'username' : 'testUser1234', 'password' : 'pass12345'} # The amazing password...
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(User.objects.count(), 2)
		self.assertEqual(User.objects.get(username='testUser1234').username, 'testUser1234')

	def test_login(self):
		"""
		Login should work for the testUser2.
		"""
		data = {'username' : 'testUser2', 'password' : 'passTestUser'}
		response = self.login(data)
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		try:
			token = response.data["token"]
		except AttributeError:
			self.fail("No token attribute")

	def test_login_wrong_credentials(self):
		"""
		Login should fail with 400 for a non-existent user.
		"""
		data = {'username' : 'nonexistentuser', 'password' : 'nopasswordlol'}
		response = self.login(data)

		error_text = "Unable to log in with provided credentials."

		self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
		try:
			if error_text not in response.data["non_field_errors"]:
				self.fail("Error text must be : '" + error_text + "'")
		except AttributeError:
			self.fail("There must be at least one entry in 'non_field_errors'")

	def test_login_required_attr(self):
		"""
		'username' and 'password' is required for login.
		"""
		data = {'username' : 'testUser2'}
		response = self.login(data)

		error_text = "This field is required."

		self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
		try:
			if error_text not in response.data["password"]:
				self.fail("Error text must exist in 'password' : '" + error_text + "'")
		except AttributeError:
			self.fail("There must be at least one entry in 'password'")

		data = {'password' : 'pass12345'}
		response = self.login(data)

		self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
		try:
			if error_text not in response.data["username"]:
				self.fail("Error text must exist in 'username' : '" + error_text + "'")
		except AttributeError:
			self.fail("There must be at least one entry in 'username'")

		data = {}
		response = self.login(data)

		self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
		try:
			if error_text not in response.data["username"]:
				self.fail("Error text must exist in 'username' : '" + error_text + "'")
			if error_text not in response.data["password"]:
				self.fail("Error text must exist in 'password' : '" + error_text + "'")
		except AttributeError:
			self.fail("There must be at least one entry of '" + 
				error_text + "' in either of 'username' or 'password'")





