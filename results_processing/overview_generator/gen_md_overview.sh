#!/bin/bash

if [ ! -d data ]; then
    echo 'Missing "data" dir!'
    exit
fi

echo "# Results" > overview.md

echo >> overview.md
echo 'Below, you can see results for pairs of a workload and a data balancing algorithm, always code-named'\
     'as "workload-algorithm". They are ordered first by the workload name and second by the algorithm name.'\
     >> overview.md

for data_set in ./data/*; do
    echo >> overview.md
    echo "## $(basename "$data_set")" >> overview.md
    echo "| Node Size | Load | Data Item Churn |" >> overview.md
    echo "| --------- | ---- | --------------- |" >> overview.md
    echo "| [![]($data_set/node_size.png)]($data_set/node_size.png)" \
         "| [![]($data_set/load.png)]($data_set/load.png)" \
         "| [![]($data_set/data_item_churn.png)]($data_set/data_item_churn.png) |" >> overview.md
done
