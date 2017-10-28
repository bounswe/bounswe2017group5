# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, redirect

from django.contrib.auth import authenticate, login

from django.contrib.auth.forms import AuthenticationForm

from django.views import generic

#from django.views.generic import views 
from django.views import View 

from .forms import LoginForm, RegisterForm


class UserLoginView(View):
	form_class = AuthenticationForm
	#template_name = '/registration_form.html'
	template_name = 'website/login.html'

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


# Create your views here.
