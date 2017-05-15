from django.http import HttpResponse
from django.db.models import Q


from apiApp.models import Profile, Comment, Tag, User, Group, Post
from apiApp.serializers import ProfileSerializer, CommentSerializer, UserSerializer, TagSerializer, GroupSerializer, PostSerializer
from apiApp.permissions import IsOwnerOrReadOnly


from rest_framework import generics
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import authentication, permissions, status


class UserList(generics.ListCreateAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = User.objects.all()
	serializer_class = UserSerializer
	
class CommentList(generics.ListCreateAPIView):
	serializer_class = CommentSerializer

	def get_queryset(self):
		user = self.request.user
		comments = Comment.objects.filter(
			Q(post__group__isPublic=True) | Q(post__group__members__id__exact=user.id)).distinct()
		return comments

	def perform_create(self, serializer):
		serializer.save(author=self.request.user)

class CommentDetail(generics.RetrieveUpdateDestroyAPIView):
	permission_classes = [
		IsOwnerOrReadOnly
	]
	serializer_class = CommentSerializer

	def get_queryset(self):
		user = self.request.user
		comments = Comment.objects.filter(
			Q(post__group__isPublic=True) | Q(post__group__members__id__exact=user.id)).distinct()
		return comments
	
class TagList(generics.ListCreateAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class TagDetail(generics.RetrieveUpdateDestroyAPIView):
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class GroupList(generics.ListCreateAPIView):
	permission_classes = [
		permissions.IsAuthenticatedOrReadOnly
	]

	serializer_class = GroupSerializer

	def get_queryset(self):
		user = self.request.user
		if user:
			groups = Group.objects.filter(
				Q(isPublic=True) | Q(members__id__exact=user.id)).distinct()
		else:
			groups = Group.objects.filter(
				Q(isPublic=True)).distinct()

		return groups
	
	
	
class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
	permission_classes = [
		IsOwnerOrReadOnly,
		permissions.IsAuthenticatedOrReadOnly
	]

	serializer_class = GroupSerializer

	def get_queryset(self):
		user = self.request.user
		if user:
			groups = Group.objects.filter(
				Q(isPublic=True) | Q(members__id__exact=user.id)).distinct()
		else:
			groups = Group.objects.filter(isPublic=True)

		return groups

	def perform_create(self, serializer):
		serializer.save(admin=self.request.user)


class ProfileList(generics.ListCreateAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer

	def perform_create(self, serializer):
		serializer.save(user=self.request.user)

class ProfileDetail(generics.RetrieveUpdateAPIView):
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer
  
class PostList(generics.ListCreateAPIView):
	permission_classes = [
		permissions.IsAuthenticatedOrReadOnly
	]
	serializer_class = PostSerializer

	def get_queryset(self):
		user = self.request.user
		posts = Post.objects.filter(
			Q(group__isPublic=True) | Q(group__members__id__exact=user.id)).distinct()
		return posts

	def perform_create(self, serializer):
		serializer.save(author=self.request.user)
	
class PostDetail(generics.RetrieveUpdateDestroyAPIView):
	permission_classes = [
		permissions.IsAuthenticatedOrReadOnly,
		IsOwnerOrReadOnly
	]
	serializer_class = PostSerializer

	def get_queryset(self):
		user = self.request.user
		posts = Post.objects.filter(
			Q(group__isPublic=True) | Q(group__members__id__exact=user.id)).distinct()
		return posts

class RegisterView(generics.CreateAPIView):
	permission_classes = [
		permissions.AllowAny
	]
	model = User
	serializer_class = UserSerializer



def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")
	