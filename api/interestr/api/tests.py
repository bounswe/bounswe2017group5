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

class RecommendationTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user1 = User.objects.create_user('ramazan', 'ramazan@wow.com', 'wowpass123')
        self.test_user2 = User.objects.create_user('murat', 'murat@wow.com', 'wowpass123')
        self.test_user3 = User.objects.create_user('mahmut', 'mahmut@wow.com', 'wowpass123')
        self.test_user4 = User.objects.create_user('enes', 'enes@wow.com', 'wowpass123')
        self.test_user5 = User.objects.create_user('orbay', 'orbay@wow.com', 'wowpass123')

        self.test_group1 = Group.objects.create(name="Chess Fans", description="All about chess.")
        self.test_group2 = Group.objects.create(name="Classic Music Lovers", description="Every thing related to classic music.")
        self.test_group3 = Group.objects.create(name="History Junkies", description="History is simply past.")
        self.test_group4 = Group.objects.create(name="Amateur Basketball", description="Keep the amateur spirit.")
        self.test_group5 = Group.objects.create(name="Heavy Lifting", description="Let's lift!")

        self.test_group1.members.add(self.test_user1, self.test_user4)
        self.test_group2.members.add(self.test_user1, self.test_user2)

    def recommend_groups(self,user, limit=None):
        self.client.force_login(user)
        limit_slug = ("?limit=" + str(limit)) if limit else ""
        response = self.client.get('/api/v1/recommend_groups/'+ limit_slug)
        json_response = json.loads(response.content)
        return list(map(lambda x: Group.objects.get(id=x["id"]),json_response["results"]))

  

    def test_length(self):
        self.test_group5.members.add(self.test_user5, self.test_user4)
        self.test_group4.members.add(self.test_user5, self.test_user2)
        
        recommendations=self.recommend_groups(self.test_user3, 4)
        self.assertEqual(len(recommendations), 4)

    def test_type(self):
        self.test_group5.members.add(self.test_user5, self.test_user4)
        self.test_group4.members.add(self.test_user5, self.test_user3)

        recommendations=self.recommend_groups(self.test_user3, 3)
        for recommendation in recommendations:
            self.assertEqual(recommendation.__class__.__name__,"Group")
    
    def test_quality(self):
        self.test_group3.members.add(self.test_user2, self.test_user4)
        self.test_group4.members.add(self.test_user3, self.test_user5)
        self.test_group5.members.add(self.test_user1, self.test_user5)
        
        recommendations=self.recommend_groups(self.test_user4, 3)
        # as user3 has two co-members in group2, one in group5, zero in group4
        best_recommendations = [self.test_group2,self.test_group5,self.test_group4]
        for i in range(3):
            self.assertEqual(recommendations[i],best_recommendations[i])


class ApiDocTests(TestCase):
    def setUp(self):
        self.client = APIClient()

    def test_doc_working(self):
        response = self.client.get('/api/v1/docs/')

        self.assertEqual(response.status_code, 200)
