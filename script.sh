#!/bin/bash
sudo docker build -t nhstat .
sudo docker run --name nhstat -d nhstat