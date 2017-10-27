from django.conf.urls import url
from django.contrib import admin

from . import views


app_name='api'
urlpatterns = [
    #user apis
    url(r'^users/$', views.UserList.as_view(), name='users'),
    url(r'^users/(?P<pk>\d+)/$', views.UserDetail.as_view(), name='userdetail'),

    #group apis
    url(r'^groups/$', views.GroupList.as_view(), name='groups'),
    url(r'^groups/(?P<pk>\d+)/$', views.GroupDetail.as_view(), name='groupDetail'),
]
