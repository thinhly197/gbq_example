#!/usr/bin/env bash

log_path='logs/messages.log'
send_limit_message='Maximum send rate exceeded when sending email to'

echo "********** FAILURE SENDING EMAILS ********"
temp=(`cat $log_path | grep "$send_limit_message" | grep -EiEio '\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b' | tr '\n' ' ' | xargs -n1 | sort -u | xargs | tr ' ' '\n'`)
for i in "${temp[@]}"
do :
    lines=`cat $log_path | grep "$send_limit_message" | grep $i | wc -l`
    echo "Email: $i. Retries: $lines"
    # $((lines/2))
done
echo ""
echo "******************************************"