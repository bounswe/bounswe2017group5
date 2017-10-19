from django.conf.urls import url
from django.contrib import admin

from . import views


app_name='api'
urlpatterns = [
    url(r'^users/$', views.UserList.as_view(), name='users'),
    url(r'^users/(?P<pk>\d+)/$', views.UserDetail.as_view(), name='userdetail'),
]
