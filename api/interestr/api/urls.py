from django.conf.urls import url
from django.contrib import admin

from . import views
from rest_framework.authtoken import views as rf_views

app_name='api'
urlpatterns = [
    url(r'^login/$', rf_views.obtain_auth_token, name='token_auth'),
    url(r'^users/$', views.UserList.as_view(), name='users'),
    url(r'^users/(?P<pk>\d+)/$', views.UserDetail.as_view(), name='userdetail'),
    
    url(r'^data_templates/$', views.DataTemplateList.as_view(), name='datatemplates'),
    url(r'^data_templates/(?P<pk>\d+)/$', views.DataTemplateDetail.as_view(), name='datatemplatedetail'),
]
