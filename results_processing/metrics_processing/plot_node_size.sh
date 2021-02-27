#!/bin/bash

help="Bad params: $0 {data dir path} {pdf|png {width} {height}}"
if [ "$2" != 'pdf' -o $# -ne 2 ] && [ "$2" != 'png' -o $# -ne 4 ]; then echo "$help"; exit; fi
data_dir="$1"
output_type="$2"

if [ "$output_type" = 'pdf' ]; then
    gnuplot_script="
    set terminal pdf
    set output 'node_size.pdf'"
else
    width="$3"
    height="$4"
    gnuplot_script="
    set terminal png size $width,$height
    set output 'node_size.png'"
fi;

gnuplot_script="$gnuplot_script

    set datafile separator \",\"

    set title \"Node Size Evolution\"
    set key outside
    set grid
    unset border

    set xlabel \"Iteration\"
    set xrange [1:*]
    set ylabel \"Node Size [data items]\"
    set yrange [0:*]

    plot"

for loadDataFile in "$data_dir"/datadistribution.node*; do
    title="node ${loadDataFile//[^0-9]/}"
    gnuplot_script="$gnuplot_script '$loadDataFile' using 1:2 with lines title '$title', "
done

echo "Running the following gnuplot script:$gnuplot_script"

echo "$gnuplot_script" |gnuplot -
