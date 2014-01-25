import facebook 
import re
from datetime import date, timedelta, datetime
import dateutil.parser
import MySQLdb

HOST = 'sidprak03.sidprak.com'
user = 'root'
passwd = 'mysql12$'
db = 'waterloop'

# xdb = MySQLdb.connect(HOST, user, passwd, db)
# cursor = xdb.cursor()

OAUTH_TOKEN = 'CAACEdEose0cBAE1j9GeYS3QyMvndH3wimHNul42KiZACcSBxZAFp0orLZBnIoTD0W08WAv1VGvTxDVcJDngp6GKiHKlHK9WaZAgF2Mk85oBlRD7Lk2SgL5jgT47QqVTbuTk4pBALpBIS0fr3SqMVRbHlhJPIxJ0HtZB9HvVfAqdsohxGZC1zx7XSvA6371B3TOlPmFhj1xHQZDZD'

FB_RIDE_GROUP_ID = '372772186164295'

graph = facebook.GraphAPI(OAUTH_TOKEN)

feed = graph.get_connections(FB_RIDE_GROUP_ID, "feed")['data']
days = { 'monday':0, 'tuesday':1, 'wednesday':3, 'thursday':4, 'friday':4, 'saturday':5, 'sunday':6}

def find_date(create_date, message):
    """Returns nearest datetime.date() of travel based on day of 
    week in message"""
   
    offset = 0
    for day,val in days.iteritems():
        if day in message.lower():
           offset = val - create_date.weekday()
           if offset > 0:
               date = create_date + timedelta(offset)
           else:
               date = create_date + timedelta(offset%7)
           return date 
    return False
 

for ride in feed:
    user_fbid = ride['from']['id']
    user_name = ride['from']['name']
    message = ride['message']
    link = ride['actions'][0]['link']
    create_date = dateutil.parser.parse(ride['created_time']).date()
    
    ride_date = find_date(create_date, message) 
#    
#     capture = re.findall('|'.join(['from\s([\w\s]+)to\s([\w\s]+)',\
#             '([\w\s]+)to\s([\w\s]+)',\
#             '(\w+)\s-+>\s([\w\s]+)']), message)

    capture = re.findall('from\s([\w\s/]+)\sto\s(\w+)', message)

    try:
        origin = capture[0][0] or 'Waterloo'
        destination = capture[0][1] or ''
    except:
        print "Error parsing message %s" % message
        continue 
    if not (ride_date and origin and destination):
        continue

    time = re.findall('at(\w\s)+', message) or '<no time>'
    seat_match = re.findall('(\d)\sseats\s', message)
    num_seats = seat_match[0][0] if seat_match else 3 

    try:
        price = re.findall('\$(\d+)$', message)[0][0]
    except:
        price = '$5'
    
    print "FBID: %s\nUSER: %s\nORIGIN: %s\nDESTINATION: %s\nDATE: %s\nPRICE: %s\nSEATS: %s\nLINK: %s\n\n" % (user_fbid, user_name, origin, 
            destination, ride_date.strftime('%Y-%m-%d'),
            price, num_seats, link)
    
    query = "INSERT INTO USERS WATERLOO_APP_USERS VALUES ("


   
