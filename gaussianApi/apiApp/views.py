from django.http import HttpResponse


def index(request):
    return HttpResponse("Hello, group 5. This is our first version of API project with Django.")