#!/bin/bash

if [ ! -d data ]; then
    echo 'Missing "data" dir!'
    exit
fi

echo "# Results" > overview.md

for data_set in ./data/*; do
    echo >> overview.md
    echo "## $(basename "$data_set")" >> overview.md
    echo "| Node Size | Load | Data Item Churn |" >> overview.md
    echo "| --------- | ---- | --------------- |" >> overview.md
    echo "| [![]($data_set/node_size.png)]($data_set/node_size.png)" \
         "| [![]($data_set/load.png)]($data_set/load.png)" \
         "| [![]($data_set/data_item_churn.png)]($data_set/data_item_churn.png) |" >> overview.md
done
