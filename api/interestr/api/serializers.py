from rest_framework import serializers

from . import models as core_models
from django.contrib.auth import models as auth_models

from data_templates import models as data_templates_models


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
		fields = ('id', 'owner', 'text', 'content_url', 'group', 'data_template', 'created', 'updated', )

class DataTemplateSerializer(serializers.ModelSerializer):

    class Meta:
        model = data_templates_models.DataTemplate
        fields = ('id', 'name', 'group', 'user', 'created', 'updated', 'fields' )
