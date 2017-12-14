from . import models

def responseErrorMessage(response, functionality, shouldNotFail=True):
	return '%s, %s with\n Status Code: %d\n Response: %s\n' % (
			functionality,
			'fails' if shouldNotFail else 'does not fail',
			response.status_code,
			response.content
		)

def createDummyComment(user, post, i=0):
	return models.Comment.objects.create(
			owner=user,
			post=post,
			text='Dummy Comment %d' % i
		)

def createDummyPost(user, group, data_template, i=0):
	return models.Post.objects.create(
			owner=user,
			group=group,
			data_template=data_template,
			data=[
				{
					'question' : 'Dummy Question %d.1' % i,
					'response' : 'Dummy Response %d.1' % i
				},
				{
					'question' : 'Dummy Question %d.2' % i,
					'response' : 'Dummy Response %d.2' % i
				},
			]
		)

def createDummyGroup(i=0):
	return models.Group.objects.create(
			name="DummyGroup %d" % i,
			description="DummyDescription %d" % i,
			location="DummyLocation %d" % i,
			is_private=False
		)


def createDummyDataTemplate(user, group, i=0):
	return models.DataTemplate.objects.create(
			name="DummyTemplate %d" % i,
			user=user,
			group=group,
			fields=[
				{
					'type' : 'text',
					'legend' : 'Wow'
				}
			]
		)