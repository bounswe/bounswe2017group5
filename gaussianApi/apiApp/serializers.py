from rest_framework import serializers
from apiApp.models import Profile, Comment, Post, Group, Tag
from django.contrib.auth.models import User
 
# Create serializers here

class PostSerializer(serializers.ModelSerializer):
	class Meta:
		model = Post
		fields = ('id', 'text', 'author','group') # a field for comments is needed

class ProfileSerializer(serializers.ModelSerializer):

	class Meta:
		model = Profile
		fields = ('id', 'name', 'surname')

class UserSerializer(serializers.ModelSerializer):

	class Meta:
		model = User
		fields = ('id', 'username', 'profile')


class GroupSerializer(serializers.ModelSerializer):

	class Meta:
		model = Group
		fields = ('created', 'name', 'isPublic', 'description', 'location_lat', 'location_lon')


class CommentSerializer(serializers.ModelSerializer):

	class Meta:
		model = Comment
		fields = ('id', 'text', 'author','post')



class TagSerializer(serializers.ModelSerializer):
	
	class Meta:
		model = Tag
		fields = ('created', 'name')

