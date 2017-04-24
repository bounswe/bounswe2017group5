from rest_framework import serializers
from apiApp.models import Comment


class GroupSerializer(serializers.ModelSerializer):

	class Meta:
		model = Group
		fields = ('created', 'name', 'isPublic', 'description' , 'location_lat', 'location_lat')


class CommentSerializer(serializers.ModelSerializer):

	class Meta:
		model Comment
		fields = ('id', 'text', 'author')


class TagSerializer(serializers.ModelSerializer):
	model = Tag
	fields = ('created', 'name')
