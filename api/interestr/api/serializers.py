from rest_framework import serializers

from . import models as core_models
from django.contrib.auth import models as auth_models

class GroupSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Group
        fields = ('id', 'name', 'created', 'updated', 'size', 'members', 'moderators', 'picture', )

class UserSerializer(serializers.ModelSerializer):
    joined_groups = GroupSerializer(many=True, read_only=True)
    moderated_groups = GroupSerializer(many=True, read_only=True)

    class Meta:
        model = auth_models.User
        fields = ('id', 'username', 'email', 'joined_groups', 'moderated_groups', )

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
        fields = ('id','label', 'url','description', 'concepturi', 'created', 'updated')
