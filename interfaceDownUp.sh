#!/bin/bash

/bin/sleep 10
/sbin/ifconfig $1 down
/bin/sleep $2
/sbin/ifconfig $1 up