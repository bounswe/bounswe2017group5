from django.http import HttpResponse
from apiApp.models import Comment
from apiApp.serializers import CommentSerializer
from rest_framework import generics


def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")