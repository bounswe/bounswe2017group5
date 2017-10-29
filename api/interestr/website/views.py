# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from django.views import generic
from django.views.generic.edit import CreateView, UpdateView, DeleteView

from .models import Group

# Create your views here.
class GroupView(generic.ListView):
    template_name = 'templates/groups.html'

    def get_queryset(self):
        return Group.objects.all()
