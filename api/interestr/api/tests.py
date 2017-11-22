# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from rest_framework.test import APIClient

from django.contrib.auth.models import User
from .models import Post, Group, Comment, DataTemplate

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
        self.test_group = Group.objects.create(name="Shout!", description="WE ALSO LOVE CAPS:)")
        self.test_post = Post.objects.create(owner=self.test_user, text='text',group=self.test_group)

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
        self.test_user1 = User.objects.create_user('user', 'e@mail.com', '1234')
        self.test_user2 = User.objects.create_user('iser', 'g@mail.com', '4321')
        self.test_group = Group.objects.create(name = "grupce", description="boyband")

    def test_1_add_users_to_group(self):

        self.client.login(username= 'user', password='1234')
        test_group_id = self.test_group.id
        test_url = '/api/v1/users/groups/' + str(test_group_id) + '/'
        response = self.client.put(test_url, follow=True)
        json_response = json.loads(response.content)

        self.assertEqual(response.status_code, 200)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)

        self.client.login(username= 'iser', password='4321')
        response = self.client.put(test_url, follow=True)
        json_response = json.loads(response.content)
        self.assertEqual(response.status_code, 200)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 2)

    def test_2_add_existing_user_to_group(self):
        self.test_group.members.add(self.test_user1)
        self.client.login(username= 'user', password='1234')
        test_group_id = self.test_group.id
        test_url = '/api/v1/users/groups/' + str(test_group_id) + '/'
        response = self.client.put(test_url, follow=True)
        self.assertEqual(response.status_code, 410)

    def test_3_remove_users_from_group(self):
        self.test_group.members.add(self.test_user1)
        self.test_group.members.add(self.test_user2)
        test_group_id = self.test_group.id
        test_url = '/api/v1/users/groups/' + str(test_group_id) + '/'       
        
        self.client.login(username= 'user', password='1234')
        response = self.client.delete(test_url, follow=True)
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['name'], 'grupce')
        self.assertEqual(json_response['size'], 1)


    def test_4_remove_nonexistant_users_from_group(self):
        self.test_group.members.add(self.test_user1)
        test_group_id = self.test_group.id
        test_url = '/api/v1/users/groups/' + str(test_group_id) + '/'       

        self.client.login(username= 'iser', password='4321')
        response = self.client.delete(test_url, follow=True)

        self.assertEqual(response.status_code, 410)

class CommentTests(TestCase):

    def setUp(self):
        self.client = APIClient()
        self.test_user1 = User.objects.create_user('zahidov', 'zahid@mail.com', "kayinco137")
        self.test_user2 = User.objects.create_user('asimov', 'asim@mail.com', "darling")
        self.test_group = Group.objects.create(name="Soviet Fans", description="We also love Russia:)")
        self.test_post1 = Post.objects.create(owner=self.test_user1, text='Who is your favorite leader?', group=self.test_group)
        self.test_post2 = Post.objects.create(owner=self.test_user2, text='What is your favorite city?', group=self.test_group)
        self.test_comment1 = Comment.objects.create(owner=self.test_user1, text="Putin is the \n Kiiing!", post= self.test_post1)
        self.test_comment2 = Comment.objects.create(owner=self.test_user1, text="Stalingrad!", post= self.test_post2)

    def test_create_comment(self):
        #check prior comment counts
        self.assertEqual(self.test_post1.comments.count(), 1)        
        self.assertEqual(self.test_post2.comments.count(), 1)
        self.assertEqual(self.test_user1.comments.count(), 2)        
        self.assertEqual(self.test_user2.comments.count(), 0)

        comment_fields =  {'text':"Kim Yong is the \n new Kiiing!" , "post" : self.test_post1.id,
                           'owner': self.test_user2.id}
        response = self.client.post('/api/v1/comments/', comment_fields, format='json')
        self.assertEqual(response.status_code, 201)
        #check comment counts after insertion
        self.assertEqual(self.test_post1.comments.count(), 2)        
        self.assertEqual(self.test_post2.comments.count(), 1)
        self.assertEqual(self.test_user1.comments.count(), 2)        
        self.assertEqual(self.test_user2.comments.count(), 1)

        comment_fields =  {'text':"Petersburg of course!" , "post" : self.test_post2.id,
                           'owner': self.test_user1.id}
        response = self.client.post('/api/v1/comments/', comment_fields, format='json')
        self.assertEqual(response.status_code, 201)
        #check comment counts after insertion
        self.assertEqual(self.test_post1.comments.count(), 2)        
        self.assertEqual(self.test_post2.comments.count(), 2)
        self.assertEqual(self.test_user1.comments.count(), 3)        
        self.assertEqual(self.test_user2.comments.count(), 1)


    def test_existing_comment(self):
        response = self.client.get('/api/v1/comments/' + str(self.test_comment1.id) + '/')
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['text'], self.test_comment1.text)

        response = self.client.get('/api/v1/users/' + str(self.test_user1.id) + '/')
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['comments'][0]['text'], self.test_comment1.text)

        response = self.client.get('/api/v1/posts/' + str(self.test_post1.id) + '/')
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        self.assertEqual(json_response['comments'][0]['text'], self.test_comment1.text)

    def test_non_existing_comment(self):
        response = self.client.get('/api/v1/comments/' + str(999) + '/')
        self.assertEqual(response.status_code, 404)

    def test_delete_comment(self):
        response = self.client.get('/api/v1/comments/' + str(self.test_comment1.id) + '/')
        self.assertEqual(response.status_code, 200)
        response = self.client.delete('/api/v1/comments/' + str(self.test_comment1.id) + '/')
        self.assertEqual(response.status_code, 204)
        #check comment counts after deletion
        self.assertEqual(self.test_post1.comments.count(), 0)        
        self.assertEqual(self.test_post2.comments.count(), 1)
        self.assertEqual(self.test_user1.comments.count(), 1)        
        self.assertEqual(self.test_user2.comments.count(), 0)


        response = self.client.get('/api/v1/comments/' + str(self.test_comment1.id) + '/')
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
