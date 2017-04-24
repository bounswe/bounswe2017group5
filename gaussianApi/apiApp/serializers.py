from rest_framework import serializers
from apiApp.models import Profile, Comment, Post, Group, Tag
from django.contrib.auth.models import User

class TagSerializer(serializers.ModelSerializer):
	
	class Meta:
		model = Tag
		fields = ('created', 'name')

class ProfileSerializer(serializers.ModelSerializer):

	class Meta:
		model = Profile
		fields = ('id', 'name', 'surname')

class GroupSerializer(serializers.ModelSerializer):
	posts = serializers.PrimaryKeyRelatedField(many=True, read_only=True)

	class Meta:
		model = Group
		fields = ('created', 'name', 'isPublic', 'description', 'location_lat', 'location_lon', 'posts')

class CommentSerializer(serializers.ModelSerializer):
	author = serializers.ReadOnlyField(source='author.username')

	class Meta:
		model = Comment
		fields = ('id', 'text', 'author', 'post', 'created')


class NoCommentPostSerializer(serializers.ModelSerializer):
	author = serializers.ReadOnlyField(source='author.username')

	class Meta:
		model = Post
		fields = ('id', 'text', 'author', 'group')


class PostSerializer(NoCommentPostSerializer):
	comments = serializers.PrimaryKeyRelatedField(many=True, queryset=Comment.objects.all())
	
	class Meta:
		model = Post
		fields = ('id', 'text', 'author', 'group', 'comments')



class UserSerializer(serializers.ModelSerializer):
	profile = ProfileSerializer()
	comments = CommentSerializer(many=True, read_only=True)
	posts = NoCommentPostSerializer(many=True, read_only=True)

	class Meta:
		model = User
		fields = ('id', 'username', 'profile', 'comments', 'posts')

