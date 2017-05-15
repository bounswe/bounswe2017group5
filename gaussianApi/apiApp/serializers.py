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

class SmallUserSerializer(serializers.ModelSerializer):
	profile = ProfileSerializer()

	class Meta:
		model = User
		fields = ('id', 'username', 'profile')

class GroupSerializer(serializers.ModelSerializer):
	posts = serializers.PrimaryKeyRelatedField(many=True, read_only=True)
	admin = SmallUserSerializer(read_only=True)
	members = SmallUserSerializer(many=True, read_only=True)

	class Meta:
		model = Group
		fields = ('id', 'created', 'name', 'isPublic', 'description', 'location_lat', 'location_lon', 'posts', 'admin', 'members')

class SmallGroupSerializer(serializers.ModelSerializer):
	class Meta:
		model = Group
		fields = ('id', 'name')


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
	comments = CommentSerializer(many=True, read_only=True)
	
	class Meta:
		model = Post
		fields = ('id', 'text', 'author', 'group', 'comments', 'created')



class UserSerializer(serializers.ModelSerializer):
	password = serializers.CharField(write_only=True, required=True)
	profile = ProfileSerializer(required=False)
	comments = CommentSerializer(many=True, read_only=True)
	posts = NoCommentPostSerializer(many=True, read_only=True)
	owned_groups = SmallGroupSerializer(many=True, read_only=True)


	class Meta:
		model = User
		fields = ('id', 'username', 'profile', 'comments', 'posts', 'owned_groups', 'password')

	def create(self, validated_data):
		user = User.objects.create(
			username=validated_data['username']
		)
		user.set_password(validated_data['password'])
		profile = Profile.objects.create(user=user)
		profile.save()
		user.save()
		return user

