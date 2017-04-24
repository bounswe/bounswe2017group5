from rest_framework import serializers
from apiApp.models import Comment


class TagSerializer(serializers.ModelSerializer):

	class Meta:
		model = Tag
		fields = ('created', 'name')


class CommentSerializer(serializers.ModelSerializer):

	class Meta:
		model Comment
		fields = ('id', 'text', 'author')


class TagSerializer(serializers.ModelSerializer):
	model = Tag
	fields = ('created', 'name')
