#!/usr/bin/expect

spawn /root/padres/bin/startclient -cli -i ClientX -b socket://192.168.2.21:1100/BrokerB
expect ">>"
send "a \[class,eq,'temp'\],\[area,eq,'tor'\],\[value,<,100\]\n"
expect ">>"
send "s \[class,eq,'temp'\],\[area,eq,'tor'\],\[value,<,0\]\n"
expect ">>"
send "p \[class,'temp'\],\[area,'tor'\],\[value,-10\]\n"
expect ">>"
send "exit\n"
expect eof

