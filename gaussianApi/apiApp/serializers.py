from rest_framework import serializers
from apiApp.models import Profile, Comment
from django.contrib.auth.models import User


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
		fields = ('created', 'name', 'isPublic', 'description' , 'location_lat', 'location_lat')


class CommentSerializer(serializers.ModelSerializer):

	class Meta:
		model Comment
		fields = ('id', 'text', 'author')
