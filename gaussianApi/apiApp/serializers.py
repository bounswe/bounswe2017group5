from rest_framework import serializers
from apiApp.models import Comment


class CommentSerializer(serializers.ModelSerializer):

	class Meta:
		model = Comment
		fields = ('id', 'text', 'author')
