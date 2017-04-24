from rest_framework import serializers

# Create serializers here

class GroupSerializer(serializers.ModelSerializer):

	class Meta:
		model = Group
		fields = ('created', 'name', 'isPublic', 'description' , 'location_lat', 'location_lat')

