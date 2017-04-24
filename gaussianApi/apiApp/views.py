from django.http import HttpResponse
from apiApp.models import Profile
from apiApp.serializers import ProfileSerializer
from rest_framework import generics

def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")