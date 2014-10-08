##!/usr/bin/env bash
#clean the data produced by version 2 before running the version 3 instance
rm -rf ESData/*
LD_LIBRARY_PATH=.:$LD_LIBRARY_PATH MONO_GC_DEBUG=clear-at-gc /opt/eventstore/clusternode --db ./ESData 
