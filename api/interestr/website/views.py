# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, redirect

from django.contrib.auth import authenticate, login, logout

from django.contrib.auth.forms import AuthenticationForm

from django.views import generic

#from django.views.generic import views
from django.views import View

from api.models import Group
from .forms import LoginForm, RegisterForm, CreateGroupForm

from django.contrib.auth.mixins import LoginRequiredMixin

class UserLoginView(View):
	form_class = AuthenticationForm
	#template_name = '/registration_form.html'
	template_name = 'website/login.html'

	def dispatch(self, request, *args, **kwargs):
		if request.user.is_authenticated():
			return redirect('website:groups')
		return super(UserLoginView, self).dispatch(request, *args, **kwargs)

	#display blank form
	def get(self, request):
		form = self.form_class(request)
		return render(request,self.template_name, {'form': form})

	# process form data
	def post(self, request):
		form = self.form_class(request, request.POST)

		if form.is_valid():
			user = form.get_user()

			if user is not None:
				if user.is_active: #if he/she is not banned
					login(request,user)
					return redirect('/') #TODO

		return render(request,self.template_name, {'form': form})

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
        return Group.objects.all()

class HomePageView(View):
	template_name = 'website/index.html'

	def dispatch(self, request, *args, **kwargs):
		if request.user.is_authenticated():
			return redirect('website:groups')
		return super(HomePageView, self).dispatch(request, *args, **kwargs)

	def get(self, request):
		return render(request, self.template_name)
  

class CreateGroupView(View):

	form_class = CreateGroupForm
	template_name = 'website/create-group.html'

	def get(self, request):
		form = self.form_class(None)
		return render(request,self.template_name, {'form': form})

	def post(self, request):
		form = self.form_class(request.POST)

		if form.is_valid():

			group = form.save(commit=True)
			return redirect('/') #TODO

		return render(request,self.template_name, {'form': form})
