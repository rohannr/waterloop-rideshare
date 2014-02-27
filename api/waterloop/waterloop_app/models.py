from django.db import models

import datetime
import json

class User(models.Model):
    fbId = models.IntegerField(blank=False, null=False, unique=True)
    name = models.CharField(blank=False, null=False, max_length=100)

    def __unicode__(self):
        return json.dumps({'fbId' : self.fbId, 'name' : self.name})

class Ride(models.Model):
    origin = models.CharField(blank=False, null=False, max_length=50)
    destination = models.CharField(blank=False, null=False, max_length=50)
    datetime = models.DateTimeField(blank=False, null=False)
    driver = models.ForeignKey(User, related_name='driver')
    passengers = models.ManyToManyField(User, related_name='passengers')
    numSeats = models.IntegerField(blank=False, null=False)
    price = models.IntegerField(blank=False, null=False)
    link = models.CharField(max_length=255)

    def __unicode__(self):
        passengers = self.passengers.all()
        passData = [{'fbId' : p.fbId, 'name' : p.name} for p in passengers]
        return json.dumps({'origin' : self.origin,
'id' : int(self.pk),
'destination' : self.destination,
'datetime' : self.datetime.strftime('%s'),
'driver' : self.driver.fbId,
'passengers' : passData,
'numSeats' : self.numSeats,
'price' : self.price,
'link' : self.link})
