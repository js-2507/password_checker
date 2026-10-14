## WARNING

*THIS IS TECHNICALLY MALWARE. DO NOT USE THIS ON SYSTEMS OR PEOPLE UNLESS YOU HAVE CONSENT OR IT IS YOUR OWN SYSTEM. USE AT YOUR OWN RISK.*

## Description

<b>This is a personal cybersecurity project to learn how phishing malware is effective.</b><br>
If this software were to be packaged as a .jar file and sent to targeted users who are not very tech-literate, this would be an easy way to harvest credentials.<br>
All they would have to do is download and run the file, then fill it out with as many logins as they want to check. Now all those logins are compromised.<br>
If this software would have a way to send the compromimsed credentials over a network connection to an external database server that an attacker manages, then they attacker could send this malware to many users (through email or other methods) and liklely get a good amount of credentials sent to their database where they can then go and try to log in to websites and use other methods like social engineering to try and bypass existing login security methods such as Multi-Factor Authentication.

## How it Works

This is a simple phishing software that tricks users into thinking its a legit password breach checking tool. <br> 
It preys on less tech-literate users who don't know any better (such as kids or elderly people).<br>
Its very straightforward, it has a main window where it describes its "purpose", the next window asks for a website and its credentials that you want to check against a "breached passwords database", then it has a completley fake loading bar where is "searches" the "database" (where its really just creating a "data.txt" file and writing the information the user inputs onto that file), the loading bar makes it seem like its actually looking through a database, then it always ends with a window saying their password was not found in the database and that their login is secure. 

## Why it Could Work

1. It uses a GUI instead of a CLI interface, which makes regular and less tech-litertate users trust it more and feel more comfortable with it
2. It uses buzzwords like "breached", "database", "records" making it sound official and more legit to regular or less tech-literate users
3. The loading bar makes users think its actually looking through a database, even though its functionally useless and is just an animation
4. It ends with telling the user that their login is secure, leaving less room for worry
5. It gives the attacker the users login for a specific website, which can include their personal email(s) which attackers can exploit in the future, along with a password which can also be further exploited if the user uses that same password for multiple logins

## How it Could be Even Scarier

If this software had network connectivity, the attacker could send the harvested credentials to their own external database or server through various methods, such as:
1. Sending an automated email with the harvested credentials to an email the attacker has access to
2. Transmitting the harvested credentials to a database server the attacker manages
3. Sending an encrypted .txt file with the harvested credentials to a server the attacker manages
