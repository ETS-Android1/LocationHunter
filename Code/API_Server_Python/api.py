#!/usr/bin/python3
# -*- coding: utf-8 -*

########
#IMPORT#
########
import cgi
import pymysql
from math import *
import time
########

BSDCONFIG = 0
if (BSDCONFIG==0):
    dbHost="localhost"
    dbUser="root"
    dbPass="xxxxxxxxxxxxxxxxxxxxxxxx"
    dbName="tmData"


headerMess = "ServerApp"
distanceGameVar = 50
#########
#PREPARE#
#########
form = cgi.FieldStorage()
commandGet = form.getvalue("command")
print("Content-type: text/html; charset=utf-8\n")
html = """ -["""+headerMess+"""]- """
#########

def pointsCal(countF=0,dateG="0000:00:00:00:00:00"):
    try:
        newPts = 0
        if (countF == 0):
            newPts = 20
        elif (countF == 1):
            newPts = 10
        elif (countF == 2):
            newPts = 5
        else:
            newPts = 3
        dateArray = dateG.split(":")
        actualTime = time.strftime("%Y:%m:%d:%H:%M:%S",time.localtime()).split(":")
        if (dateArray[0]==actualTime[0] and dateArray[1]==actualTime[1] and dateArray[2]==actualTime[2] and abs((float(dateArray[3])+(float(dateArray[4])/60))-(float(actualTime[3])+(float(actualTime[4])/60)))<1):
            newPts = newPts * 2
    except Exception as e:
        newPts = 0
    return newPts


def distCalculator(lat1, long1, lat2, long2):
    lat1 = (lat1*pi)/180
    lat2 = (lat2*pi)/180
    long1 = (long1*pi)/180
    long2 = (long2*pi)/180
    dLat = lat2-lat1
    dLong = long2-long1
    Va = sin(dLat/2)**2 + (sin(dLong/2)**2) * cos(lat1) * cos(lat2)
    Vc = 2*atan((sqrt(Va))/(sqrt(1-Va)))
    return Vc*6371008

def fConnexion(email, password):
    try:
        db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
        cursor = db.cursor()
        cursor.execute("SELECT * FROM account WHERE enabled = 1")
        data = cursor.fetchall()
        returnPrep = ""
        for row in data:
            if (row[1]==email and row[3]==password):
                returnPrep = "Match"
        if (returnPrep != "Match"):
            returnPrep = "NoMatch"
    except:
        returnPrep = "NoMatch"
    return returnPrep

def fGetToday():
    try:
        db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
        cursor = db.cursor()
        cursor.execute("SELECT * FROM history ORDER BY id DESC LIMIT 1")
        data = cursor.fetchall()
        useData = []
        for row in data:
            useData.append(row[0])
            useData.append(row[1])
            useData.append(row[2])
            useData.append(row[3])
            useData.append(row[4])
        return ";"+ str(useData[0])+";"+str(useData[1])+";"+str(useData[2])+";"+str(useData[3])+";"+str(useData[4])+";"
    except Exception as e:
        return "Error With Server"

def fGetVersion():
    try:
        db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
        cursor = db.cursor()
        cursor.execute("SELECT * FROM command WHERE command = 2")
        data = cursor.fetchall()
        tobereturn = ""
        for row in data:
            tobereturn = str(row[2])
        return tobereturn
    except:
        return "Error With Server"


if (commandGet=="getToday"): #command for application to get the destination to search
    try:
        html = (html + fGetToday())
    except Exception as e:
        html = (html + "Error while using API")

elif (commandGet=="connexion"):
    try:
        email = form.getvalue("email")
        password = form.getvalue("password")
        html = html + fConnexion(email,password)
    except:
        html = (html + "Error while using API")

elif (commandGet=="getVersion"):
    try:
        html = (html + fGetVersion())
    except:
        html = (html + "Error while using API")

elif (commandGet=="sendIt"):
    try:
        latS = float(form.getvalue("latS"))
        longS = float(form.getvalue("longS"))
        distS = float(form.getvalue("dist"))
        email = form.getvalue("email")
        password = form.getvalue("password")

        connectedS = fConnexion(email,password)
        if(connectedS=="Match"):
            db = pymysql.connect(dbHost,dbUser,dbPass,dbName)
            cursor = db.cursor()
            cursor.execute("SELECT * FROM history ORDER BY id DESC LIMIT 1")
            data = cursor.fetchall()
            coordBrut = ""
            playerList = ""
            idGet = 0
            date = ""
            for row in data:
                idGet = row[0]
                coordBrut = row[1]
                playerList = row[3]
                dateID = row[4]
            coordBrut = coordBrut.split(",")
            pListBefBroken = playerList.split(",")
            countPListBefBroken = len(pListBefBroken)-1
            if (email not in pListBefBroken):
                latG = float(coordBrut[0])
                longG = float(coordBrut[1])
                distL = distCalculator(latS,longS,latG,longG)
                if (int(distL)<=int(distS)+1 and int(distL)>=int(distS)-1):
                    if (distL<distanceGameVar):
                        playerList = playerList + "," + email
                        cursor.execute("UPDATE history SET foundList = '"+playerList+"' WHERE id ='"+str(idGet)+"'")
                        db.commit()
                        cursor = db.cursor()
                        cursor.execute("SELECT * FROM account WHERE email = '"+email+"'")
                        dataPoints = cursor.fetchall()
                        pointsTotal = 0
                        pointsNew = 0
                        pointsNew = pointsCal(countPListBefBroken,dateID)
                        for row in dataPoints:
                            pointsTotal = int(row[5]) + pointsNew
                        cursor.execute("UPDATE account SET points = "+str(pointsTotal)+" WHERE email = '"+email+"'")
                        db.commit()
                        html = (html + "oui;"+str(pointsNew))
                    else:
                        html = (html + "non")
                else:
                    html = (html + "Error while using API")
            else:
                html = (html+"You have already played today")

        else:
            html = (html + "You are not connected with a valid account")

    except Exception as e:
        html = (html + "Error while using API")

else:
    html = (html + "Error while using API")


htmlEncoded = ""
for x in list(html):
    if (x=="ê"):
        x = "/ecirc/"
    elif(x=="é"):
        x = "/eacute/"
    elif (x=="è"):
        x= "/egrave/"
    elif (x=="à"):
        x = "/agrave/"
    elif (x=="â"):
        x = "/acirc/"
    elif (x=="ç"):
        x = "/ccedil/"

    htmlEncoded = htmlEncoded + x


print(htmlEncoded)
