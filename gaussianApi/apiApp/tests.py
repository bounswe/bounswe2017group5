# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from apiApp.models import Profile, Group

from django.test import TestCase
from rest_framework import status
from rest_framework.test import APITestCase
from django.contrib.auth.models import User
from apiApp.models import Post, Comment

class LogInRegisterTests(APITestCase):

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
		# Every user must be created with its Profile
		self.assertEqual(Profile.objects.count(), 1)
		# The initial user doesn't have a Profile. So it has to be 1.
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

class UserTests(APITestCase):

	def setUp(self):
		user = User.objects.create_user('testUser1', 'testEmail@testEmail.com', 'passTestUser')
		user2 = User.objects.create_user('testUser2', 'testEmail2@testEmail.com', 'passTestUser2')
		# Creating profiles for users
		profile = Profile.objects.create(user=user)
		profile2 = Profile.objects.create(user=user2)
		self.client.force_authenticate(user=user)

	def test_getUsers(self):
		"""
		The list of users is visible to every user in the system.
		"""
		url = "/users/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 2)

	def test_getUser(self):
		"""
		It's possible to get information of a single User.
		"""
		url = "/users/2/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["id"], 2)
		self.assertEqual(response.data["username"], "testUser2")

	def test_editProfile(self):
		"""
		Profiles can be edited by their respective users.
		"""
		url = "/profile/1/"
		data = {"name" : "testName", "surname" : "testSurname"}
		response = self.client.patch(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["name"], "testName")
		self.assertEqual(response.data["surname"], "testSurname")



class GroupTests(APITestCase):

	def setUp(self):
		user = User.objects.create_user('testUser1', 'testEmail@testEmail.com', 'passTestUser')
		user2 = User.objects.create_user('testUser2', 'testEmail2@testEmail.com', 'passTestUser2')
		user3 = User.objects.create_user('testUser3', 'testEmail3@testEmail.com', 'passTestUser3')
		group = Group.objects.create(admin=user2, name='testGroup', isPublic=False, 
			description='This is a test group.', location_lat='12345', location_lon='12345')
		group2 = Group.objects.create(admin=user3, name='testGroup2', isPublic=True, 
			description='This is another test group.', location_lat='12345', location_lon='12345')
		# self.client.force_authenticate(user=user)

	def test_getGroups(self):
		"""
		All groups are visible to everyone unless it's a private group.
		"""
		url = "/groups/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 1)

	def test_getGroup(self):
		"""
		All public groups can be retrieved from api.
		"""
		url = "/groups/2/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["id"], 2)
		self.assertEqual(response.data["name"], "testGroup2")

	def test_createGroup(self):
		"""
		Anyone can create a group.
		"""
		self.client.force_authenticate(user=User.objects.get(id=1))
		url = "/groups/"
		data = {
			'name' : 'testGroup3',
			'description' : 'This is another test group that just created.',
			'isPublic' : True
		}
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.data["id"], 3)
		self.assertEqual(response.data["name"], 'testGroup3')
		
	def test_editGroup(self):
		"""
		Users can only edit their own groups.
		"""
		user = User.objects.get(id=1)
		self.client.force_authenticate(user=user)
		group = Group.objects.create(admin=user, name='testGroup3', isPublic=True, 
			description='This is another test group that just created.')

		url = "/groups/3/"
		data = {
			'name' : 'anotherTestGroup'
		}

		response = self.client.patch(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["name"], 'anotherTestGroup')

		url = "/groups/1/"
		response = self.client.patch(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)


	def test_removeGroup(self):
		"""
		Users can only delete their own groups.
		"""
		user = User.objects.get(id=1)
		self.client.force_authenticate(user=user)
		group = Group.objects.create(admin=user, name='testGroup3', isPublic=True, 
			description='This is another test group that just created.')

		url = "/groups/3/"
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

		url = "/groups/1/"
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)


