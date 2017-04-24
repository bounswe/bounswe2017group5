from rest_framework import serializers

# Create serializers here
class TagSerializer(serializers.ModelSerializer):
	model = Tag
	fields = ('created', 'name')
