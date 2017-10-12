from django.http import HttpResponse
from django.db.models import Q


from apiApp.models import Profile, Comment, Tag, User, Group, Post
from apiApp.serializers import ProfileSerializer, CommentSerializer, UserSerializer, TagSerializer, GroupSerializer, PostSerializer
from apiApp.permissions import IsOwnerOrReadOnly


from rest_framework import generics
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import authentication, permissions, status


class UserList(generics.ListAPIView):
	"""
	Retrieve a list of users.
	"""

	queryset = User.objects.all()
	serializer_class = UserSerializer

class UserDetail(generics.RetrieveAPIView):
	"""
	Retrieve information of a specific User.
	"""
	queryset = User.objects.all()
	serializer_class = UserSerializer
	
class CommentList(generics.ListCreateAPIView):
	"""
	get:
	Retrieve a list of Comments. Comments from private groups that the User has not joined, are hidden.

	post:
	Create a new Comment. Should have a parent post.
	"""

	serializer_class = CommentSerializer

	def get_queryset(self):
		user = self.request.user
		comments = Comment.objects.filter(
			Q(post__group__isPublic=True) | Q(post__group__members__id__exact=user.id)).distinct()
		return comments

	def perform_create(self, serializer):
		serializer.save(author=self.request.user)

class CommentDetail(generics.RetrieveUpdateDestroyAPIView):
	"""
	get:
	Get details of a specific Comment.

	put:
	Edit an existing Comment that belongs to the authenticated User.

	patch:
	Edit an existing Comment that belongs to the authenticated User.

	delete:
	Delete an existing Comment that belongs to the authenticated User.
	"""
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
	"""
	get:
	Retrieve a list of Tags.

	post:
	Create a new Tag.
	"""
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class TagDetail(generics.RetrieveUpdateDestroyAPIView):
	"""
	get:
	Get details of a specific Tag.

	put:
	Edit an existing Tag.

	patch:
	Edit an existing Tag.

	delete:
	Delete an existing Tag.
	"""
	queryset = Tag.objects.all()
	serializer_class = TagSerializer
	
class GroupList(generics.ListCreateAPIView):
	"""
	get:
	Retrieve a list of Groups. Groups that are private, are not visible.

	post:
	Create a new Group. Authenticated User will be the admin of the created group.
	"""
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
	"""
	get:
	Get details of a specific Group. Groups that are private will return 404 Not Found.

	put:
	Edit an existing Group. The authenticated user should be the admin of the Group.

	patch:
	Edit an existing Group. The authenticated user should be the admin of the Group.

	delete:
	Delete an existing Group. The authenticated user should be the admin of the Group.
	"""
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
		

class ProfileList(generics.ListAPIView):
	"""
	get:
	Retrieve a list of Profiles.
	"""
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer

class ProfileDetail(generics.RetrieveUpdateAPIView):
	"""
	get:
	Retrieve a specific Profile.

	put:
	Edit an existing Profile.

	patch:
	Edit an existing Profile.
	"""
	queryset = Profile.objects.all()
	serializer_class = ProfileSerializer
  
class PostList(generics.ListCreateAPIView):
	"""
	get:
	Retrieve a list of Posts. Posts that are posted to a private 
	Group are not visible unless the authenticated User is a member 
	of the private Group.

	post:
	Create a Post on a Group. The authenticated User should be a
	member of the Group.
	"""
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

	"""
	get:
	Retrieve a specific Post. The Group of the Post must be public.
	Or the authenticated User should be a member of this Group.

	put:
	Edit an existing Post. The authenticated User must be the
	author of this Post.

	patch:
	Edit an existing Post. The authenticated User must be the
	author of this Post.

	delete:
	Delete an existing Post. The authenticated User must be the
	author of this Post.
	"""
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
	"""
	post:
	Registers a User. Allowed for non-authenticated Users, too.

	"""
	permission_classes = [
		permissions.AllowAny
	]
	model = User
	serializer_class = UserSerializer



def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")
	