import facebook 
import re

OAUTH_TOKEN = 'CAACEdEose0cBAMcMEv03owJaqPs8ZBJbGszZCwQp2zthLTOOuvXuIVlEvQVoncX28nxzExO3IKgJy0VhWZAEOvZCqKQkKAfFvCKoFzcgsG1eV0Gthfh3UTSqJNa1W57PmkGWCDaZAjV3DshFeEjQSiA2im8ZAmZASX0sWyPYmccEHZABAwYI0ZBNW5l6B3Uo6tvMQgKneb7e5ZCgZDZD'

FB_RIDE_GROUP_ID = '372772186164295'

graph = facebook.GraphAPI(OAUTH_TOKEN)

feed = graph.get_connections(FB_RIDE_GROUP_ID, "feed")['data']

for ride in feed:
    user_fbid = ride['from']['id']
    user_name = ride['from']['name']
    message = ride['message']
#    
#     capture = re.findall('|'.join(['from\s([\w\s]+)to\s([\w\s]+)',\
#             '([\w\s]+)to\s([\w\s]+)',\
#             '(\w+)\s-+>\s([\w\s]+)']), message)

    capture = re.findall('from\s([\w\s]+)to\s([\w\s]+)', message)
    try:
        origin = capture[0][0] or 'Waterloo'
        destination = capture[0][1] or ''
    except:
        print "Error parsing message %s" % message
    
    date = re.findall('on(\w\s)+', message) or '<no date>'
    time = re.findall('at(\w\s)+', message) or '<no time>'
    seat_match = re.findall('(\d)\sseats\s', message)
    num_seats = seat_match[0][0] if seat_match else 3 

    try:
        price = re.findall('\$(\d+)$', message)[0][0]
    except:
        price = '$5'
    
    print "FBID: %s\nUSER: %s\nORIGIN: %s\nDESTINATION: %s\nPRICE: %s\nSEATS: %s\n\n" % (user_fbid, user_name, origin, destination, price, num_seats)
