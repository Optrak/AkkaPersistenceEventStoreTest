#!/bin/sh
mono-sgen /opt/eventstore2/EventStore.SingleNode.exe --db ./ESData --run-projections=all
