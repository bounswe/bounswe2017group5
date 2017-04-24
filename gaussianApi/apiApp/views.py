from django.http import HttpResponse

from apiApp.models import Profile, Comment, Tag, User, Group, Post
from apiApp.serializers import ProfileSerializer, CommentSerializer, UserSerializer, TagSerializer, GroupSerializer, PostSerializer

from rest_framework import generics


class UserList(generics.ListAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer

class UserDetail(generics.RetrieveAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer
	
class CommentList(generics.ListAPIView):
	queryset = Comment.objects.all()
	serializer_class = CommentSerializer
	# add owner on create
	
class CommentDetail(generics.RetrieveAPIView):
	queryset = Comment.objects.all()
	serializer_class = CommentSerializer
	
class TagList(generics.ListAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class TagDetail(generics.RetrieveAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class GroupList(generics.ListAPIView):
	queryset = Group.objects.all()
	serializer_class = GroupSerializer
	
class GroupDetail(generics.RetrieveAPIView):
	queryset = Group.objects.all()
	serializer_class = GroupSerializer
	
class ProfileList(generics.ListAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer

class ProfileDetail(generics.RetrieveAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer
  
class PostList(generics.ListAPIView):
	queryset = Post.objects.all()
	serializer_class = UserSerializer
	
class PostDetail(generics.RetrieveAPIView):
	queryset = Post.objects.all()
	serializer_class = UserSerializer

def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")
	