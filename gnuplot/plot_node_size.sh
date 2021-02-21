#!/bin/bash

gnuplot_script="
    set datafile separator \",\"

    set title \"Node Size Evolution\"
    set key outside
    set grid
    unset border

    set xlabel \"Iteration\"
    set xrange [1:*]
    set ylabel \"Node Size [data items]\"
    set yrange [0:*]

    set terminal pdf
    set output 'node_size.pdf'

    plot"

if [ $# -ne 1 ]; then
    echo "Bad params: $0 {data dir path}"
fi
data_dir="$1"

for loadDataFile in "$data_dir"/model.datadistribution.node*; do
    title="node ${loadDataFile//[^0-9]/}"
    gnuplot_script="$gnuplot_script '$loadDataFile' using 1:2 with lines title '$title', "
done

echo "Running the following gnuplot script:$gnuplot_script"

echo "$gnuplot_script" |gnuplot -
