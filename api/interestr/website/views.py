# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.forms import model_to_dict
from django.shortcuts import render, redirect

from django.contrib.auth import authenticate, login, logout

from django.contrib.auth.forms import AuthenticationForm

from django.views import generic

from django.contrib.auth.models import User

#from django.views.generic import views
from django.views import View

from api.models import Group, ProfilePage
from api.views import recommend_groups, recommend_posts
from .forms import LoginForm, RegisterForm, CreateGroupForm, ProfileForm
import json

from django.contrib.auth.mixins import LoginRequiredMixin
from django.db.models import Q

class UserLoginView(View):
    form_class = LoginForm
    template_name = 'website/login.html'

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated():
            return redirect('website:news')
        return super(UserLoginView, self).dispatch(request, *args, **kwargs)

    #display blank form
    def get(self, request):
        form = self.form_class()
        return render(request,self.template_name)

    # process form data
    def post(self, request):

                #form = self.form_class(request.POST)
                #user = form.get_user(request)
                password = request.POST["pwd"]
                username_or_email = request.POST["username_or_email"]

                try:
                        if '@'in username_or_email:
                                username = User.objects.get(email=username_or_email).username
                        else:
                                username = username_or_email

                        user = authenticate(username=username, password=password)
                except:
                        user = None

                if user is not None:
                        if user.is_active: #if he/she is not banned
                                login(request,user)
                                return redirect('website:news')

                return render(request,self.template_name, {'err_msg': 'Invalid credentials!'})

class UserRegisterView(View):

    form_class = RegisterForm
    template_name = 'website/register.html'

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated():
            return redirect('website:groups')
        return super(UserRegisterView, self).dispatch(request, *args, **kwargs)

    #display blank form
    def get(self, request):
        form = self.form_class(None)
        return render(request,self.template_name, {'form': form})

    # process form data
    def post(self, request):
        form = self.form_class(request.POST)

        if form.is_valid():
            user = form.save(commit=False)
        else:
            return render(request,self.template_name, {'form': form})

        #cleaned (normalized) data
        username = form.cleaned_data['username']
        password = form.cleaned_data['password1']
        user.set_password(password)
        user.save()

        #returns user objects if credentials are correct
        user = authenticate(username=username, password=password)

        if user is not None:
            if user.is_active: #if he/she is not banned
                login(request,user)
                return redirect('/') #TODO
        return render(request,self.template_name, {'form': form})

class LogoutView(View):

    def get(self, request):
        logout(request)
        return redirect('website:index')

class GroupView(LoginRequiredMixin, generic.ListView):
    template_name = 'website/groups.html'

    def get_queryset(self):
        return Group.objects.filter(is_private=False)

class HomePageView(View):
    template_name = 'website/index.html'

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated():
            return redirect('website:news')
        return super(HomePageView, self).dispatch(request, *args, **kwargs)

    def get(self, request):
        return render(request, self.template_name)

class GroupDetailsView(LoginRequiredMixin, View):
    template_name = 'website/group-details.html'
    model = Group

    def get_object(self, pk):
        return self.model.objects.get(pk=pk)

    def get(self, request, pk):
        group = self.get_object(pk)
        isJoined = request.user in group.members.all()
        return render(request, self.template_name, {
            'group' : group,
            'is_joined' : isJoined
            })

class CreateGroupView(View):

    form_class = CreateGroupForm
    template_name = 'website/create-group.html'

    def get(self, request):
        form = self.form_class(None)
        return render(request,self.template_name, {'form': form})

    def post(self, request):
        form = self.form_class(request.POST, request.FILES)
        data = dict(form.data.iterlists())
        if "tags" in data.keys():
            form.fields['tags'].choices = [(id, id) for id in data["tags"]]

        if form.is_valid():
            group = form.save(commit=True)
            user = request.user
            group.members.add(user)
            group.moderators.add(user)
            group.save()
            return redirect('website:group_details', group.id)

        return render(request,self.template_name, {'form': form})

class NewsView(LoginRequiredMixin, generic.ListView):
    template_name = 'website/news.html'

    def get(self, request):
        groups = json.loads(recommend_groups(request).content)['results']
        posts = json.loads(recommend_posts(request).content)['results']
        return render(request, self.template_name, {'recommended_groups': groups, 'recommended_posts': posts})

class SearchView(LoginRequiredMixin, generic.ListView):
    template_name = 'website/search.html'

    def get(self, request):
        query = self.request.GET.get("q")
        groups = Group.objects.all()
        users = User.objects.all()
        if query:
            groups = groups.filter(Q(name__icontains=query)).distinct()
            users = users.filter(Q(username__icontains=query)).distinct()
        return render(request, self.template_name, {'groups': groups, 'users': users, 'search_term': query})

class SearchAdvancedView(LoginRequiredMixin, generic.ListView):
    template_name = 'website/search_advanced.html'

    def get(self, request):
        data_templates = [data_template for group in Group.objects.all() for data_template in group.data_templates.all()]
        return render(request, self.template_name, {'data_templates': data_templates})

class MyProfileView(LoginRequiredMixin, View):
    form_class = ProfileForm
    template_name = 'website/my_profile.html'

    def get(self, request):
        profile, _ = ProfilePage.objects.get_or_create(user=request.user)
        form = self.form_class(instance=profile)
        return render(request, self.template_name, {'form': form})

    def post(self, request):
        form = self.form_class(request.POST, request.FILES)
        profile = form.save(commit=True, user=request.user)
        return redirect('website:my_profile')


class ProfileView(View):
    model = ProfilePage
    template_name = 'website/profile.html'

    def get_object(self, pk):
        user =  User.objects.get(pk=pk)
        profile, _ = ProfilePage.objects.get_or_create(user=user)
        return profile

    def get(self, request, pk):
        profile = self.get_object(pk)
        following = request.user.profile in profile.followed_by.all()
        return render(request, self.template_name, {'profile' : profile, 
                                                    'following': following})
