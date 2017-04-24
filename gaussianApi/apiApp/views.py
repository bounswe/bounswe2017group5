from django.http import HttpResponse

from apiApp.models import Profile, Comment, Tag, User, Group, Post
from apiApp.serializers import ProfileSerializer, CommentSerializer, UserSerializer, TagSerializer, GroupSerializer, PostSerializer

from rest_framework import generics
from rest_framework.decorators import api_view


class UserList(generics.ListCreateAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer
	
class CommentList(generics.ListCreateAPIView):
	queryset = Comment.objects.all()
	serializer_class = CommentSerializer

	def perform_create(self, serializer):
		serializer.save(author=self.request.user)
	
class CommentDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Comment.objects.all()
	serializer_class = CommentSerializer
	
class TagList(generics.ListCreateAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class TagDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class GroupList(generics.ListCreateAPIView):
	queryset = Group.objects.all()
	serializer_class = GroupSerializer
	
class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Group.objects.all()
	serializer_class = GroupSerializer

class ProfileList(generics.ListCreateAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer

	def perform_create(self, serializer):
		serializer.save(user=self.request.user)

class ProfileDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer
  
class PostList(generics.ListCreateAPIView):
	queryset = Post.objects.all()
	serializer_class = PostSerializer

	def perform_create(self, serializer):
		serializer.save(author=self.request.user)
	
class PostDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Post.objects.all()
	serializer_class = PostSerializer

@api_view(['POST'])
def createUser(request):
	serialized = UserSerializer(data=request.data)
	if serialized.is_valid():
		User.objects.create_user(
			serialized.init_data['username'],
			serialized.init_data['password']
		)
		return Response(serialized.data, status=status.HTTP_201_CREATED)
	else:
		return Response(serialized.errors, status=status.HTTP_400_BAD_REQUEST)

def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")
	