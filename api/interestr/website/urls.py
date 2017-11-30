from django.conf.urls import url, include

from . import views


app_name = 'website'

urlpatterns = [
    url(r'^$', views.HomePageView.as_view(), name='index'),
    url(r'^login/$', views.UserLoginView.as_view(), name='login'),
    url(r'^register/$', views.UserRegisterView.as_view(), name='register'),
    url(r'^groups/$', views.GroupView.as_view(), name='groups'),
    url(r'^logout/$', views.LogoutView.as_view(), name='logout'),
    url(r'^news/$', views.NewsView.as_view(), name='news'),
    url(r'^search/$', views.SearchView.as_view(), name='search'),
    url(r'^profile/$', views.MyProfileView.as_view(), name='my_profile'),
    url(r'^profile/(?P<pk>\d+)/$', views.ProfileView.as_view(), name='profile'),
    url(r'^groups/(?P<pk>\d+)/$', views.GroupDetailsView.as_view(), name='group_details'),
    url(r'^groups/create/$', views.CreateGroupView.as_view(), name='create_group'),
]
