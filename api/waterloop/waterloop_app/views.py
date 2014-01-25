from django.http import HttpResponse

from waterloop_app.models import User

import json

def user_index(request):
    if request.method == 'POST':
        print request.raw_post_data
        postData = json.loads(request.raw_post_data)

        try:
            u = User(**postData)
            u.save()
        except Exception, e:
            # TODO: Custom 500 page
            return HttpResponse(str(e))

        return HttpResponse(json.dumps({'id' : u.id}))
#        return HttpResponse(request.raw_post_data)
    elif request.method == 'GET':
        return HttpResponse("List of users! yay!")

def user_detail(request, userId):
    return HttpResponse(User.objects.filter(id=userId))
