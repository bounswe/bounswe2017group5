# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.test import TestCase
from rest_framework.test import APIClient

from django.contrib.auth.models import User
from .models import Post, Group, Comment, DataTemplate



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

class SignupTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user = User.objects.create_user('user', 'e@mail.com', 'password123')

    def test_signup_nonexisting_user(self):
        response = self.client.post('/api/v1/register/',
            {
                'username': 'name',
                'email':'email@email.com',
                'password':'password123'
            })

        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        try:
            self.assertEqual(json_response['username'], 'name')
            self.assertEqual(json_response['email'], 'email@email.com')
            self.assertEqual(json_response['joined_groups'], [])
            self.assertEqual(json_response['moderated_groups'], [])
            self.assertEqual(json_response['data_templates'], [])
            self.assertEqual(json_response['posts'], [])
        except KeyError:
            self.fail('User object definition is not correct')

    def test_signup_with_existing_username(self):
        response = self.client.post('/api/v1/register/',
            {
                'username' : 'user',
                'email' : 'email@email.com',
                'password' : 'password123'
            })

        self.assertEqual(response.status_code, 417)

        json_response = json.loads(response.content)

        self.assertEqual(json_response['username'],
            ["A user with that username already exists."])

    def test_signup_with_existing_email(self):
        response = self.client.post('/api/v1/register/',
            {
                'username' : 'name2',
                'email' : 'email@email.com',
                'password' : 'password123'
            })
        self.assertEqual(response.status_code, 200)

        json_response = json.loads(response.content)
        try:
            self.assertEqual(json_response['username'], 'name2')
            self.assertEqual(json_response['email'], 'email@email.com')
            self.assertEqual(json_response['joined_groups'], [])
            self.assertEqual(json_response['moderated_groups'], [])
            self.assertEqual(json_response['data_templates'], [])
            self.assertEqual(json_response['posts'], [])
        except KeyError:
            self.fail('User object definition is not correct')


class PostTests(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.test_user = User.objects.create_user('owner', 'wow@wow.com', 'wowpass123')
        self.test_group = Group.objects.create(name="Shout!", description="WE ALSO LOVE CAPS:)")
        self.test_post = Post.objects.create(owner=self.test_user, data=[{
            'question' : 'IS YOUR CAPS LOCK ON?',
            'response' : 'YES'
        }],group=self.test_group)

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

class ProfilePageTests(TestCase):
    def setUp(self):
        self.client = APIClient()

    def test_1_check_auto_creation(self):
        # posts a user and checks if profile page is created
        post_test_url = '/api/v1/register/'

        user_dict = {
            'username' : 'naim',
            'email' : 'e@mail.com',
            'password' : '10201'
        }
        # post the user
        response = self.client.post(post_test_url, user_dict)
        json_response = json.loads(response.content)
        user_id = json_response['id']
        # get its id
        profilepage = json_response['profilepage']
        sakki_no_id = profilepage['id']
        self.assertEqual(response.status_code, 200)
        # try to retrieve a profile page with given id
        profile_page_url = '/api/v1/profile_pages/' + str(sakki_no_id) + '/'  
        response = self.client.get(profile_page_url)
        json_response = json.loads(response.content)


        self.assertEqual(json_response['name'], '')
        self.assertEqual(json_response['surname'], '')
        self.assertEqual(json_response['location'], '')
        self.assertEqual(json_response['interests'], '')
        self.assertEqual(json_response['date_of_birth'], '1900-01-01')
        self.assertEqual(json_response['user'], user_id) # last user's id


class CommentTests(TestCase):

    def setUp(self):
        self.client = APIClient()
        self.test_user1 = User.objects.create_user('zahidov', 'zahid@mail.com', "kayinco137")
        self.test_user2 = User.objects.create_user('asimov', 'asim@mail.com', "darling")
        self.test_group = Group.objects.create(name="Soviet Fans", description="We also love Russia:)")
        self.test_post1 = Post.objects.create(owner=self.test_user1, data=[
                {
                    'question' : 'Who is your favorite leader?',
                    'response' : 'Putin is the Kiiing!'
                }
            ], group=self.test_group)
        self.test_post2 = Post.objects.create(owner=self.test_user2, data=[
                {
                    'question' : 'What is your favorite city?',
                    'response' : 'Stalingrad!'
                }
            ], group=self.test_group)
        self.test_comment1 = Comment.objects.create(owner=self.test_user1, text="Putin is the \n Kiiing!", post= self.test_post1)
        self.test_comment2 = Comment.objects.create(owner=self.test_user1, text="Stalingrad!", post= self.test_post2)

    def test_create_comment(self):
        #check prior comment counts
        self.assertEqual(self.test_post1.comments.count(), 1)        
        self.assertEqual(self.test_post2.comments.count(), 1)
        self.assertEqual(self.test_user1.comments.count(), 2)        
        self.assertEqual(self.test_user2.comments.count(), 0)

        comment_fields =  {
            'text' : 'Kim Yong is the \n new Kiiing!',
            'post' : self.test_post1.id,
            'owner': self.test_user2.id
        }
        response = self.client.post('/api/v1/comments/', comment_fields, format='json')
        self.assertEqual(response.status_code, 201)
        #check comment counts after insertion
        self.assertEqual(self.test_post1.comments.count(), 2)        
        self.assertEqual(self.test_post2.comments.count(), 1)
        self.assertEqual(self.test_user1.comments.count(), 2)        
        self.assertEqual(self.test_user2.comments.count(), 1)

        comment_fields =  {
            'text' : 'Petersburg of course!',
            'post' : self.test_post2.id,
            'owner': self.test_user1.id
        }
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
        
        try:
            self.assertEqual(json_response['text'], self.test_comment1.text)
        except KeyError:
            self.fail("Comment should have field named 'text'")
        
        response = self.client.get('/api/v1/posts/' + str(self.test_post1.id) + '/')
        self.assertEqual(response.status_code, 200)
        json_response = json.loads(response.content)
        try:
            self.assertEqual(json_response['comments'][0]['text'], self.test_comment1.text)
        except KeyError:
            self.fail("Created comment does not exist in the posts list")

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
        c = 0
        for User in auth_models.User.objects.all():
            if query in User.username:
                c = c + 1

        return c

    def count_groups(self, query):
        d = 0
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