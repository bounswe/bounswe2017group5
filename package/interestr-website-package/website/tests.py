# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from django.test import Client

# Create your tests here.
class WebsiteRegisterTest(TestCase):

    def setUp(self):
        self.client = Client()

    def test_register_with_invalid_values(self):
        response = self.client.post('/register/', { 'username' : 'karacasoft',
            'email' : 'invalid', 'password1' : 'wow', 'password2' : 'wow' })

        # TODO assert page displaying the errors

        self.assertEqual(response.status_code, 200)
