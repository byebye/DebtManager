#!/bin/bash

rm ~/.java/deployment/log/*
javaws client.jnlp
sleep 8
cat ~/.java/deployment/log/*.trace
