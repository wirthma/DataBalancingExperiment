#!/bin/bash

gnuplot_script="
    set datafile separator \",\"

    set title \"Data Item Churn\"
    set key outside
    set grid
    unset border

    set xlabel \"Iteration\"
    set xrange [1:*]
    set ylabel \"Churn [data items]\"
    set yrange [0:*]

    set terminal pdf
    set output 'data_item_churn.pdf'

    plot"

if [ $# -ne 1 ]; then
    echo "Bad params: $0 {data dir path}"
    exit
fi
data_dir="$1"

gnuplot_script="$gnuplot_script '$data_dir/dataitemchurn.created' using 1:2 with lines title 'Created', "
gnuplot_script="$gnuplot_script '$data_dir/dataitemchurn.removed' using 1:2 with lines title 'Removed', "

echo "Running the following gnuplot script:$gnuplot_script"

echo "$gnuplot_script" |gnuplot -
