from django.conf.urls import url
from django.contrib import admin

from . import views
from rest_framework.authtoken import views as rf_views

app_name='api'
urlpatterns = [
    url(r'^login/$', rf_views.obtain_auth_token, name='token_auth'),
    url(r'^users/$', views.UserList.as_view(), name='users'),
    url(r'^users/(?P<pk>\d+)/$', views.UserDetail.as_view(), name='userdetail'),
    url(r'^posts/$', views.PostList.as_view(), name='posts'),
    url(r'^posts/(?P<pk>\d+)/$', views.PostDetail.as_view(), name='postdetail'),
    url(r'^tags/$', views.TagList.as_view(), name='tags'),
    url(r'^tags/(?P<pk>\d+)/$', views.TagDetail.as_view(), name='tagdetail'),
    url(r'^comments/$', views.CommentList.as_view(), name='comments'),
    url(r'^comments/(?P<pk>\d+)/$', views.CommentDetail.as_view(), name='commentdetail'),

    url(r'^data_templates/$', views.DataTemplateList.as_view(), name='datatemplates'),
    url(r'^data_templates/(?P<pk>\d+)/$', views.DataTemplateDetail.as_view(), name='datatemplatedetail'),
    url(r'^groups/$', views.GroupList.as_view(), name='groups'),
    url(r'^groups/(?P<pk>\d+)/$', views.GroupDetail.as_view(), name='groupdetail'),

    url(r'^me/$', views.CurrentUserView.as_view(), name='currentuser'),

    url(r'^users/groups/(?P<pk>\d+)/$', views.MemberGroupOperation.as_view(), name='groupoperation'),
    url(r'^search_wiki/$', views.search_wikidata, name="searchwiki"),
    
]
