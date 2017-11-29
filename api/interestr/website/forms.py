from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django import forms
from api.models import Group, Post
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.core.exceptions import ValidationError


class LoginForm(forms.Form):

	username_or_email = forms.CharField(label="Username/Email",widget = forms.TextInput(attrs={'placeholder': 'Username/Email'}))
	password = forms.CharField(label="Password", widget = forms.PasswordInput(attrs={'placeholder': 'Password'}))

	#class Meta:
	#	model = User
	#	fields = ['username_or_email', 'password']
	
def UniqueEmailValidator(value):
	if User.objects.filter(email__iexact=value).exists():
		raise ValidationError('User with this Email already exists.')


class RegisterForm(UserCreationForm):

	username = forms.CharField(label="Username",widget = forms.TextInput(attrs={'placeholder': 'Username', 'class':"form-control"}))
	password1 = forms.CharField(label="Password", widget = forms.PasswordInput(attrs={'placeholder': 'Password', 'class':"form-control"}))
	password2 = forms.CharField(label="Confirm Password", widget = forms.PasswordInput(attrs={'placeholder': 'Confirm Password', 'class':"form-control"}))
	email = forms.EmailField(required=True,label="Email Address", widget = forms.EmailInput(attrs={'placeholder': 'Email Address', 'class':"form-control"}))

	def __init__(self, *args, **kwargs):
		super(RegisterForm, self).__init__(*args, **kwargs)
		self.fields['email'].validators.append(UniqueEmailValidator)

	class Meta:
		model = User
		fields = ("username", "email", "password1", "password2")

	def save(self, commit=True):
		user = super(RegisterForm, self).save(commit=False)
		user.email = self.cleaned_data["email"]
		if commit:
			user.save()
		return user

class CreateGroupForm(forms.ModelForm):
	name = forms.CharField(widget = forms.TextInput(attrs={'placeholder': 'Name of the group', 'class':"form-control"}))
	description = forms.CharField(widget = forms.Textarea(attrs={'placeholder': 'Description', 'class':"form-control"}))
	tags = forms.MultipleChoiceField(required=False, widget = forms.SelectMultiple(attrs={'placeholder': 'Tags', 'class':"form-control", 'id': 'create-group-tag-select'}))
	location = forms.CharField(required=False, widget = forms.TextInput(attrs={'placeholder': 'Location', 'class':"form-control"}))
	is_private = forms.BooleanField(required=False, widget = forms.CheckboxInput())
	picture = forms.ImageField(required=False, widget = forms.FileInput())


	class Meta:
		model = Group
		fields = ['name','description','tags','location','is_private','picture']
