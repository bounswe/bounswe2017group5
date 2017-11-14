# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, redirect

from django.contrib.auth import authenticate, login, logout

from django.contrib.auth.forms import AuthenticationForm

from django.views import generic

from django.contrib.auth.models import User

#from django.views.generic import views
from django.views import View

from api.models import Group
from .forms import LoginForm, RegisterForm, CreateGroupForm, CreatePostForm

from django.contrib.auth.mixins import LoginRequiredMixin

class UserLoginView(View):
    form_class = LoginForm
    template_name = 'website/login.html'

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated():
            return redirect('website:groups')
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
				print username
			else:
				username = username_or_email
			
			user = authenticate(username=username, password=password)
		except:
			user = None 

		if user is not None:
			if user.is_active: #if he/she is not banned 
				login(request,user)
				return redirect('website:groups') 
		
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
            return redirect('website:groups')
        return super(HomePageView, self).dispatch(request, *args, **kwargs)

    def get(self, request):
        return render(request, self.template_name)

class GroupDetailsView(LoginRequiredMixin, View):
    template_name = 'website/group-details.html'
    form_class = CreatePostForm
    model = Group

    def get_object(self, pk):
        return self.model.objects.get(pk=pk)

    def get(self, request, pk):
        form = self.form_class()
        group = self.get_object(pk)
        isJoined = request.user in group.members.all()
        return render(request, self.template_name, {
            'post_create_form': form,
            'group' : group,
            'is_joined' : isJoined
            })

    def post(self, request, pk):
        form = self.form_class(request.POST, request.FILES)

        if form.is_valid():
            post = form.save(request.user, self.get_object(pk), commit=True)
            return redirect('website:group_details', self.get_object(pk).id)

        return render(request, self.template_name, {'post_create_form': form, 'group' : self.get_object(pk) })

class CreateGroupView(View):

    form_class = CreateGroupForm
    template_name = 'website/create-group.html'

    def get(self, request):
        form = self.form_class(None)
        return render(request,self.template_name, {'form': form})

    def post(self, request):
        form = self.form_class(request.POST, request.FILES)

        if form.is_valid():
            group = form.save(commit=True)
            return redirect('website:group_details', group.id)

        return render(request,self.template_name, {'form': form})

class NewsView(LoginRequiredMixin, generic.ListView):
    template_name = 'website/news.html'

    def get_queryset(self):
        return None