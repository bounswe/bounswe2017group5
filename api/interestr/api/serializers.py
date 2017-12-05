from rest_framework import serializers

from . import models as core_models
from django.contrib.auth import models as auth_models

#===Mid level serializers===
# These will help us display the data much better with
# the other serializers.

class UserIdNameSerializer(serializers.ModelSerializer):

    class Meta:
        model = auth_models.User
        fields = ('id', 'username', )

class GroupIdNameDescriptionSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = core_models.Group
        fields = ('id', 'name', 'description', )

class DataTemplateIdNameFieldsSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'fields', )

#END
#===Mid level serializers===

class DataTemplateSimpleSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'group', 'user', 'created', 'updated', 'fields' )


class DataTemplateSerializer(serializers.ModelSerializer):
    user = UserIdNameSerializer()
    group = GroupIdNameDescriptionSerializer()

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'group', 'user', 'created', 'updated', 'fields' )


class TagSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Tag
        fields = ('id','label', 'url','description', 'concepturi', 'created', 'updated', )


class GroupSerializer(serializers.ModelSerializer):
    data_templates = DataTemplateIdNameFieldsSerializer(many=True, read_only=True)
    tags = TagSerializer(many=True, read_only=True)
    members = UserIdNameSerializer(many=True, read_only=True)
    moderators = UserIdNameSerializer(many=True, read_only=True)

    class Meta:
        model = core_models.Group
        fields = ('id', 'name','description', 'location', 'created', 'updated', 'size', 'members',
         'moderators', 'picture', 'data_templates', 'tags', )
        read_only_fields = ('members', 'moderators',)


class CommentSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer()

    class Meta:
        model = core_models.Comment
        fields = ('id', 'owner', 'text', 'post', 'created', 'updated',)

class VoteSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer()

    class Meta:
        model = core_models.Vote
        fields = ('id', 'owner', 'post', 'up', 'created', 'updated',)

class PostSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer()
    group = GroupIdNameDescriptionSerializer()
    data_template = DataTemplateIdNameFieldsSerializer()
    comments = CommentSerializer(many=True, read_only=True)
    votes = VoteSerializer(many=True, read_only=True)

    class Meta:
        model = core_models.Post
        fields = ('id', 'owner', 'group', 'data_template', 'data', 'created', 'updated', 'comments', 'votes')


class ProfilePageSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.ProfilePage
        fields = ('id', 'name', 'surname', 'date_of_birth',
         'location', 'interests', 'user')


class UserSerializer(serializers.ModelSerializer):
    joined_groups = GroupIdNameDescriptionSerializer(many=True, read_only=True)
    moderated_groups = GroupIdNameDescriptionSerializer(many=True, read_only=True)
    data_templates = DataTemplateIdNameFieldsSerializer(many=True, read_only=True)
    posts = PostSerializer(many=True, read_only=True)
    profilepage = ProfilePageSerializer(read_only=True, many=False)

    class Meta:
        model = auth_models.User
        fields = ('id', 'username', 'email', 'joined_groups', 'moderated_groups',
         'data_templates', 'posts', 'profilepage', 'votes', )
