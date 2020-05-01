# PAS
Public address system

Public Address system is an android based system.It consists of two applications:
1) PAS system for citizens
2) PAS systsem for authorities like various government departments


1) PAS system for citizens 

It allows citizens to view various messages posted by government departmentstargeted towards the region they are currently living. 
e.g. if citizen A is living in area with pincode-123456, then all the government messages that are intended for pincode 123456 will be visible to citizen A.

The application also allows citizens to share this messages to other platforms like whatsapp, facebook etc.

1) Registration :

Registartion is done by taking citizens email id, location information like pincode, district, state etc.
Pin code validity is determined through REST API.Once clicked on registration, email verification link is sent to provided email address. 
Once email is verified, citizen is allowed to sign in to application.

![](registration_page.jpeg)


2) Home page:

Once signed-in, citizen can see all the posts linked to his/her pincode with latest post first.
Citizen can see all the attachments of that post. Also citizen can share the same post message to other platforms like whatsapp , facebook etc. using share button under each post.

![](home_page1.jpeg)

