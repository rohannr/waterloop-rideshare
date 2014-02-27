from django.http import HttpResponse

from waterloop_app.models import *

import datetime
import json

def user_index(request):
    if request.method == 'POST':
        print 'data: %s' % request.raw_post_data
        postData = json.loads(request.raw_post_data)

        try:
            u = User(**postData)
            u.save()
        except Exception, e:
            return HttpResponseBadRequest(str(e))

        return HttpResponse(json.dumps({'id' : u.id}))
    elif request.method == 'GET':
        return HttpResponse("List of users! yay!")

def user_detail(request, userId):
    return HttpResponse(User.objects.filter(id=userId))

def rides(request):
    if request.method == 'POST':
        print 'data: %s' % request.raw_post_data
        postData = json.loads(request.raw_post_data)
        postData['driver_id'] = User.objects.filter(fbId=postData['driver_id'])[0].pk
        postData['datetime'] = datetime.datetime.fromtimestamp(postData['datetime'])

        try:
            r = Ride(**postData)
            r.save()
        except Exception, e:
            return HttpResponseBadRequest(str(e))

        return HttpResponse(json.dumps({'id' : r.pk}))

def search(request):
    print 'data: %s' % request.raw_post_data
    params = json.loads(request.raw_post_data)
    rideDate = datetime.datetime.fromtimestamp(float(params['datetime']))
    
    r = [str(r) for r in Ride.objects.filter(
        origin__icontains=params['origin']
    ).filter(
        destination__icontains=params['destination']
    ).filter(
        datetime__year=rideDate.year,
        datetime__month=rideDate.month,
        datetime__day=rideDate.day
    )]
    
    return HttpResponse("[" + ','.join(r) + "]")
