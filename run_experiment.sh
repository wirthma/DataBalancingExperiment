#!/bin/bash

# Simulates the data balancing algorithm (specified by a properties file in $1) under the given workload
# (specified by the properties file in $2). The simulation is called as $3.
function simulate {
    algorithm="$1" workload="$2" mvn -f impl/pom.xml exec:java -Dexec.mainClass="cz.cuni.mff.dbe.simulator.Main"

    cd metrics || exit
    cp -rf ../results_processing/metrics_processing/* .
    METRICS_PATH=. make
    rm Makefile *.sh
    cd ..

    mkdir -p "results/data/"
    mv metrics "results/data/$3"
}

rm -rf results

simulate 'algorithm/onenode' 'workload/lru' "OneNode-Lru"
simulate 'algorithm/random' 'workload/lru' "Random-Lru"
simulate 'algorithm/roundrobin' 'workload/lru' "RoundRobin-Lru"
simulate 'algorithm/uniform' 'workload/lru' "Uniform-Lru"

cp -f results_processing/overview_generator/gen_md_overview.sh results
cd results || exit
bash gen_md_overview.sh
rm gen_md_overview.sh
cd ..

