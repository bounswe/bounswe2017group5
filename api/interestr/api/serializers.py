from rest_framework import serializers

from . import models as core_models
from django.contrib.auth import models as auth_models


class GroupSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Group
        fields = ('name', 'created', 'updated', )

class UserSerializer(serializers.ModelSerializer):

    class Meta:
        model = auth_models.User
        fields = ('username', 'email',)
