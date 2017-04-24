from rest_framework import serializers

# Create serializers here

class TagSerializer(serializers.ModelSerializer):

	class Meta:
		model = Tag
		fields = ('created', 'name')

