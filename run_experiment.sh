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

simulate    'algorithm/onenode'             'workload/favoriteitems5'     "FavoriteItems5-OneNode"
simulate    'algorithm/random'              'workload/favoriteitems5'     "FavoriteItems5-Random"
simulate    'algorithm/roundrobin'          'workload/favoriteitems5'     "FavoriteItems5-RoundRobin"
simulate    'algorithm/uniform'             'workload/favoriteitems5'     "FavoriteItems5-Uniform"
simulate    'algorithm/consistenthashing'   'workload/favoriteitems5'     "FavoriteItems5-ConsistentHashing"
simulate    'algorithm/slicer'              'workload/favoriteitems5'     "FavoriteItems5-Slicer"

simulate    'algorithm/roundrobin'          'workload/favoriteitems7'     "FavoriteItems7-RoundRobin"

simulate    'algorithm/onenode'             'workload/lru'                "Lru-OneNode"
simulate    'algorithm/random'              'workload/lru'                "Lru-Random"
simulate    'algorithm/roundrobin'          'workload/lru'                "Lru-RoundRobin"
simulate    'algorithm/uniform'             'workload/lru'                "Lru-Uniform"
simulate    'algorithm/consistenthashing'   'workload/lru'                "Lru-ConsistentHashing"
simulate    'algorithm/slicer'              'workload/lru'                "Lru-Slicer"

simulate    'algorithm/onenode'             'workload/uniform'            "Uniform-OneNode"
simulate    'algorithm/random'              'workload/uniform'            "Uniform-Random"
simulate    'algorithm/roundrobin'          'workload/uniform'            "Uniform-RoundRobin"
simulate    'algorithm/uniform'             'workload/uniform'            "Uniform-Uniform"
simulate    'algorithm/consistenthashing'   'workload/uniform'            "Uniform-ConsistentHashing"
simulate    'algorithm/slicer'              'workload/uniform'            "Uniform-Slicer"

simulate    'algorithm/onenode'             'workload/uniformdist'        "UniformDist-OneNode"
simulate    'algorithm/random'              'workload/uniformdist'        "UniformDist-Random"
simulate    'algorithm/roundrobin'          'workload/uniformdist'        "UniformDist-RoundRobin"
simulate    'algorithm/uniform'             'workload/uniformdist'        "UniformDist-Uniform"
simulate    'algorithm/consistenthashing'   'workload/uniformdist'        "UniformDist-ConsistentHashing"
simulate    'algorithm/slicer'              'workload/uniformdist'        "UniformDist-Slicer"

cp -f results_processing/overview_generator/gen_md_overview.sh results
cd results || exit
bash gen_md_overview.sh
rm gen_md_overview.sh
cd ..

