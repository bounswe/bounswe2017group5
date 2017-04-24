from rest_framework import serializers
from apiApp.models import Profile

class ProfileSerializer(serializers.ModelSerializer):

	class Meta:
		model = Profile
		fields = ('id', 'name', 'surname')

