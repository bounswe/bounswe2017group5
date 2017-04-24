from django.conf.urls import url

from . import views
from rest_framework.authtoken import views as auth_views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^users/$', views.UserList.as_view()),
    url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view()),
    url(r'^groups/$', views.GroupList.as_view()),
    url(r'^groups/(?P<pk>[0-9]+)/$', views.GroupDetail.as_view()),
    url(r'^tags/$', views.TagList.as_view()),
    url(r'^tags/(?P<pk>[0-9]+)/$', views.TagDetail.as_view()),
    url(r'^profiles/$', views.ProfileList.as_view()),
    url(r'^profiles/(?P<pk>[0-9]+)/$', views.ProfileDetail.as_view()),
    url(r'^comments/$', views.CommentList.as_view()),
    url(r'^comments/(?P<pk>[0-9]+)/$', views.CommentDetail.as_view()),
    url(r'^posts/$', views.PostList.as_view()),
    url(r'^posts/(?P<pk>[0-9]+)/$', views.PostDetail.as_view()),
    url(r'^register/', views.RegisterView.as_view()),
    url(r'^login/', auth_views.obtain_auth_token),
]