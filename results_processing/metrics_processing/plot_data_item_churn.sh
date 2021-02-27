#!/bin/bash

help="Bad params: $0 {data dir path} {pdf|png {width} {height}}"
if [ "$2" != 'pdf' -o $# -ne 2 ] && [ "$2" != 'png' -o $# -ne 4 ]; then echo "$help"; exit; fi
data_dir="$1"
output_type="$2"

if [ "$output_type" = 'pdf' ]; then
    gnuplot_script="
    set terminal pdf
    set output 'data_item_churn.pdf'"
else
    width="$3"
    height="$4"
    gnuplot_script="
    set terminal png size $width,$height
    set output 'data_item_churn.png'"
fi;

gnuplot_script="$gnuplot_script

    set datafile separator \",\"

    set title \"Data Item Churn\"
    set key outside
    set grid
    unset border

    set xlabel \"Iteration\"
    set xrange [1:*]
    set ylabel \"Churn [data items]\"
    set yrange [0:*]

    plot"

gnuplot_script="$gnuplot_script '$data_dir/dataitemchurn.created' using 1:2 with lines title 'Created', "
gnuplot_script="$gnuplot_script '$data_dir/dataitemchurn.removed' using 1:2 with lines title 'Removed', "

echo "Running the following gnuplot script:$gnuplot_script"

echo "$gnuplot_script" |gnuplot -
