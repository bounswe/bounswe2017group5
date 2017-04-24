from django.db import models

class Tag(models.Model):
	created = models.DateTimeField(auto_now_add=True)
	name = models.CharField(max_length=30, blank=True, default='')

	class Meta:
		ordering = ('created',)


class Group(models.Model):
	created = models.DateTimeField(auto_now_add=True)
	name = models.CharField(max_length=30, blank=True, default='')

	class Meta:
		ordering = ('created',)


class Comment(models.Model):
	author = models.ForeignKey('auth.User', related_name='comment', on_delete=models.CASCADE)
	text = models.TextField()
	created = models.DateTimeField(auto_now_add=True)

    class Meta:
    	ordering = ('created',)
