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

# END
#===Mid level serializers===


class DataTemplateSimpleSerializer(serializers.ModelSerializer):

    VALID_TYPES = [
        'checkbox',
        'multisel',
        'textarea',
        'text',
        'number',
        'date',
        'email',
        'url',
        'tel',
        'select'
    ]

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'group', 'user',
                  'created', 'updated', 'fields')

    def validate_fields(self, value):
        """
        Check that fields value is in correct format.
        """
        errors = []
        
        if not value:
            errors.append("Template should have at least one field")
        else:
            legends = []

            i = 0
            for field in value:
                try:   
                    fieldType = field['type']
                    fieldLegend = field['legend']
                    fieldInputs = field['inputs']

                    if fieldType not in self.VALID_TYPES:
                        errors.append("In field[%d]: 'type' should be one of these: [ %s ]" % (i, ", ".join(self.VALID_TYPES)))

                    if not isinstance(fieldLegend, basestring):
                        errors.append("In field[%d]: 'legend' must be a string" % i)

                    if not fieldLegend.strip():
                        errors.append("In field[%d]: 'legend' must not be empty" % i)

                    if fieldLegend in legends:
                        errors.append("In field[%d]: multiple definitions of same legend found for %s" % (i, fieldLegend))
                    
                    legends.append(fieldLegend)

                except KeyError:
                    errors.append("In field[%d]: Elements should have 'type', 'legend' and 'inputs' fields." % i)
                i = i + 1

        if errors:
            raise serializers.ValidationError(errors)

        return value


class DataTemplateSerializer(DataTemplateSimpleSerializer):
    user = UserIdNameSerializer()
    group = GroupIdNameDescriptionSerializer()

    class Meta:
        model = core_models.DataTemplate
        fields = ('id', 'name', 'group', 'user',
                  'created', 'updated', 'fields')


class TagSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Tag
        fields = ('id', 'label', 'url', 'description',
                  'concepturi', 'created', 'updated', )


class GroupSerializer(serializers.ModelSerializer):
    data_templates = DataTemplateIdNameFieldsSerializer(
        many=True, read_only=True)
    tags = TagSerializer(many=True, read_only=True)
    members = UserIdNameSerializer(many=True, read_only=True)
    moderators = UserIdNameSerializer(many=True, read_only=True)

    class Meta:
        model = core_models.Group
        fields = ('id', 'name', 'description', 'location', 'created', 'updated', 'size', 'members',
                  'moderators', 'picture', 'data_templates', 'tags', )
        read_only_fields = ('members', 'moderators',)


class CommentSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer(read_only=True)

    class Meta:
        model = core_models.Comment
        fields = ('id', 'owner', 'text', 'post', 'created', 'updated',)


class VoteSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer(read_only=True)

    class Meta:
        model = core_models.Vote
        fields = ('id', 'owner', 'post', 'up', 'created', 'updated',)

class PostCreateSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Post
        fields = ('id', 'owner', 'group', 'data_template', 'data')

    def validate_data(self, value):

        errors = []

        i = 0
        for field in value:
            try:
                field_question = field['question']
                field_response = field['response']

                if not field_question.strip():
                    errors.append("In field[%d]: 'question' must not be empty." % i)

                # if not field_response.strip():
                #     errors.append("In field[%d]: 'response' must not be empty." % i)

            except KeyError:
                errors.append("Post data should have 'question' and 'response' fields")
            i = i + 1

        if errors:
            raise serializers.ValidationError(errors)

        return value


    def validate(self, data):
        try:
            template = data['data_template']
        except:
            # Default template case
            return data
        post_data = data['data']

        template_errors = []

        if len(post_data) < len(template.fields):
            template_errors.append('Post does not have enough many fields')
        elif len(post_data) > len(template.fields):
            template_errors.append('Post has more fields than its template')
        else:
            for i in range(len(post_data)):
                field_legend = template.fields[i]['legend'].strip()
                if post_data[i]['question'].strip() != field_legend:
                    template_errors.append("In field[%d]: 'question' should have been '%s'" % (i, field_legend))

        if template_errors:
            raise serializers.ValidationError({'template_errors' : template_errors})

        return data

class VoteCreateSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Vote
        fields = ('id', 'post', 'up')

class PostSerializer(serializers.ModelSerializer):
    owner = UserIdNameSerializer(read_only=True)
    group = GroupIdNameDescriptionSerializer()
    data_template = DataTemplateIdNameFieldsSerializer()
    comments = CommentSerializer(many=True, read_only=True)
    votes = VoteSerializer(many=True, read_only=True)

    class Meta:
        model = core_models.Post
        fields = ('id', 'owner', 'group', 'data_template', 'data',
                  'created', 'updated', 'comments', 'votes')

class ProfilePageSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.ProfilePage
        fields = ('id', 'name', 'surname', 'date_of_birth',
                  'location', 'interests', 'user', 'follows', 'followed_by')


class UserSerializer(serializers.ModelSerializer):
    joined_groups = GroupIdNameDescriptionSerializer(many=True, read_only=True)
    moderated_groups = GroupIdNameDescriptionSerializer(
        many=True, read_only=True)
    data_templates = DataTemplateIdNameFieldsSerializer(
        many=True, read_only=True)
    posts = PostSerializer(many=True, read_only=True)
    profilepage = ProfilePageSerializer(read_only=True, many=False)

    class Meta:
        model = auth_models.User
        fields = ('id', 'username', 'email', 'joined_groups', 'data_templates',
                  'posts', 'profilepage', 'votes', 'moderated_groups', )

class AnnotationSerializer(serializers.ModelSerializer):

    class Meta:
        model = core_models.Annotation
        exclude = ( )

    def to_representation(self, instance):
        data = super(AnnotationSerializer, self).to_representation(instance)
        json_str = {"@context": "http://www.w3.org/ns/anno.jsonld", 
            "id": "http://interestr.com/annotations/" + str(instance.anno_id), 
            "type": "Annotation",
            "created": instance.created,
            "bodyValue": instance.text,
            "target": {
            "source":  str(instance.target),
                "type": str(instance.annotype),
             "selector":  str(instance.selector)           }

            }
        if(instance.user):
            json_str["creator"] = {
                                    "id": "http://interestr.com/profile/" + str(instance.user.profile.id),
                                    "type": "Person",
                                    "name":  instance.user.profile.name + " " +  str(instance.user.profile.surname),
                                    "nickname": instance.user.username ,
                                    "email": instance.user.email
                }


        return json_str

    def to_internal_value(self, data):
        
        newdata = { "text": data["bodyValue"], 
                    "annotype": data["target"]["type"],
                    "target": data["target"]["source"],
                    "created":data["created"],
                    "selector": str(data["target"]["selector"])
                }
        if("creator" in data.keys()): 
            profile_id = data["creator"]["id"].strip("http://interestr.com/profile/")
            user_id = core_models.ProfilePage.objects.get(id=profile_id).id
            newdata["user"] =  user_id 

        

        return  super(AnnotationSerializer, self).to_internal_value(newdata)
