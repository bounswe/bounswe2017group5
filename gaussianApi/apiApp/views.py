from django.http import HttpResponse
from apiApp.models import Profile, Comment, Tag
from apiApp.serializers import ProfileSerializer, CommentSerializer
from rest_framework import generics

class UserList(generics.ListAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer

class UserDetail(generics.RetrieveAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer
	
class CommentList(generics.ListAPIView)
	queryset = Comment.object.all()
	serializer_class = CommentSerializer
	
class CommentDetail(generics.RetrieveAPIView):
	queryset = Comment.object.all()
	serializer_class = CommentSerializer
	
class TagList(generics.ListAPIView)
	queryset = Tag.object.all()
	serializer_class = TagSerializer
	
class TagDetail(generics.RetrieveAPIView):
	queryset = Tag.object.all()
	serializer_class = TagSerializer
	
class GroupList(generics.ListAPIView)
	queryset = Group.object.all()
	serializer_class = GroupSerializer
	
class GroupDetail(generics.RetrieveAPIView):
	queryset = Group.object.all()
	serializer_class = GroupSerializer
	
class ProfileList(generics.ListAPIView)
	queryset = Profile.object.all()
	serializer_class = ProfileSerializer
	
class ProfileDetail(generics.RetrieveAPIView):
	queryset = Profile.object.all()
	serializer_class = ProfileSerializer

def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")
	