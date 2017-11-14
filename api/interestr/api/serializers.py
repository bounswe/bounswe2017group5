from rest_framework import serializers

from . import models as core_models
from django.contrib.auth import models as auth_models

class GroupSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Group
        fields = ('id', 'name', 'created', 'updated', 'size', 'members', 'picture', )

class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = auth_models.User
        fields = ('id', 'username', 'email',)

class PostSerializer(serializers.ModelSerializer):

	class Meta:
		model = core_models.Post
		fields = ('id', 'owner', 'text', 'group', 'data_template', 'created', 'updated', )

class DataTemplateSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'group', 'user', 'created', 'updated', 'fields' )

class TagSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Tag
        fields = ('label', 'url', 'concept_uri', 'created', 'updated')
