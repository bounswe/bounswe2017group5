# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.shortcuts import redirect
from django.contrib.auth import authenticate, login

from django.views import generic
from django.views.generic.edit import CreateView, UpdateView, DeleteView

from .models import Group

# Create your views here.
class GroupView(generic.ListView):
    if not request.user.is_authenticated():
        return redirect('login')
    else:
        template_name = 'templates/groups.html'
        def get_queryset(self):
            return Group.objects.all()
