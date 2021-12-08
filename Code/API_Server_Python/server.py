#!/usr/bin/python
# -*- coding: utf-8 -*-

########
#IMPORT#
########
import http.server
import threading
import time
import pymysql
import random
import os
########

BSDCONFIG = 0
if (BSDCONFIG==0):
    dbHost="localhost"
    dbUser="root"
    dbPass="xxxxxxxxxxxxx"
    dbName="tmData"
else:
    dbHost="217.182.68.69"
    dbUser="outsideTest"
    dbPass="1234"
    dbName="tmData"

timeChange = "10:00:00" #set Here the time when changing loca | this format 09:33:00
osName = os.name



class WebServer(threading.Thread):
    def run(self):
        # -*- coding: utf-8 -*-
        ################
        #INITIALISATION#
        ################
        if (osName != "nt"): # not windows
            print("LINUX OS VERSION")
            os.chdir("/var/www/pyscript") #To ENable on Rasppi
        else:
            print("WINDOWS OS VERSION")

        serverAppPort = 8080
        serverAdress = ("",serverAppPort)
        serverManager = http.server.HTTPServer
        handlerRequest = http.server.CGIHTTPRequestHandler
        handlerRequest.cgi_directories = ["/"]
        print("THE SERVER HAS STARTED")
        http.server.CGIHTTPRequestHandler.have_fork=False
        httpd = serverManager(serverAdress, handlerRequest)
        httpd.serve_forever()

        ################

class coordUpdaterTimeChecker(threading.Thread):
    def run(self):
        oldId = 0
        newLocId = 0
        manuUpdate = 0
        while True:
            time.sleep(0.9)
            print("o")
            timeLocStr=time.strftime("%H:%M:%S",time.localtime())
            timeLocFordb = time.strftime("%Y:%m:%d:%H:%M:%S",time.localtime())
            db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
            cursor = db.cursor()
            cursor.execute("SELECT * FROM command WHERE command=1")
            data = cursor.fetchall()
            for row in data:
                manuUpdate = row[2]
                cursor.execute("UPDATE command SET value = 0 WHERE id = 0")
                db.commit()
            if (timeLocStr==timeChange or manuUpdate == 1):
                print("Changement De Coord")
                dataId = []
                dataCoord = []
                dataName = []
                db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
                cursor = db.cursor()
                cursor.execute("SELECT * FROM location")
                data = cursor.fetchall()
                for row in data:
                    dataId.append(row[0])
                    dataCoord.append(row[1])
                    dataName.append(row[2])
                numOfId = len(dataId)
                while (newLocId == oldId):
                    newLocId = random.randint(0,(numOfId-1))
                    print("Sucess")

                oldId = newLocId
                print("La Nouvelle Location sera: "+str(dataName[newLocId])+" avec les coord "+str(dataCoord[newLocId])+"; id:"+str(dataId[newLocId]))
                cursor.execute("INSERT INTO history (coord,name,foundList,dateheur) VALUES ( '" + str(dataCoord[newLocId]) + "','"+str(dataName[newLocId])+"','','"+str(timeLocFordb)+"' ) ")
                db.commit()
                print("Temporisation...")
                time.sleep(5)
                print("Waiting")



time.sleep(10)
Th1 = WebServer()
Th1.start()
Th2 = coordUpdaterTimeChecker()
Th2.start()