class CommentTests(APITestCase):
	
	def addComment(self, data):
		url = '/comments/'
		return self.client.post(url, data)

	def setUp(self):
		'''
		   create some initial users, groups and posts
		'''
		user1 = User.objects.create_user('testUser1', 'testEmail@testEmail.com', 'passTestUser') 
		user2 = User.objects.create_user('testUser2', 'testEmail2@testEmail.com', 'passTestUser2')
		user3 = User.objects.create_user('testUser3', 'testEmail3@testEmail.com', 'passTestUser3')

		group1 = Group.objects.create(admin=user2, name='testGroup', isPublic=False, 
			description='This is a test group.', location_lat='12345', location_lon='12345')
		group1.members= [1,2]
		group2 = Group.objects.create(admin=user3, name='testGroup2', isPublic=True, 
			description='This is another test group.', location_lat='12345', location_lon='12345')
		group1.members= [1,3]

		self.client.force_authenticate(user=user2) # this user belongs to group 1 but not to group 2

		post1 = Post.objects.create(text='Hello, I am admin in this group.', group= group1, author=user1);
		post2 = Post.objects.create(text='Hello, this is my second post.', group= group1, author=user2);
		post3 = Post.objects.create(text='Hello, I am not an admin in this group.', group= group2, author=user1);

	def test_createComment(self):


		data = {'text' : 'What a nice post.', 'post' : 1} #comment to an existing post
		url = '/comments/'
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(Comment.objects.count(), 1)

		data = {'text' : 'What a nice post.I keep sayig this.', 'post' : 1} #comment to the same post
		url = '/comments/'
		response = self.client.post(url, data, format='json')
		self.assertEqual(Comment.objects.count(), 2)

		data = {'text' : 'Another brilliant post.', 'post' : 2} #comment to the another post
		url = '/comments/'
		response = self.client.post(url, data, format='json')
		self.assertEqual(Comment.objects.count(), 3)


		data = {'text' : 'Oops.', 'post' : 5} #comment to the a non-existing post
		url = '/comments/'
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
		self.assertEqual(Comment.objects.count(), 3)

		data = {'text' : 'What a nice post...', 'post' : 3} #comment to the another post which is posted in a group which doesn't contain this user
		url = '/comments/'
		response = self.client.post(url, data, format='json')
		self.assertEqual(Comment.objects.count(), 4)
	 		
	def test_getCommments(self):

		self.addComment({'text' : 'What a nice post.', 'post' : 1})
		self.addComment({'text' : 'What a nice post.I keep sayig this.', 'post' : 2})
		self.addComment({'text' : 'Another brilliant post.', 'post' : 2})
		self.addComment({'text' : 'What a nice post...', 'post' : 3}) 


		url = '/comments/'
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 3)

	def test_getCommment(self):

		url = '/comments/2/' # see a comment that is posted to a group this user belongs
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["post"], 1)
		self.assertEqual(response.data["text"], 'What a nice post.I keep sayig this.')

		url = '/comments/3/' # see a comment that is posted to a group this user does not belong
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

	def test_deleteComment(self):

		self.addComment({'text' : 'What a nice post.', 'post' : 1})
		self.addComment({'text' : 'What a nice post.I keep sayig this.', 'post' : 1})
		self.addComment({'text' : 'What a nice post...', 'post' : 2});


		url = '/comments/2/' # delete self created comment
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

		url = '/comments/'  
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 2)

		url = '/comments/3/' # delete some comment created by another user
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

		url = '/comments/'  
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 2)



class PostTests(APITestCase):

	def setUp(self):
		user = User.objects.create_user('testUser1', 'testEmail@testEmail.com', 'passTestUser')
		user2 = User.objects.create_user('testUser2', 'testEmail2@testEmail.com', 'passTestUser2')
		user3 = User.objects.create_user('testUser3', 'testEmail3@testEmail.com', 'passTestUser3')
		user4 = User.objects.create_user('testUser4', 'testEmail4@testEmail.com', 'passTestUser4')
		group = Group.objects.create(admin=user2, name='testGroup', isPublic=False, 
			description='This is a test group.')
		group2 = Group.objects.create(admin=user3, name='testGroup2', isPublic=True, 
			description='This is another test group.')
		group3 = Group.objects.create(admin=user4,members=[user], name='testGroup3',isPublic=True,
			description='This is a group for music lovers')

		post1 = Post.objects.create(author=user2,text="Beethoven",group=group)
		post2 = Post.objects.create(author=user3,text="Rachmaninoff",group=group2)
		post3 = Post.objcts.create(author=user,text="Teoman",group=group3)	

	def test_createPost(self):
		"""
		If user is a member of the group, he/she can create post
		"""
		self.client.force_authenticate(user=User.objects.get(id=1))
		url = "/posts/"
		data = {
			'text' : 'Vivaldi',
			'group': 3

		}
		response = self.client.post(url, data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.data["id"], 4)
		self.assertEqual(response.data["text"], 'Vivaldi')
		self.assertEqual(Post.objects.count(), 4)

	def test_removePost():
		"""
		Users can only delete their own post.
		"""
		self.client.force_authenticate(user = User.objects.get(id=3))

		post5 = Group.objects.create(author=user, group=group3, text="Mahmut is best computer geek I have ever met")

		url = "/post/4/"
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

		url = "/post/1/"
		response = self.client.delete(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

    def test_getPosts(self):
		"""
		All posts
		"""
		url = "/posts/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["count"], 3)


	def test_getPost(self):
		"""
		get specific Post
		"""
		url = "/posts/2/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code, status.HTTP_200_OK)
		self.assertEqual(response.data["id"], 2)
		self.assertEqual(response.data["text"], "Rachmaninoff")	

		url = "/posts/1/"
		response = self.client.get(url, format='json')
		self.assertEqual(response.status_code,status.HTTP_401_UNAUTHORIZED)




