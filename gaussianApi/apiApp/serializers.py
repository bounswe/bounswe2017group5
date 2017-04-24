from rest_framework import serializers
from apiApp.models import Profile
from django.contrib.auth.models import User


class ProfileSerializer(serializers.ModelSerializer):

	class Meta:
		model = Profile
		fields = ('id', 'name', 'surname')

class UserSerializer(serializers.ModelSerializer):

	class Meta:
		model = User
		fields = ('id', 'username', 'profile')
